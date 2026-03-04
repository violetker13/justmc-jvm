package justmc.event.world;

import justmc.annotation.Event;
import justmc.annotation.Inline;

@Inline
@Event(id = "world_stop")
public final class WorldStopEvent {
    private WorldStopEvent() {}
}
