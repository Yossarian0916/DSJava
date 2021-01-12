package bst;

public interface SortedMap<K, V> extends TreeMap<K, V> {
    TreeMap.Entry<K, V> firstEntry(); // returns the entry with smallest key value

    TreeMap.Entry<K, V> lastEntry(); // returns the entry with largest key value

    TreeMap.Entry<K, V> ceilingEntry(K key); // returns the entry with the least key value greater than or equal to key

    TreeMap.Entry<K, V> floorEntry(K key); // returns the entry with the greatest key value less than or equal to key

    TreeMap.Entry<K, V> lowerEntry(K key); // returns the entry with the greatest key value strictly less than key

    TreeMap.Entry<K, V> higherEntry(K key); // returns the entry with the least key value strictly greater than key

    Iterable<TreeMap.Entry<K, V>> subMap(K fromKey, K toKey); // returns a view of all entries with key greater than or
                                                              // equal to
    // fromKey, but strictly less than toKey
}
