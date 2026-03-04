package justmc.event.player;

import justmc.GameValue;
import justmc.Unsafe;
import justmc.annotation.Event;
import justmc.annotation.Inline;
import justmc.event.CancellableEvent;
import justmc.event.PlayerEvent;

@Inline
@Event(id = "player_chat")
public final class PlayerChatEvent implements PlayerEvent, CancellableEvent {
    private PlayerChatEvent() {}

    public String getMessage() {
        return Unsafe.asString(GameValue.get("event_chat_message"));
    }
}
