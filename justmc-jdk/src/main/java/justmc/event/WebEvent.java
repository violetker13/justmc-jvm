package justmc.event;

import justmc.Text;
import justmc.Unsafe;
import justmc.World;

public interface WebEvent {
    default Text getUrl() {
        return Unsafe.cast(World.getValue("url"));
    }
}
