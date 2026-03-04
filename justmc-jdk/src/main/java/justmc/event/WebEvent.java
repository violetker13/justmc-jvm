package justmc.event;

import justmc.GameValue;
import justmc.Unsafe;

public interface WebEvent {
    default String getUrl() {
        return Unsafe.asString(GameValue.get("url"));
    }
}
