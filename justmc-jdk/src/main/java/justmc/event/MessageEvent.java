package justmc.event;

import justmc.GameValue;
import justmc.Text;
import justmc.Unsafe;

public interface MessageEvent {
    default Text getMessage() {
        return Unsafe.cast(GameValue.get("event_message"));
    }
}
