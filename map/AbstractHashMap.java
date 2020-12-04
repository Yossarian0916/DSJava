package map;

import java.util.ArrayList;
import java.util.Random;

public abstract class AbstractHashMap<K, V> extends AbstractMap<K, V> {
    protected int n = 0;
    protected int capacity;
    private int prime; // prime number
    private long scale, shift; // factors for hash function

    public AbstractHashMap(int cap, int p) {
        prime = p;
        capacity = cap;
        Random rand = new Random();
        scale = rand.nextInt(prime - 1) + 1; // from set {1, 2, 3, ..., p-1}
        shift = rand.nextInt(prime); // from set {0, 1, 2, 3, ..., p}
        createTable();
    }

    public AbstractHashMap(int cap) {
        this(cap, 109345121);
    }

    public AbstractHashMap() {
        this(17);
    }

    @Override
    public int size() {
        return n;
    }

    @Override
    public boolean containsKey(K key) {
        return (bucketGet(hashValue(key), key) != null);
    }

    @Override
    public V get(K key) {
        return bucketGet(hashValue(key), key);
    }

    @Override
    public V put(K key, V value) {
        V retValue = bucketPut(hashValue(key), key, value);
        if (n > capacity / 2) { // load factor <= 0.5
            resize(2 * capacity - 1);
        }
        return retValue;
    }

    @Override
    public V remove(K key) {
        return bucketRemove(hashValue(key), key);
    }

    // utility hash function
    private int hashValue(K key) {
        return (int) ((Math.abs(key.hashCode() * scale + shift) % prime) % capacity);
    }

    private void resize(int newCap) {
        ArrayList<Entry<K, V>> buffer = new ArrayList<>(n);
        for (Entry<K, V> e : entrySet()) {
            buffer.add(e);
        }
        this.capacity = newCap;
        createTable();
        this.n = 0; // re-increment while inserting
        for (Entry<K, V> e : buffer) {
            put(e.getKey(), e.getValue());
        }
    }

    protected abstract void createTable();

    protected abstract V bucketGet(int h, K k);

    protected abstract V bucketPut(int h, K k, V v);

    protected abstract V bucketRemove(int h, K k);
}
