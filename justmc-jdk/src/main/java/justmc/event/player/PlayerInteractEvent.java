package justmc.event.player;

import justmc.annotation.Event;
import justmc.annotation.Inline;
import justmc.event.BlockInteractEvent;

@Inline
@Event(id = "player_interact")
public final class PlayerInteractEvent implements BlockInteractEvent {
    private PlayerInteractEvent() {}
}
