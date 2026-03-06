package justmc;

import justmc.annotation.Inline;
import justmc.enums.VariableScope;

@Inline
public final class Variable implements Primitive {
    private Variable() {}

    public static final int MAX_SAVE_VARIABLES = 800000;
    public static final int MAX_GAME_VARIABLES = 250000;
    public static final int MAX_LOCAL_VARIABLES = 25000;
    public static final int MAX_LINE_VARIABLES = 25000;

    public static native Variable save(String name);
    public static native Variable game(String name);
    public static native Variable local(String name);
    public static native Variable line(String name);
    public static native Variable temp();
    public static native Variable free(Variable variable);

    public native String getName();
    public native VariableScope getScope();
}
