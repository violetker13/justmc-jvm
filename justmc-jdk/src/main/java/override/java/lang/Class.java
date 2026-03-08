package override.java.lang;

public final class Class<T> {
    private static native void registerNatives();

    static {
        registerNatives();
    }

    public static native Class<?> forName();

    static native Class<?> getPrimitiveClass(String name);
}
