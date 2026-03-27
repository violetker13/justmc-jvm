package override.java.util;

import justmc.Unsafe;

public final class Arrays {
    private Arrays() {}

    public static Object[] copyOf(Object[] arr) {
        Object[] copy = new Object[0];
        Unsafe.getInstance(copy).setValue(Unsafe.getInstance(arr));
        return copy;
    }
}
