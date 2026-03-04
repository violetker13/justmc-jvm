package justmc;

import justmc.annotation.Inline;

@Inline
public enum GameValue {
    CURRENT,
    DEFAULT,
    DEFAULT_ENTITY,
    KILLER,
    DAMAGER,
    VICTIM,
    SHOOTER,
    PROJECTILE,
    LAST_ENTITY;

    public static native GameValue ofPlayer(Player player);
    public static native GameValue ofEntity(Entity entity);
    public static native Primitive get(String id);

    public native Primitive getValue(String id);
}
