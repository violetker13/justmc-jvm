package justmc.event.player;

import justmc.annotation.Event;
import justmc.annotation.Inline;
import justmc.event.PlayerEvent;

@Inline
@Event(id = "player_quit")
public final class PlayerQuitEvent implements PlayerEvent {
    private PlayerQuitEvent() {}
}
