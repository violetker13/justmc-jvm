package justmc.event;

import justmc.Block;
import justmc.Location;
import justmc.Unsafe;
import justmc.World;

public interface BlockEvent {
    default Location getBlockLocation() {
        return Unsafe.cast(World.getValue("event_block_location"));
    }

    default Block getBlock() {
        return Unsafe.cast(World.getValue("event_block"));
    }
}
