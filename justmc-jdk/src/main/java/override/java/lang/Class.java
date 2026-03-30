package override.java.lang;

import justmc.ListPrimitive;
import justmc.NumberPrimitive;

public final class Class<T> {
    private static native void registerNatives();

    static {
        registerNatives();
    }

    public static native java.lang.Class<?> forName();

    static native java.lang.Class<?> getPrimitiveClass(java.lang.String name);

    public native ListPrimitive<NumberPrimitive> getObjectFields();
}
