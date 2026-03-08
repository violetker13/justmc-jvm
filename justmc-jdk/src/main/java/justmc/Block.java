package justmc;

import justmc.annotation.Inline;

@Inline
public final class Block extends Primitive {
    private Block() {}

    public static native Block of(String id);
}
