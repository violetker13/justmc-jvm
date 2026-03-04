package justmc;

import justmc.annotation.Inline;

@Inline
public final class MutableMap<K extends Primitive, V extends Primitive> {
    public MutableMap() {}
    public MutableMap(CopyableMap<K, V> map) {}

    public native CopyableMap<K, V> dereference();

    public native int size();

    public native V get(K key);

    public native V get(K key, V defaultValue);

    public native V getByIndex(int index, V defaultValue);

    public native void put(K key, V value);

    public native void putAll(CopyableMap<K, V> map);

    public native void remove(K key);

    @SafeVarargs
    public final native void remove(K key, V... values);

    public native CopyableList<K> getKeys();

    public native CopyableList<V> getValues();

    public native K getKeyByValue(V value, V defaultValue);

    public native K getLastKeyByValue(V value, V defaultValue);

    public native CopyableList<K> getAllKeysByValue(V value, V defaultValue);

    public native K getKeyByIndex(int index, K defaultValue);

    public native void sortByKeys();

    public native void sortByKeysByDescending();

    public native void sortByValues();

    public native void sortByValuesByDescending();

}
