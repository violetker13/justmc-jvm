package override.java.lang;

public final class Class<T> {
    public static native Class<?> forName();

    static native Class<?> getPrimitiveClass(String name);
}
