package override.java.lang;

import justmc.*;
import justmc.annotation.Inline;

@Inline
public final class System {
    private System() {}

    public static long currentTimeMillis() {
        return Unsafe.asLong(GameValue.get("timestamp"));
    }

    public static java.lang.String getenv(java.lang.String name) {
        return Unsafe.cast(Variable.game(name));
    }

    public static int identityHashCode(Object o) {
        return Unsafe.asAddress(o);
    }
}
