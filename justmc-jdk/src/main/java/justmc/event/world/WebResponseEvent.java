package justmc.event.world;

import justmc.*;
import justmc.annotation.Event;
import justmc.annotation.Inline;
import justmc.event.WebEvent;

@Inline
@Event(id = "world_web_response")
public final class WebResponseEvent implements WebEvent {
    private WebResponseEvent() {}

    public String getResponse() {
        return Unsafe.asString(GameValue.get("url_response"));
    }

    public int getResponseCode() {
        return Unsafe.asInt(GameValue.get("url_response_code"));
    }

    public CopyableMap<Text, CopyableList<Text>> getHeaders() {
        return Unsafe.asCopyableMap(GameValue.get("response_headers"));
    }
}
