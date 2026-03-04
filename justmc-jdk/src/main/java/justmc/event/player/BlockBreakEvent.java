package justmc.event.player;

import justmc.annotation.Event;
import justmc.annotation.Inline;
import justmc.event.BlockEvent;
import justmc.event.CancellableEvent;
import justmc.event.PlayerEvent;

@Inline
@Event(id = "player_break_block")
public final class BlockBreakEvent implements PlayerEvent, CancellableEvent, BlockEvent {
    private BlockBreakEvent() {}
}
