package justmc.event;

import justmc.GameValue;
import justmc.Unsafe;
import justmc.enums.EquipmentSlot;

public interface EquipmentSlotEvent {
    default EquipmentSlot getEquipmentSlot() {
        return Unsafe.cast(GameValue.get("event_equipment_slot"));
    }
}
