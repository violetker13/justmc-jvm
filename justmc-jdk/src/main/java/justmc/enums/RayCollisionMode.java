package justmc.enums;

import justmc.EnumPrimitive;
import justmc.Unsafe;

public final class RayCollisionMode extends EnumPrimitive {
    public static final RayCollisionMode ONLY_BLOCKS = Unsafe.cast(EnumPrimitive.of("only_blocks"));
    public static final RayCollisionMode BLOCKS_AND_ENTITIES = Unsafe.cast(EnumPrimitive.of("blocks_and_entities"));
    public static final RayCollisionMode ONLY_ENTITIES = Unsafe.cast(EnumPrimitive.of("only_entities"));

    private RayCollisionMode() {}
}