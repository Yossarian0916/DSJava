package map;

import java.util.Iterator;

public abstract class AbstractMap<K, V> implements Map<K, V> {

    protected static class MapEntry<K, V> implements Entry<K, V> {
        private K k;
        private V v;

        public MapEntry(K key, V value) {
            this.k = key;
            this.v = value;
        }

        @Override
        public K getKey() {
            return k;
        }

        @Override
        public V getValue() {
            return v;
        }

        public void setKey(K key) {
            k = key;
        }

        public V setValue(V value) {
            V old = v;
            v = value;
            return old;
        }
    }

    private class KeyIterator implements Iterator<K> {
        private final Iterator<Entry<K, V>> entries = entrySet().iterator();

        @Override
        public boolean hasNext() {
            return entries.hasNext();
        }

        @Override
        public K next() {
            return entries.next().getKey();
        }
    }

    private class keyIterable implements Iterable<K> {
        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }
    }

    @Override
    public Iterable<K> keySet() {
        return new keyIterable();
    }

    private class valueIterator implements Iterator<V> {
        private final Iterator<Entry<K, V>> entries = entrySet().iterator();

        @Override
        public boolean hasNext() {
            return entries.hasNext();
        }

        @Override
        public V next() {
            return entries.next().getValue();
        }
    }

    private class valueIterable implements Iterable<V> {

        @Override
        public Iterator<V> iterator() {
            return new valueIterator();
        }
    }

    @Override
    public Iterable<V> valueSet() {
        return new valueIterable();
    }
}
