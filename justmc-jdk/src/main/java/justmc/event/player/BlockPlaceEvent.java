package justmc.event.player;

import justmc.annotation.Event;
import justmc.annotation.Inline;
import justmc.event.*;

@Inline
@Event(id = "player_place_block")
public final class BlockPlaceEvent implements PlayerEvent, CancellableEvent, BlockEvent, BlockFaceEvent, EquipmentSlotEvent {
    private BlockPlaceEvent() {}
}
