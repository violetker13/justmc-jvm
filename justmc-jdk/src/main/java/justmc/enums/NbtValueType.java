package justmc.enums;

import justmc.EnumPrimitive;
import justmc.Unsafe;

public final class NbtValueType extends EnumPrimitive {
    public static final NbtValueType ANY_VALUE = Unsafe.cast(EnumPrimitive.of("any_value"));
    public static final NbtValueType NBT_STRING = Unsafe.cast(EnumPrimitive.of("nbt_string"));

    private NbtValueType() {}
}
