package justmc.event;

import justmc.Entity;

public interface EntityEvent {
    default Entity getEntity() {
        return Entity.DEFAULT;
    }
}
