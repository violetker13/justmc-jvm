package justmc;

import justmc.annotation.Inline;

@Inline
public final class Block implements Primitive {
    public static native Block of(String id);
}
