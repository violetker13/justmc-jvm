package justmc.event;

import justmc.GameValue;
import justmc.Text;
import justmc.Unsafe;

public interface WebEvent {
    default Text getUrl() {
        return Unsafe.cast(GameValue.get("url"));
    }
}
