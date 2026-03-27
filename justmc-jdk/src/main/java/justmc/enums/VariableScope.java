package justmc.enums;

import justmc.EnumPrimitive;
import justmc.Unsafe;

public final class VariableScope extends EnumPrimitive {
    public static final VariableScope GAME = Unsafe.cast(EnumPrimitive.of("game"));
    public static final VariableScope SAVE = Unsafe.cast(EnumPrimitive.of("save"));
    public static final VariableScope LOCAL = Unsafe.cast(EnumPrimitive.of("local"));
    public static final VariableScope LINE = Unsafe.cast(EnumPrimitive.of("line"));

    private VariableScope() {}
}
