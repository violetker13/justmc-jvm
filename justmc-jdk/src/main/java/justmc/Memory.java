package justmc;

import justmc.annotation.EventHandler;
import justmc.annotation.Inline;

public final class Memory {
    private static final int MIN_HEAP = 128;
    private static int heapSize = MIN_HEAP;
    /**
     * Данные кучи, хранящие класс каждого объекта.
     * [null, ссылка на класс, ссылка на класс, ссылка на класс, ...]
     */
    private static ListPrimitive<NumberPrimitive> objs = ListPrimitive.ofNulls(MIN_HEAP + 1);
    /**
     * Данные кучи, хранящие количество ссылок на каждой объект.
     * [null, количество ссылок, количество ссылок, количество ссылок, ...]
     */
    private static ListPrimitive<NumberPrimitive> refs = ListPrimitive.ofNulls(MIN_HEAP + 1);
    /**
     * Очередь свободных указателей.
     * Изначально хранит все указатели.
     */
    private static ListPrimitive<NumberPrimitive> free = ListPrimitive.empty();
    /**
     * Индекс начала очереди.
     */
    private static int freeHead = 0;
    /**
     * Количество удерживаемых объектов.
     */
    private static int holding = 0;
    /**
     * Список объектов, помеченных на удаление.
     */
    private static ListPrimitive<NumberPrimitive> mark = ListPrimitive.empty();

    static {
        for (int i = 1; i < MIN_HEAP; i++) {
            free = free.add(NumberPrimitive.of(i));
        }
    }

    private Memory() {}

    @Inline
    public static Variable getFieldsVariable(int ptr) {
        return Variable.game(NumberPrimitive.of(ptr));
    }

    @Inline
    public static int getClass(int ptr) {
        return Unsafe.asInt(objs.get(ptr));
    }

    @Inline
    public static int getRefs(int ptr) {
        return Unsafe.asInt(refs.get(ptr));
    }

    @Inline
    public static void setRefs(int ptr, int r) {
        refs = refs.set(ptr, NumberPrimitive.of(r));
    }

    /**
     * Добавить ссылку на объект.
     * Автоматически вставляется перед каждым дублированием ссылки:
     * при передаче аргументов, при установке в переменные и т.п.
     * @param ptr Указатель на объект
     */
    @Inline
    public static void addRef(int ptr) {
        setRefs(ptr, getRefs(ptr) + 1);
    }

    /**
     * Удалить ссылку на объект.
     * Автоматически вставляется после каждого дублирования ссылки.
     * Если ссылок не осталось, то помечает объект на удаление.
     * @param ptr Указатель на объект
     * @see #addRef(int ptr)
     */
    @Inline
    public static void removeRef(int ptr) {
        int r = getRefs(ptr) - 1;
        setRefs(ptr, r);
        if (r <= 0) {
            mark.add(NumberPrimitive.of(ptr));
        }
    }

    public static int newInstance(Class<?> clazz) {
        if (holding >= heapSize) {
            gc(); // Пробуем очистить
            if (holding >= heapSize) {
                // Если ничего не очистило, то расширяем кучу вдвое
                ListPrimitive<NumberPrimitive> addHeap = ListPrimitive.ofNulls(heapSize);
                objs = objs.addAll(addHeap);
                refs = refs.addAll(addHeap);
                // Добавляем свободные указатели
                Util.repeat(heapSize, () -> free = free.add(NumberPrimitive.of(++heapSize)));
            }
        }
        int ptr = Unsafe.asInt(free.get(freeHead++));
        objs = objs.set(ptr, NumberPrimitive.of(Unsafe.asAddress(clazz)));
        holding++;
        return ptr;
    }

    @EventHandler(id = "world_start")
    private static void cleaner() {
        while (true) {
            Util.wait(173);
            gc();
        }
    }

    public static void gc() {
        mark.forEach((ptr) -> {
            free.set(--freeHead, ptr);
            holding--;
//            Unsafe.<ListPrimitive<NumberPrimitive>>cast(Variable.game(Text.plain("f").plus(objs.get(Unsafe.asInt(ptr))))).forEach((field) -> {
//                getFieldsVariable(Unsafe.asInt(ptr)).;
//            });
        });
        mark = ListPrimitive.empty();
    }
}
