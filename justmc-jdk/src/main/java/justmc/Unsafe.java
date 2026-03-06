package justmc;

import justmc.annotation.Inline;

@Inline
public final class Unsafe {
    private Unsafe() {}

    // Действия воровать отсюда: https://github.com/donzgold/JustMC_compilator/blob/master/data/actions.json
    public static void operation(String id, CopyableMap<Text, Primitive> args) {
        operation(Text.plain(id), args);
    }

    public static native void operation(Text id, CopyableMap<Text, Primitive> args);

    public static native boolean asBoolean(Primitive o);

    public static native byte asByte(Primitive o);

    public static native short asShort(Primitive o);

    public static native char asChar(Primitive o);

    public static native int asInt(Primitive o);

    public static native long asLong(Primitive o);

    public static native float asFloat(Primitive o);

    public static native double asDouble(Primitive o);

    public static native <T> T cast(Object o);

    public static native int asAddress(Object o);

    public static native Object getObject(int adr);

    public static native Variable asVariable(int adr);

    public static Variable asVariable(Object o) {
        return asVariable(asAddress(o));
    }

    public void setVariable(Variable variable, Primitive value) {
        Unsafe.operation("set_variable_value", CopyableMap.of(
                Pair.of("variable", variable),
                Pair.of("value", value)
        ));
    }

    public void removeVariable(Variable variable) {
        Unsafe.operation("set_variable_purge", CopyableMap.of(
                Pair.of("names", Text.plain(variable.getName())),
                Pair.of("scope", variable.getScope())
        ));
    }

    // TODO надо думать как реализовать
//    public static native long measureNanoTime(Runnable block);
//    public static native long measureTimeMicros(Runnable block);
//    public static native long measureTimeMillis(Runnable block);
}
