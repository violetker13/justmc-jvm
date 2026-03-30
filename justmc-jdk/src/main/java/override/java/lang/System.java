package override.java.lang;

import justmc.*;
import justmc.annotation.Inline;
import justmc.Memory;

@Inline
public final class System {
    private System() {}

    public static long currentTimeMillis() {
        return Unsafe.asLong(World.getValue("timestamp"));
    }

    public static java.lang.String getenv(java.lang.String name) {
        return Unsafe.cast(new String(Unsafe.<Text>cast(Variable.game(Text.plain(name)))));
    }

    public static int identityHashCode(Object o) {
        return Unsafe.asAddress(o);
    }

    public static void gc() {
        Memory.gc();
    }
}
