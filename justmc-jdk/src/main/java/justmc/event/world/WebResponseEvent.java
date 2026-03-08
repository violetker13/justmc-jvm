package justmc.event.world;

import justmc.*;
import justmc.annotation.Event;
import justmc.annotation.Inline;
import justmc.event.WebEvent;

@Inline
@Event(id = "world_web_response")
public final class WebResponseEvent implements WebEvent {
    private WebResponseEvent() {}

    public Text getResponse() {
        return Unsafe.cast(GameValue.get("url_response"));
    }

    public int getResponseCode() {
        return Unsafe.asInt(GameValue.get("url_response_code"));
    }

    public MapPrimitive<Text, ListPrimitive<Text>> getHeaders() {
        return Unsafe.cast(GameValue.get("response_headers"));
    }
}
