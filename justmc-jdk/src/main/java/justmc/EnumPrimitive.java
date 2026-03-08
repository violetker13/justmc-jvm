package justmc;

public final class EnumPrimitive extends Primitive {
    private EnumPrimitive() {super();}

    public static native EnumPrimitive of(String name);

    public static native EnumPrimitive of(boolean b);

    public static native EnumPrimitive of(Enum<?> o);
}
