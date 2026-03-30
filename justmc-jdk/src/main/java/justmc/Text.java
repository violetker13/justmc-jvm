package justmc;

import justmc.annotation.Inline;
import justmc.enums.TextParsing;
import org.jetbrains.annotations.NotNull;

@Inline
public final class Text extends Primitive {
    private Text() {}

    public static native @NotNull Text plain(String text);

    public static native @NotNull Text legacy(String text);

    public static native @NotNull Text mini(String text);

    public static native @NotNull Text json(String text);

    public int getLength() {
        var result = Variable.result();
        Unsafe.operation("set_variable_text_length", MapPrimitive.of(
                Pair.of("variable", result),
                Pair.of("text", this)
        ));
        return Unsafe.asInt(result);
    }

    public @NotNull Text setParsing(TextParsing parsing) {
        var result = Variable.result();
        Unsafe.operation("set_variable_change_component_parsing", MapPrimitive.of(
                Pair.of("variable", result),
                Pair.of("component", this),
                Pair.of("parsing", parsing)
        ));
        return Unsafe.cast(result);
    }

    public native @NotNull Text plus(Primitive other);
}
