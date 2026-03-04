package justmc.event;

import justmc.GameValue;
import justmc.Unsafe;
import justmc.enums.InteractionType;

public interface BlockInteractEvent extends PlayerEvent, CancellableEvent,
        BlockEvent, BlockFaceEvent, ItemEvent, EquipmentSlotEvent {
    default InteractionType getInteraction() {
        return Unsafe.asEnum(GameValue.get("event_interaction"));
    }
}
