package justmc.event.player;

import justmc.annotation.Event;
import justmc.annotation.Inline;
import justmc.event.PlayerEvent;

@Inline
@Event(id = "player_join")
public final class PlayerJoinEvent implements PlayerEvent {
    private PlayerJoinEvent() {}
}
