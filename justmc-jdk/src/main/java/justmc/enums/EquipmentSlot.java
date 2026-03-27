package justmc.enums;

import justmc.EnumPrimitive;
import justmc.Unsafe;

public final class EquipmentSlot extends EnumPrimitive {
    public static final EquipmentSlot HAND = Unsafe.cast(EnumPrimitive.of("hand"));
    public static final EquipmentSlot OFF_HAND = Unsafe.cast(EnumPrimitive.of("off_hand"));

    private EquipmentSlot() {}
}
