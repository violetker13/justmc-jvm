package justmc;

import justmc.annotation.Inline;

@Inline
public final class Variable extends Primitive {
    private Variable() {}

    public static final int MAX_SAVE_VARIABLES = 800000;
    public static final int MAX_GAME_VARIABLES = 250000;
    public static final int MAX_LOCAL_VARIABLES = 25000;
    public static final int MAX_LINE_VARIABLES = 25000;

    /**
     * @param name Имя переменной
     * @return Сохранённая переменная с данным именем
     */
    public static native Variable save(Primitive name);

    /**
     * @param name Имя переменной
     * @return Игровая переменная с данным именем
     */
    public static native Variable game(Primitive name);

    /**
     * @param name Имя переменной
     * @return Локальная переменная с данным именем
     */
    public static native Variable local(Primitive name);

    /**
     * @param name Имя переменной
     * @return Строчная переменная с данным именем
     */
    public static native Variable line(Primitive name);

    /**
     * @return Строчная переменная со сгенерированным именем
     */
    public static native Variable temp();

    /**
     * @return Переменная, в которую ожидается установка возвращаемого значения метода
     */
    public static native Variable result();

    public static void purge(Primitive names) {
        Unsafe.operation("set_variable_purge", MapPrimitive.of(
                Pair.of("names", names)
        ));
    }

    public native Text getName();

    public void setValue(Primitive value) {
        Unsafe.operation("set_variable_value", MapPrimitive.of(
                Pair.of("variable", this),
                Pair.of("value", value)
        ));
    }

    public void increment() {
        Unsafe.operation("set_variable_increment", MapPrimitive.of(
                Pair.of("variable", this)
        ));
    }

    public void decrement() {
        Unsafe.operation("set_variable_decrement", MapPrimitive.of(
                Pair.of("variable", this)
        ));
    }

    public boolean exists() {
        return Conditional.of("if_variable_exists", MapPrimitive.of(
                Pair.of("variable", this)
        )).get();
    }
}
