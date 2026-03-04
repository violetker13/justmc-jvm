package justmc.event.world;

import justmc.annotation.Event;
import justmc.annotation.Inline;
import justmc.event.MessageEvent;
import justmc.event.WebEvent;

@Inline
@Event(id = "world_web_exception")
public final class WebExceptionEvent implements WebEvent, MessageEvent {
    private WebExceptionEvent() {}
}
