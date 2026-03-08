package justmc;

import justmc.annotation.Inline;

/**
 * Примитив списка (предмет)
 * @param <E> Тип элементов
 */
@Inline
public final class ListPrimitive<E extends Primitive> extends Primitive {
    public static final int MAX_SIZE = 20000;

    private ListPrimitive() {}

    public static native <E extends Primitive> ListPrimitive<E> empty();

    @SafeVarargs
    public static native <E extends Primitive> ListPrimitive<E> of(E... values);

    public static ListPrimitive<NumberPrimitive> of(boolean... arr) {
        return of(Unsafe.<NumberPrimitive[]>cast(arr));
    }

    public static ListPrimitive<NumberPrimitive> of(byte... arr) {
        return of(Unsafe.<NumberPrimitive[]>cast(arr));
    }

    public static ListPrimitive<NumberPrimitive> of(char... arr) {
        return of(Unsafe.<NumberPrimitive[]>cast(arr));
    }

    public static ListPrimitive<NumberPrimitive> of(short... arr) {
        return of(Unsafe.<NumberPrimitive[]>cast(arr));
    }

    public static ListPrimitive<NumberPrimitive> of(int... arr) {
        return of(Unsafe.<NumberPrimitive[]>cast(arr));
    }

    public static ListPrimitive<NumberPrimitive> of(long... arr) {
        return of(Unsafe.<NumberPrimitive[]>cast(arr));
    }

    public static ListPrimitive<NumberPrimitive> of(float... arr) {
        return of(Unsafe.<NumberPrimitive[]>cast(arr));
    }

    public static ListPrimitive<NumberPrimitive> of(double... arr) {
        return of(Unsafe.<NumberPrimitive[]>cast(arr));
    }

    public native int size();

    public native E get(int index);

    public native E get(int index, E defaultValue);

    public native boolean isEmpty();

    public native boolean contains(E value);

    public native int indexOf(E value);

    public native int lastIndexOf(E value);
}
