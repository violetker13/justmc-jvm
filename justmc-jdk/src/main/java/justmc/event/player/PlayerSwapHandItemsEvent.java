package justmc.event.player;

import justmc.annotation.Event;
import justmc.annotation.Inline;
import justmc.event.CancellableEvent;
import justmc.event.ItemEvent;
import justmc.event.PlayerEvent;

@Inline
@Event(id = "player_swap_hands")
public final class PlayerSwapHandItemsEvent implements PlayerEvent, CancellableEvent, ItemEvent {
    private PlayerSwapHandItemsEvent() {}
}
