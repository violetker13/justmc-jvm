package justmc.event;

import justmc.CopyableMap;
import justmc.Unsafe;

public interface CancellableEvent {
    default void cancel() {
        Unsafe.operation("game_cancel_event", CopyableMap.empty());
    }

    default void uncancel() {
        Unsafe.operation("game_uncancel_event", CopyableMap.empty());
    }
}
