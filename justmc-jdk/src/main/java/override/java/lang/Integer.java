package override.java.lang;

import justmc.Unsafe;
import justmc.annotation.Inline;

@Inline
public final class Integer extends Number {

    public static final int MIN_VALUE = 0x80000000;

    public static final int MAX_VALUE = 0x7fffffff;

    public static final java.lang.Class<java.lang.Integer> TYPE = Unsafe.cast(Class.getPrimitiveClass("int"));

    public static native String toString(int i, int radix); // TODO

    @Inline
    public static String toHexString(int i) {
        return toString(i, 16);
    }

    @Inline
    public static String toOctalString(int i) {
        return toString(i, 8);
    }

    @Inline
    public static String toBinaryString(int i) {
        return toString(i, 2);
    }

    @Inline
    public static java.lang.String toString(int i) {
        return null; // TODO
    }

    public static native int parseInt(String s, int radix) throws NumberFormatException; // TODO

    public static int parseInt(String s) throws NumberFormatException {
        return parseInt(s, 10);
    }

    public static Integer valueOf(String s, int radix) throws NumberFormatException {
        return Integer.valueOf(parseInt(s,radix));
    }

    public static Integer valueOf(String s) throws NumberFormatException {
        return Integer.valueOf(parseInt(s, 10));
    }

    public static Integer valueOf(int i) {
        return new Integer(i);
    }

    private final int value;

    @Inline
    public Integer(int value) {
        this.value = value;
    }

    @Inline
    public byte byteValue() {
        return (byte)value;
    }

    @Inline
    public short shortValue() {
        return (short)value;
    }

    @Inline
    public int intValue() {
        return value;
    }

    @Inline
    public long longValue() {
        return value;
    }

    @Inline
    public float floatValue() {
        return (float)value;
    }

    @Inline
    public double doubleValue() {
        return value;
    }

    @Inline
    public java.lang.String toString() {
        return toString(value);
    }

    @Inline
    @Override
    public int hashCode() {
        return java.lang.Integer.hashCode(value);
    }

    @Inline
    public static int hashCode(int value) {
        return value;
    }

    @Inline
    @Override
    public boolean equals(java.lang.Object obj) {
        if (obj instanceof Integer) {
            return value == ((Integer)obj).intValue();
        }
        return false;
    }
}
