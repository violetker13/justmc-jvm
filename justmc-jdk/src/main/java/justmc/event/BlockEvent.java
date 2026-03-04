package justmc.event;

import justmc.Block;
import justmc.GameValue;
import justmc.Location;
import justmc.Unsafe;

public interface BlockEvent {
    default Location getBlockLocation() {
        return Unsafe.asLocation(GameValue.get("event_block_location"));
    }

    default Block getBlock() {
        return Unsafe.asBlock(GameValue.get("event_block"));
    }
}
