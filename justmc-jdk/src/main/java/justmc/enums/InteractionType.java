package justmc.enums;

import justmc.EnumPrimitive;
import justmc.Unsafe;

public final class InteractionType extends EnumPrimitive {
    public static final InteractionType LEFT_CLICK_AIR = Unsafe.cast(EnumPrimitive.of("left_click_air"));
    public static final InteractionType LEFT_CLICK_BLOCK = Unsafe.cast(EnumPrimitive.of("left_click_block"));
    public static final InteractionType RIGHT_CLICK_AIR = Unsafe.cast(EnumPrimitive.of("right_click_air"));
    public static final InteractionType RIGHT_CLICK_BLOCK = Unsafe.cast(EnumPrimitive.of("right_click_block"));
    public static final InteractionType PHYSICAL = Unsafe.cast(EnumPrimitive.of("physical"));

    private InteractionType() {}
}
