package justmc.event;

import justmc.Unsafe;
import justmc.World;
import justmc.enums.InteractionType;

public interface BlockInteractEvent extends PlayerEvent, CancellableEvent,
        BlockEvent, BlockFaceEvent, ItemEvent, EquipmentSlotEvent {
    default InteractionType getInteraction() {
        return Unsafe.cast(World.getValue("event_interaction"));
    }
}
