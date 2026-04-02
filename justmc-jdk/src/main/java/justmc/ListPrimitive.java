package justmc;

import justmc.annotation.Inline;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

@Inline
public final class ListPrimitive<E extends Primitive> extends Primitive implements Iterable<E> {
    public static final int MAX_SIZE = 20000;

    private ListPrimitive() {}

    @NotNull
    public static <E extends Primitive> ListPrimitive<E> empty() {
        return ofNulls(0);
    }

    @NotNull
    public static native <E extends Primitive> ListPrimitive<E> ofNulls(int size);

    @NotNull
    @SafeVarargs
    public static native <E extends Primitive> ListPrimitive<E> of(E... values);

    @NotNull
    public static ListPrimitive<NumberPrimitive> of(boolean... arr) {
        return of(Unsafe.<NumberPrimitive[]>cast(arr));
    }

    @NotNull
    public static ListPrimitive<NumberPrimitive> of(byte... arr) {
        return of(Unsafe.<NumberPrimitive[]>cast(arr));
    }

    @NotNull
    public static ListPrimitive<NumberPrimitive> of(char... arr) {
        return of(Unsafe.<NumberPrimitive[]>cast(arr));
    }

    @NotNull
    public static ListPrimitive<NumberPrimitive> of(short... arr) {
        return of(Unsafe.<NumberPrimitive[]>cast(arr));
    }

    @NotNull
    public static ListPrimitive<NumberPrimitive> of(int... arr) {
        return of(Unsafe.<NumberPrimitive[]>cast(arr));
    }

    @NotNull
    public static ListPrimitive<NumberPrimitive> of(long... arr) {
        return of(Unsafe.<NumberPrimitive[]>cast(arr));
    }

    @NotNull
    public static ListPrimitive<NumberPrimitive> of(float... arr) {
        return of(Unsafe.<NumberPrimitive[]>cast(arr));
    }

    @NotNull
    public static ListPrimitive<NumberPrimitive> of(double... arr) {
        return of(Unsafe.<NumberPrimitive[]>cast(arr));
    }

    public native int size();

    public native E get(int index);

    public native E get(int index, E defaultValue);

    @NotNull
    public native ListPrimitive<E> set(int index, E value);

    @NotNull
    public native ListPrimitive<E> add(E value);

    @NotNull
    public native ListPrimitive<E> addAll(ListPrimitive<E> value);

    public native boolean isEmpty();

    public native boolean contains(E value);

    public native int indexOf(E value);

    public native int lastIndexOf(E value);

    @NotNull
    public ListPrimitive<E> subList(int start, int end) {
        var result = Variable.result();
        Unsafe.operation("set_variable_trim_list", MapPrimitive.of(
                Pair.of("variable", result),
                Pair.of("list", this),
                Pair.of("start", NumberPrimitive.of(start)),
                Pair.of("end", NumberPrimitive.of(end))
        ));
        return Unsafe.cast(result);
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return Unsafe.iterator("repeat_for_each_in_list", MapPrimitive.of(
                Pair.of("value_variable", Variable.temp()),
                Pair.of("list", this)
        ));
    }
}
