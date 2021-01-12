package bst;

public interface TreeMap<K, V> {

    int size();

    default boolean isEmpty() {
        return (size() == 0);
    }

    V get(K key);

    V put(K key, V value);

    V remove(K key);

    boolean containsKey(K key);

    Iterable<K> keySet();

    Iterable<V> valueSet();

    Iterable<Entry<K, V>> entrySet();

    public static interface Entry<K, V> {
        K getKey();

        V getValue();
    }
}