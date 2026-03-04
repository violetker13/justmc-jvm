package justmc.event.world;

import justmc.annotation.Event;
import justmc.annotation.Inline;

@Inline
@Event(id = "world_start")
public final class WorldStartEvent {
    private WorldStartEvent() {}
}
