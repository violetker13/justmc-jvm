package justmc.event;

import justmc.Item;
import justmc.Unsafe;
import justmc.World;

public interface ItemEvent {
    default Item getItem() {
        return Unsafe.cast(World.getValue("event_item"));
    }
}
