package map;

import list.ArrayList;

public class LinearProbeHashMap<K, V> extends AbstractHashMap<K, V> {
    private MapEntry<K, V>[] table;
    private final MapEntry<K, V> DEFUNCT = new MapEntry<>(null, null); // sentinel, convenient for deletions

    public LinearProbeHashMap() {
        super();
    }

    public LinearProbeHashMap(int cap) {
        super(cap);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void createTable() {
        this.table = (MapEntry<K, V>[]) new MapEntry[this.capacity];
    }

    private boolean isAvailable(int i) {
        return (table[i] == null || table[i] == DEFUNCT);
    }

    private int findSlot(int h, K k) {
        /**
         * if @return < 0, then no match find, the slot can be used to add new entry
         * if @return >= 0, successful match
         * 
         * if key is present in table[j], findSlot return j, or return -(j + 1) such
         * that k can be added at table[j]
         */
        int j = h;
        int ret = 0;
        do {
            // either empty (null) or defunct
            if (isAvailable(j)) {
                if (ret == 0) {
                    ret = -(j + 1);
                }
                // table[j] is empty, return immediately
                if (table[j] == null) {
                    return ret;
                }
            }
            // find a match
            else if (table[j].getKey().equals(k)) {
                return j;
            }
            j = (j + 1) % capacity;
        } while (j != h);
        return ret; // if here ret = 0, something wrong happens
    }

    @Override
    protected V bucketGet(int h, K k) {
        int i = findSlot(h, k);
        if (i < 0) {
            return null;
        }
        return table[i].getValue();
    }

    @Override
    protected V bucketPut(int h, K k, V v) {
        int i = findSlot(h, k);
        if (i >= 0) {
            return table[i].setValue(v);
        }
        table[-(i + 1)] = new MapEntry<K, V>(k, v);
        n++;
        return null;
    }

    @Override
    protected V bucketRemove(int h, K k) {
        int i = findSlot(h, k);
        if (i < 0) {
            return null;
        }
        V oldValue = table[i].getValue();
        table[i] = DEFUNCT;
        n--;
        return oldValue;
    }

    @Override
    public Iterable<Entry<K, V>> entrySet() {
        ArrayList<Entry<K, V>> buffer = new ArrayList<>();
        for (int h = 0; h < size(); h++) {
            if (!isAvailable(h)) {
                buffer.add(table[h]);
            }
        }
        return buffer;
    }
}
