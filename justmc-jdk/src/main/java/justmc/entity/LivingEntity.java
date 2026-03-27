package justmc.entity;

import justmc.Location;
import justmc.Unsafe;

public interface LivingEntity extends Damageable {
    default Location getTargetBlock() {
        return Unsafe.cast(getValue("target_block"));
    }
}
