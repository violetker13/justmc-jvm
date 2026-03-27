package justmc.event;

import justmc.Selection;
import justmc.entity.Entity;

public interface EntityEvent {
    default Entity getEntity() {
        return (Entity) Selection.getDefault();
    }
}
