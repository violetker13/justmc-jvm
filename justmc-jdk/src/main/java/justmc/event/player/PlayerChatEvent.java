package justmc.event.player;

import justmc.Text;
import justmc.Unsafe;
import justmc.World;
import justmc.annotation.Event;
import justmc.annotation.Inline;
import justmc.event.CancellableEvent;
import justmc.event.PlayerEvent;

@Inline
@Event(id = "player_chat")
public final class PlayerChatEvent implements PlayerEvent, CancellableEvent {
    private PlayerChatEvent() {}

    public Text getMessage() {
        return Unsafe.cast(World.getValue("event_chat_message"));
    }
}
