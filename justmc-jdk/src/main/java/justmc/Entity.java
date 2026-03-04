package justmc;

import justmc.annotation.Inline;

@Inline
public enum Entity {
    CURRENT,
    DEFAULT,
    KILLER,
    DAMAGER,
    SHOOTER,
    PROJECTILE,
    VICTIM,
    RANDOM,
    ALL_MOBS,
    ALL,
    LAST;

    public native void operation(String id, CopyableMap<Text, Primitive> args);
}
