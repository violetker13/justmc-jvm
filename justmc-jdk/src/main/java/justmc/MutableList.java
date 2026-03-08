package justmc;

import justmc.annotation.Inline;

@Inline
public final class MutableList<E extends Primitive> {
    public MutableList() {
        Unsafe.operation("set_variable_create_list", MapPrimitive.of(
                Pair.of("variable", Unsafe.getInstance(this))
        ));
    }

    public MutableList(ListPrimitive<E> list) {
        Unsafe.operation("set_variable_create_list", MapPrimitive.of(
                Pair.of("variable", Unsafe.getInstance(this)),
                Pair.of("values", list)
        ));
    }

    public int size() {
        var result = Variable.temp();
        Unsafe.operation("set_variable_list_get_size", MapPrimitive.of(
                Pair.of("variable", result),
                Pair.of("list", Unsafe.getInstance(this))
        ));
        return Unsafe.asInt(result);
    }

    public native E get(int index);

    public native E get(int index, E defaultValue);

    public native int indexOf(E value);

    public native int lastIndexOf(E value);

    public native void add(E value);

    public native void add(int index, E value);

    public native void addAll(ListPrimitive<E> list);

    public native void set(int index, E value);

    public native void remove(E value);

    public native void removeLast(E value);

    public native void removeAll(E value);

    public native E removeAt(int index);

    public native void shuffle();

    public native void reverse();

    public native void distinct();

    public native ListPrimitive<E> subList(int begin, int end);

    public native void sort();

    public native void sortByDescending();

    public native void flatMap(boolean deep);

}
