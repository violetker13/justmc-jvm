package justmc.enums;

import justmc.EnumPrimitive;
import justmc.Unsafe;

public final class SelectionMode extends EnumPrimitive {
    public static final SelectionMode DEFAULT = Unsafe.cast(EnumPrimitive.of("default"));
    public static final SelectionMode CURRENT = Unsafe.cast(EnumPrimitive.of("current"));
    public static final SelectionMode EMPTY = Unsafe.cast(EnumPrimitive.of("empty"));
    public static final SelectionMode FOR_EACH = Unsafe.cast(EnumPrimitive.of("for_each"));

    private SelectionMode() {}
}
