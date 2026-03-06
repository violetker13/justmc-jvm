package justmc;

import justmc.annotation.Inline;
import justmc.enums.TextParsing;

@Inline
public final class Text implements Primitive {
    private Text() {}

    public static native Text of(String text, TextParsing parsing);

    public static Text plain(String text) {
        return of(text, TextParsing.PLAIN);
    }

    public static Text legacy(String text) {
        return of(text, TextParsing.LEGACY);
    }

    public static Text mini(String text) {
        return of(text, TextParsing.MINIMESSAGE);
    }

    public static Text json(String text) {
        return of(text, TextParsing.JSON);
    }

    public int getLength() {
        var result = Variable.temp();
        Unsafe.operation("set_variable_text_length", CopyableMap.of(
                Pair.of("variable", result),
                Pair.of("text", this)
        ));
        return Unsafe.cast(result);
    }

    public Text setParsing(TextParsing parsing) {
        var result = Variable.temp();
        Unsafe.operation("set_variable_change_component_parsing", CopyableMap.of(
                Pair.of("variable", result),
                Pair.of("component", this),
                Pair.of("parsing", parsing)
        ));
        return Unsafe.cast(result);
    }
}
