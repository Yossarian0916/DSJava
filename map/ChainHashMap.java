package map;

import list.ArrayList;

public class ChainHashMap<K, V> extends AbstractHashMap<K, V> {
    private UnsortedTableMap<K, V>[] table;

    public ChainHashMap() {
        super();
    }

    public ChainHashMap(int cap) {
        super(cap);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void createTable() {
        this.table = (UnsortedTableMap<K, V>[]) new UnsortedTableMap[this.capacity];
    }

    @Override
    protected V bucketGet(int h, K k) {
        UnsortedTableMap<K, V> bucket = table[h];
        if (bucket == null) {
            return null;
        }
        return bucket.get(k);
    }

    @Override
    protected V bucketPut(int h, K k, V v) {
        UnsortedTableMap<K, V> bucket = table[h];
        if (bucket == null) {
            bucket = new UnsortedTableMap<>();
            table[h] = bucket;
        }
        int oldSize = bucket.size();
        V ret = bucket.put(k, v);
        this.n += (bucket.size() - oldSize);
        return ret;
    }

    @Override
    protected V bucketRemove(int h, K k) {
        UnsortedTableMap<K, V> bucket = table[h];
        if (bucket == null) {
            return null;
        }
        int oldSize = bucket.size();
        V ret = bucket.remove(k);
        this.n -= (oldSize - bucket.size());
        return ret;
    }

    @Override
    public Iterable<Entry<K, V>> entrySet() {
        ArrayList<Entry<K, V>> buffer = new ArrayList<>();
        for (int h = 0; h < capacity; h++) {
            if (table[h] != null) {
                for (Entry<K, V> entry : table[h].entrySet()) {
                    buffer.add(entry);
                }
            }
        }
        return buffer;
    }
}

class UnsortedTableMap<K, V> extends AbstractMap<K, V> {
    private ArrayList<MapEntry<K, V>> table = new ArrayList<>();

    public UnsortedTableMap() {
    }

    @Override
    public int size() {
        return table.size();
    }

    private int findIdx(K key) {
        for (int i = 0; i < size(); i++) {
            if (table.get(i).getKey().equals(key)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public V get(K key) {
        int idx = findIdx(key);
        if (idx == -1) {
            return null;
        }
        return table.get(idx).getValue();
    }

    @Override
    public V put(K key, V value) {
        int idx = findIdx(key);
        if (idx == -1) {
            table.add(new MapEntry<K, V>(key, value));
            return null;
        }
        return table.get(idx).setValue(value);
    }

    @Override
    public V remove(K key) {
        int idx = findIdx(key);
        if (idx == -1) {
            return null;
        }
        V ret = table.get(idx).getValue();
        table.remove(idx);
        return ret;
    }

    @Override
    public boolean containsKey(K key) {
        return (findIdx(key) != -1);
    }

    @Override
    public Iterable<Entry<K, V>> entrySet() {
        ArrayList<Entry<K, V>> buffer = new ArrayList<>();
        for (Entry<K, V> entry : table) {
            buffer.add(entry);
        }
        return buffer;
    }
}
