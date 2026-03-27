package justmc.enums;

import justmc.EnumPrimitive;
import justmc.Unsafe;

public final class TextMerging extends EnumPrimitive {
    public static final TextMerging SPACES = Unsafe.cast(EnumPrimitive.of("spaces"));
    public static final TextMerging LINES = Unsafe.cast(EnumPrimitive.of("lines"));
    public static final TextMerging CONCATENATION = Unsafe.cast(EnumPrimitive.of("concatenation"));

    private TextMerging() {}
}
