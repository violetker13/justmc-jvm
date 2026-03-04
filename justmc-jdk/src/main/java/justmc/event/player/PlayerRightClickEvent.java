package justmc.event.player;

import justmc.annotation.Event;
import justmc.annotation.Inline;
import justmc.event.BlockInteractEvent;

@Inline
@Event(id = "player_right_click")
public final class PlayerRightClickEvent implements BlockInteractEvent {
    private PlayerRightClickEvent() {}
}
