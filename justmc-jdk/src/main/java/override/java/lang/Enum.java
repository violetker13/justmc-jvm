package override.java.lang;

public abstract class Enum<E extends java.lang.Enum<E>> {
    private final java.lang.String name;

    public final java.lang.String name() {
        return name;
    }

    private final int ordinal;

    public final int ordinal() {
        return ordinal;
    }

    protected Enum(java.lang.String name, int ordinal) {
        this.name = name;
        this.ordinal = ordinal;
    }

    public java.lang.String toString() {
        return name;
    }

    @SuppressWarnings("unchecked")
    public final java.lang.Class<E> getDeclaringClass() {
        java.lang.Class<?> clazz = getClass();
        java.lang.Class<?> zuper = clazz.getSuperclass();
        return (zuper == java.lang.Enum.class) ? (java.lang.Class<E>)clazz : (java.lang.Class<E>)zuper;
    }

    public static <T extends java.lang.Enum<T>> T valueOf(java.lang.Class<T> enumClass,
                                                          java.lang.String name) {
//        T result = enumClass.enumConstantDirectory().get(name);
//        if (result != null)
//            return result;
//        if (name == null)
//            throw new NullPointerException("Name is null");
//        throw new IllegalArgumentException(
//                "No enum constant " + enumClass.getCanonicalName() + "." + name);
        return null;
    }
}
