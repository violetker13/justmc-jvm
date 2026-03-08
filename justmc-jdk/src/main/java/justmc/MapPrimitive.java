package justmc;

import justmc.annotation.Inline;

@Inline
public final class MapPrimitive<K extends Primitive, V extends Primitive> extends Primitive {
    public static final int MAX_SIZE = 10000;

    private MapPrimitive() {}

    public static native <K extends Primitive, V extends Primitive> MapPrimitive<K, V> empty();

    public static native <K extends Primitive, V extends Primitive> MapPrimitive<K, V> of(ListPrimitive<K> keys, ListPrimitive<V> values);

    @SafeVarargs
    public static native <K extends Primitive, V extends Primitive> MapPrimitive<K, V> of(Pair<K, V>... args);

    public native int size();

    public native V get(K key);

    public native V get(K key, V defaultValue);

    public native V getByIndex(int index, V defaultValue);

    public native MapPrimitive<K, V> put(K key, V value);

    public native MapPrimitive<K, V> putAll(MapPrimitive<K, V> map);

    public native MapPrimitive<K, V> remove(K key);

    @SafeVarargs
    public final native MapPrimitive<K, V> remove(K key, V... values);

    public native ListPrimitive<K> getKeys();

    public native ListPrimitive<V> getValues();

    public native K getKeyByValue(V value, V defaultValue);

    public native K getLastKeyByValue(V value, V defaultValue);

    public native ListPrimitive<K> getAllKeysByValue(V value, V defaultValue);

    public native K getKeyByIndex(int index, K defaultValue);

    public native MapPrimitive<K, V> sortedByKeys();

    public native MapPrimitive<K, V> sortedByKeysByDescending();

    public native MapPrimitive<K, V> sortedByValues();

    public native MapPrimitive<K, V> sortedByValuesByDescending();

}
