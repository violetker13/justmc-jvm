package justmc.util;

//import justmc.*;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;

//public class Primitive2ObjectMap<K extends Primitive, V> implements Map<K, V> {
//    public Primitive2ObjectMap() {
//        Unsafe.operation("set_variable_create_map", MapPrimitive.of(
//                Pair.of("variable", delegate())
//        ));
//    }
//
//    private Variable delegate() {
//        return Variable.game(Unsafe.getInstance(this).getName() + "_delegate");
//    }
//
//    private MapPrimitive<K, NumberPrimitive> map() {
//        return Unsafe.cast(delegate());
//    }
//
//    @Override
//    public int size() {
//        return map().size();
//    }
//
//    @Override
//    public boolean isEmpty() {
//        return map().isEmpty();
//    }
//
//    @Override
//    public boolean containsKey(Object key) {
//        return map().containsKey(Unsafe.cast(key));
//    }
//
//    @Override
//    public boolean containsValue(Object value) {
//        return false;
//    }
//
//    @Override
//    public V get(Object key) {
//        return Unsafe.cast(map().get(Unsafe.cast(key)));
//    }
//
//    @Nullable
//    @Override
//    public V put(K key, V value) {
//        map().put(key, Unsafe.cast(value));
//        return null;
//    }
//
//    @Override
//    public V remove(Object key) {
//        return null;
//    }
//
//    @Override
//    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
//
//    }
//
//    @Override
//    public void clear() {
//
//    }
//
//    @NotNull
//    @Override
//    public Set<K> keySet() {
//        return Set.of();
//    }
//
//    @NotNull
//    @Override
//    public Collection<V> values() {
//        return List.of();
//    }
//
//    @NotNull
//    @Override
//    public Set<Entry<K, V>> entrySet() {
//        return Set.of();
//    }
//}
