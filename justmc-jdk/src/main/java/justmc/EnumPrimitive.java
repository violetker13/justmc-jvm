package justmc;

public abstract class EnumPrimitive extends Primitive {
    protected EnumPrimitive() {}

    public static native EnumPrimitive of(String name);

    public static native EnumPrimitive of(boolean b);
}
