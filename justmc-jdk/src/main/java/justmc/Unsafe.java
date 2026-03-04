package justmc;

import justmc.annotation.Inline;

@Inline
public final class Unsafe {
    private Unsafe() {}

    // Действия воровать отсюда: https://github.com/donzgold/JustMC_compilator/blob/master/data/actions.json
    public static native void operation(String id, CopyableMap<Text, Primitive> args);

    public static native boolean asBoolean(Primitive o);
    public static native byte asByte(Primitive o);
    public static native short asShort(Primitive o);
    public static native char asChar(Primitive o);
    public static native int asInt(Primitive o);
    public static native long asLong(Primitive o);
    public static native float asFloat(Primitive o);
    public static native double asDouble(Primitive o);
    public static native String asString(Primitive o);
    public static native Text asText(Primitive o);
    public static native Vector asVector(Primitive o);
    public static native Location asLocation(Primitive o);
    public static native Block asBlock(Primitive o);
    public static native Item asItem(Primitive o);
    public static native Primitive asPrimitive(Object o);
    public static native <E extends Primitive> CopyableList<E> asCopyableList(Primitive o);
    public static native <K extends Primitive, V extends Primitive> CopyableMap<K, V> asCopyableMap(Primitive o);
    public static native <T extends Primitive> T typed(Primitive o);
    public static native <E extends Enum<E>> E asEnum(Primitive o);
    public static native <E extends Enum<E>> Text asText(E e);

    public static int addressOf(Object o) {
        return asInt(asPrimitive(o));
    }

    public static Variable pointerTo(Object o) {
        return Variable.game(asString((asPrimitive(o))));
    }

    public static Variable pointerTo(int adr) {
        return Variable.game(asString(NumberPrimitive.of(adr)));
    }

    // TODO надо думать как реализовать
//    public static native long measureNanoTime(Runnable block);
//    public static native long measureTimeMicros(Runnable block);
//    public static native long measureTimeMillis(Runnable block);
}
