package justmc;

import justmc.annotation.Inline;

@Inline
public final class NumberPrimitive extends Primitive {
    private NumberPrimitive() {}

    public static native NumberPrimitive of(boolean x);
    public static native NumberPrimitive of(byte x);
    public static native NumberPrimitive of(char x);
    public static native NumberPrimitive of(short x);
    public static native NumberPrimitive of(int x);
    public static native NumberPrimitive of(long x);
    public static native NumberPrimitive of(float x);
    public static native NumberPrimitive of(double x);
}
