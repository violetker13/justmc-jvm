package justmc;

import justmc.annotation.Inline;

@Inline
public final class Item extends Primitive {
    private Item() {}

    public static native Item fromId(String id);
    public static native Item fromData(String id);
}
