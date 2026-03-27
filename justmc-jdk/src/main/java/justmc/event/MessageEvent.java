package justmc.event;

import justmc.Text;
import justmc.Unsafe;
import justmc.World;

public interface MessageEvent {
    default Text getMessage() {
        return Unsafe.cast(World.getValue("event_message"));
    }
}
