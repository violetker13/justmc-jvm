package justmc;

import justmc.annotation.Inline;

@Inline
public final class Conditional {
    private Conditional() {}

    public static native Conditional of(boolean b);

    public static native Conditional of(String id, boolean inverted, MapPrimitive<Text, Primitive> args);

    public static Conditional of(String id, boolean inverted) {
        return of(id, inverted, MapPrimitive.empty());
    }

    public static Conditional of(String id, MapPrimitive<Text, Primitive> args) {
        return of(id, false, args);
    }

    public static Conditional of(String id) {
        return of(id, false, MapPrimitive.empty());
    }

    public native boolean get();
}
