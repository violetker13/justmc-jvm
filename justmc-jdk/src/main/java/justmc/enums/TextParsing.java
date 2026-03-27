package justmc.enums;

import justmc.EnumPrimitive;
import justmc.Unsafe;

public final class TextParsing extends EnumPrimitive {
    public static final TextParsing PLAIN = Unsafe.cast(EnumPrimitive.of("plain"));
    public static final TextParsing LEGACY = Unsafe.cast(EnumPrimitive.of("legacy"));
    public static final TextParsing MINIMESSAGE = Unsafe.cast(EnumPrimitive.of("minimessage"));
    public static final TextParsing JSON = Unsafe.cast(EnumPrimitive.of("json"));

    private TextParsing() {}
}
