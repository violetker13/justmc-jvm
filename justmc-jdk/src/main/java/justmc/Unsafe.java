package justmc;

import justmc.annotation.Inline;

import java.util.Iterator;

@Inline
public final class Unsafe {
    private Unsafe() {}

    // Действия воровать отсюда: https://github.com/donzgold/JustMC_compilator/blob/master/data/actions.json
    public static native void operation(String id, MapPrimitive<Text, Primitive> args);

    public static native void operation(String id, MapPrimitive<Text, Primitive> args, Runnable block);

    public static native void operation(String id, Conditional conditional);

    public static native void operation(String id, Conditional conditional, Runnable block);

    public static native <E extends Primitive> Iterator<E> iterator(String id, MapPrimitive<Text, Primitive> args);

    /**
     * Принимает значение как boolean
     * @param o Значение
     * @return То же самое значение, но с типом boolean
     */
    public static native boolean asBoolean(Primitive o);

    /**
     * Принимает значение как byte
     * @param o Значение
     * @return То же самое значение, но с типом byte
     */
    public static native byte asByte(Primitive o);

    /**
     * Принимает значение как short
     * @param o Значение
     * @return То же самое значение, но с типом short
     */
    public static native short asShort(Primitive o);

    /**
     * Принимает значение как char
     * @param o Значение
     * @return То же самое значение, но с типом char
     */
    public static native char asChar(Primitive o);

    /**
     * Принимает значение как int
     * @param o Значение
     * @return То же самое значение, но с типом int
     */
    public static native int asInt(Primitive o);

    /**
     * Принимает long как int
     * @param l Значение
     * @return То же самое значение, но с типом int
     */
    public static native int asInt(long l);

    /**
     * Принимает значение как long
     * @param o Значение
     * @return То же самое значение, но с типом long
     */
    public static native long asLong(Primitive o);

    /**
     * Принимает int как long
     * @param i Значение
     * @return То же самое значение, но с типом long
     */
    public static native long asLong(int i);

    /**
     * Принимает значение как float
     * @param o Значение
     * @return То же самое значение, но с типом float
     */
    public static native float asFloat(Primitive o);

    /**
     * Принимает значение как double
     * @param o Значение
     * @return То же самое значение, но с типом double
     */
    public static native double asDouble(Primitive o);

    /**
     * Принимает значение как {@code T}
     * @param o Значение
     * @return То же самое значение, но с типом {@code T}
     * @param <T> Новый тип
     */
    public static native <T> T cast(Object o);

    /**
     * Принимает объект как адрес
     * @param o Объект
     * @return То же самое значение, но с типом int
     */
    public static native int asAddress(Object o);

    /**
     * Принимает адрес как объект
     * @param adr Адрес объекта
     * @return То же самое значение, но с типом Object
     */
    public static native Object asObject(int adr);
}
