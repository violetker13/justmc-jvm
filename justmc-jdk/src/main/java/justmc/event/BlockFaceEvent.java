package justmc.event;

import justmc.GameValue;
import justmc.Unsafe;
import justmc.enums.BlockFace;

public interface BlockFaceEvent {
    default BlockFace getBlockFace() {
        return Unsafe.cast(GameValue.get("event_block_face"));
    }
}
