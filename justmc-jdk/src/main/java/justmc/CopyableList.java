package justmc;

import justmc.annotation.Inline;

@Inline
public final class CopyableList<E extends Primitive> implements Primitive {
    public static final int MAX_SIZE = 20000;

    private CopyableList() {}

    public static native <E extends Primitive> CopyableList<E> empty();

    @SafeVarargs
    public static native <E extends Primitive> CopyableList<E> of(E... values);

    public static CopyableList<NumberPrimitive> of(boolean... arr) {
        return of(Unsafe.<NumberPrimitive[]>cast(arr));
    }

    public static CopyableList<NumberPrimitive> of(byte... arr) {
        return of(Unsafe.<NumberPrimitive[]>cast(arr));
    }

    public static CopyableList<NumberPrimitive> of(char... arr) {
        return of(Unsafe.<NumberPrimitive[]>cast(arr));
    }

    public static CopyableList<NumberPrimitive> of(short... arr) {
        return of(Unsafe.<NumberPrimitive[]>cast(arr));
    }

    public static CopyableList<NumberPrimitive> of(int... arr) {
        return of(Unsafe.<NumberPrimitive[]>cast(arr));
    }

    public static CopyableList<NumberPrimitive> of(long... arr) {
        return of(Unsafe.<NumberPrimitive[]>cast(arr));
    }

    public static CopyableList<NumberPrimitive> of(float... arr) {
        return of(Unsafe.<NumberPrimitive[]>cast(arr));
    }

    public static CopyableList<NumberPrimitive> of(double... arr) {
        return of(Unsafe.<NumberPrimitive[]>cast(arr));
    }

    public int size() {
        var result = Variable.temp();
        Unsafe.operation("set_variable_list_get_size", CopyableMap.of(
                Pair.of("variable", result),
                Pair.of("list", this)
        ));
        return Unsafe.asInt(result);
    }

    public native E get(int index);

    public native E get(int index, E defaultValue);

    public native int indexOf(E value);

    public native int lastIndexOf(E value);

    public native CopyableList<E> add(E value);

    public native CopyableList<E> add(int index, E value);

    public native CopyableList<E> addAll(CopyableList<E> list);

    public native CopyableList<E> set(int index, E value);

    public native CopyableList<E> remove(E value);

    public native CopyableList<E> removeLast(E value);

    public native CopyableList<E> removeAll(E value);

    public native Pair<E, CopyableList<E>> removeAt(int index);

    public native CopyableList<E> shuffled();

    public native CopyableList<E> reversed();

    public native CopyableList<E> toSet();

    public native CopyableList<E> subList(int begin, int end);

    public native CopyableList<E> sorted();

    public native CopyableList<E> sortedByDescending();

    public native CopyableList<E> flatten(boolean deep);

}
