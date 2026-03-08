package justmc.event;

import justmc.MapPrimitive;
import justmc.Unsafe;

public interface CancellableEvent {
    default void cancel() {
        Unsafe.operation("game_cancel_event", MapPrimitive.empty());
    }

    default void uncancel() {
        Unsafe.operation("game_uncancel_event", MapPrimitive.empty());
    }
}
