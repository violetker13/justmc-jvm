package justmc.event.player;

import justmc.annotation.Event;
import justmc.annotation.Inline;
import justmc.event.BlockInteractEvent;

@Inline
@Event(id = "player_left_click")
public final class PlayerLeftClickEvent implements BlockInteractEvent {
    private PlayerLeftClickEvent() {}
}
