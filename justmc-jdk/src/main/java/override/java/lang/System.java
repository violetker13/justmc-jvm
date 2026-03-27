package override.java.lang;

import justmc.Text;
import justmc.Unsafe;
import justmc.Variable;
import justmc.World;
import justmc.annotation.Inline;

@Inline
public final class System {
    private System() {}

    public static long currentTimeMillis() {
        return Unsafe.asLong(World.getValue("timestamp"));
    }

    public static java.lang.String getenv(java.lang.String name) {
        return Unsafe.cast(new String(Unsafe.<Text>cast(Variable.game(name))));
    }

    public static int identityHashCode(Object o) {
        return Unsafe.asAddress(o);
    }
}
