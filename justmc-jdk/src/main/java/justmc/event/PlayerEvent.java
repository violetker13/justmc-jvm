package justmc.event;

import justmc.Selection;
import justmc.entity.Player;

public interface PlayerEvent {
    default Player getPlayer() {
        return (Player) Selection.getDefault();
    }
}