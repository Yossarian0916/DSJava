package map;

public interface SortedMap<K, V> extends Map<K, V> {
    Entry<K, V> firstEntry(); // returns the entry with smallest key value

    Entry<K, V> lastEntry(); // returns the entry with largest key value

    Entry<K, V> ceilingEntry(K key); // returns the entry with the least key value greater than or equal to key

    Entry<K, V> floorEntry(K key); // returns the entry with the greatest key value less than or equal to key

    Entry<K, V> lowerEntry(K key); // returns the entry with the greatest key value strictly less than key

    Entry<K, V> higherEntry(K key); // returns the entry with the least key value strictly greater than key

    Iterable<Entry<K, V>> subMap(K fromKey, K toKey); // returns a view of all entries with key greater than or equal to
                                                      // fromKey, but strictly less than toKey
}
