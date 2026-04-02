package justmc;

import justmc.annotation.EventHandler;
import justmc.annotation.Inline;

/**
 * Стандартная реализация памяти (кучи), балансирующая между
 * производительностью и максимальным размером.
 * Максимальный размер - 19999 объектов, причём не важно,
 * сколько полей имеет объект. То есть каждый из этих объектов
 * может хранить по 20000 объектных полей и ещё столько же примитивных.
 */
public final class Memory {
    private static final int MIN_HEAP = 128;
    private static final int MAX_HEAP = ListPrimitive.MAX_SIZE;
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
    public static Variable getObjectFieldsVariable(Primitive ptr) {
        return Variable.game(Text.plain("o").plus(ptr));
    }

    @Inline
    public static Variable getPrimitiveFieldsVariable(Primitive ptr) {
        return Variable.game(Text.plain("p").plus(ptr));
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
                if (heapSize >= MAX_HEAP) {
                    Thread.fatalError(Text.plain("Out of memory"));
                }
                int newSize = Math.min(heapSize * 2, MAX_HEAP);
                int add = newSize - heapSize;
                ListPrimitive<NumberPrimitive> addHeap = ListPrimitive.ofNulls(add);
                objs = objs.addAll(addHeap);
                refs = refs.addAll(addHeap);
                // Добавляем свободные указатели
                Util.repeat(add, () -> free = free.add(NumberPrimitive.of(++heapSize)));
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
            Thread.wait(93);
            gc();
        }
    }

    public static void gc() {
        mark.forEach((ptr) -> {
            free.set(--freeHead, ptr);
            holding--;
            if (getObjectFieldsVariable(ptr).exists()) {
                Unsafe.<ListPrimitive<NumberPrimitive>>cast(getObjectFieldsVariable(ptr)).forEach((field) -> {
                    removeRef(Unsafe.asInt(field));
                });
            }
            Variable.purge(ListPrimitive.of(
                    getObjectFieldsVariable(ptr).getName(),
                    getPrimitiveFieldsVariable(ptr).getName()
            ));
        });
        mark = ListPrimitive.empty();
    }
}
