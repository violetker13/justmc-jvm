package justmc;

import justmc.annotation.Inline;

import java.util.function.Consumer;

@Inline
public final class ListPrimitive<E extends Primitive> extends Primitive {
    public static final int MAX_SIZE = 20000;

    private ListPrimitive() {}

    public static native <E extends Primitive> ListPrimitive<E> empty();

    public static native <E extends Primitive> ListPrimitive<E> ofNulls(int size);

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

    public native ListPrimitive<E> set(int index, E value);

    public native ListPrimitive<E> add(E value);

    public native ListPrimitive<E> addAll(ListPrimitive<E> value);

    public native boolean isEmpty();

    public native boolean contains(E value);

    public native int indexOf(E value);

    public native int lastIndexOf(E value);

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

    public void forEach(Consumer<E> block) {
        var value = Variable.temp();
        Unsafe.operation("repeat_for_each_in_list", MapPrimitive.of(
                Pair.of("value_variable", value),
                Pair.of("list", this)
        ), () -> {
            block.accept(Unsafe.cast(value));
        });
    }
}
