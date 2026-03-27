package justmc.event;

import justmc.Unsafe;
import justmc.World;
import justmc.enums.EquipmentSlot;

public interface EquipmentSlotEvent {
    default EquipmentSlot getEquipmentSlot() {
        return Unsafe.cast(World.getValue("event_equipment_slot"));
    }
}
