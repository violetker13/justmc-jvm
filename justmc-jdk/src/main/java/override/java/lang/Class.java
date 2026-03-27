package override.java.lang;

public final class Class<T> {
    private static native void registerNatives();

    static {
        registerNatives();
    }

    public static native java.lang.Class<?> forName();

    static native java.lang.Class<?> getPrimitiveClass(java.lang.String name);
}
