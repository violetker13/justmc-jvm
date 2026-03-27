package justmc;

import justmc.annotation.Inline;

@Inline
public final class Unsafe {
    private Unsafe() {}

    // Действия воровать отсюда: https://github.com/donzgold/JustMC_compilator/blob/master/data/actions.json
    public static native void operation(String id, MapPrimitive<Text, Primitive> args);

    public static native void operation(String id, MapPrimitive<Text, Primitive> args, Runnable block);

    public static native void operation(String id, Conditional conditional);

    public static native void operation(String id, Conditional conditional, Runnable block);

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

    public static native Object asObject(int adr);

    public static native Variable getInstance(int adr);

    public static Variable getInstance(Object o) {
        return getInstance(asAddress(o));
    }
}
