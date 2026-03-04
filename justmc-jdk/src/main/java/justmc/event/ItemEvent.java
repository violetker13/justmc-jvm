package justmc.event;

import justmc.GameValue;
import justmc.Item;
import justmc.Unsafe;

public interface ItemEvent {
    default Item getItem() {
        return Unsafe.asItem(GameValue.get("event_item"));
    }
}
