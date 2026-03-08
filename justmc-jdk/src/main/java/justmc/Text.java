package justmc;

import justmc.annotation.Inline;
import justmc.enums.TextParsing;

@Inline
public final class Text extends Primitive {
    private Text() {}

    public static native Text plain(String text);

    public static native Text legacy(String text);

    public static native Text mini(String text);

    public static native Text json(String text);

    public int getLength() {
        var result = Variable.temp();
        Unsafe.operation("set_variable_text_length", MapPrimitive.of(
                Pair.of("variable", result),
                Pair.of("text", this)
        ));
        return Unsafe.cast(result);
    }

    public Text setParsing(TextParsing parsing) {
        var result = Variable.temp();
        Unsafe.operation("set_variable_change_component_parsing", MapPrimitive.of(
                Pair.of("variable", result),
                Pair.of("component", this),
                Pair.of("parsing", EnumPrimitive.of(parsing))
        ));
        return Unsafe.cast(result);
    }
}
