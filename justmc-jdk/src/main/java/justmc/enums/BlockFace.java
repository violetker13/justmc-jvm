package justmc.enums;

import justmc.EnumPrimitive;
import justmc.Unsafe;

public final class BlockFace extends EnumPrimitive {
    public static final BlockFace NORTH = Unsafe.cast(EnumPrimitive.of("north"));
    public static final BlockFace SOUTH = Unsafe.cast(EnumPrimitive.of("south"));
    public static final BlockFace WEST = Unsafe.cast(EnumPrimitive.of("west"));
    public static final BlockFace EAST = Unsafe.cast(EnumPrimitive.of("east"));
    public static final BlockFace UP = Unsafe.cast(EnumPrimitive.of("up"));
    public static final BlockFace DOWN = Unsafe.cast(EnumPrimitive.of("down"));

    private BlockFace() {}
}