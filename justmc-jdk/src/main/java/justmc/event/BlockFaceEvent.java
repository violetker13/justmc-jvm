package justmc.event;

import justmc.Unsafe;
import justmc.World;
import justmc.enums.BlockFace;

public interface BlockFaceEvent {
    default BlockFace getBlockFace() {
        return Unsafe.cast(World.getValue("event_block_face"));
    }
}
