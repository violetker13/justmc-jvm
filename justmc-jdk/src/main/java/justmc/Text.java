package justmc;

import justmc.annotation.Inline;

@Inline
public final class Text implements Primitive {
    private Text() {}

    public static native Text plain(String text);
    public static native Text legacy(String text);
    public static native Text mini(String text);
    public static native Text json(String text);
}
