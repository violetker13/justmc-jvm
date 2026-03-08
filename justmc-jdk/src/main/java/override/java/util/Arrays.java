package override.java.util;

import justmc.Unsafe;
import justmc.Variable;

public final class Arrays {
    private Arrays() {}

    public static Object[] copyOf(Object[] arr) {
        Object[] copy = new Object[0];
        Unsafe.setVariable(Unsafe.getInstance(copy), Unsafe.getInstance(arr));
        return copy;
    }
}
