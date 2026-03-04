package justmc.event;

import justmc.GameValue;
import justmc.Unsafe;

public interface MessageEvent {
    default String getMessage() {
        return Unsafe.asString(GameValue.get("event_message"));
    }
}
