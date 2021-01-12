/**
 * sorted map interface, skip list implementation
 */
package map;

import java.util.Random;

import list.ArrayList;

public class SkipList<K extends Comparable<K>, V> extends AbstractMap<K, V> implements SortedMap<K, V> {

    private final int MAX_LEVEL; // contain up to 2^{MAX_LEVEL} elements
    private final Node<K, V> HEADER;
    private final Node<K, V> TRAILER;
    private Random rand;
    private int levelCount;
    private int size;

    public SkipList() {
        this(16); // default, skip list contains up to 2^16 elements
    }

    public SkipList(int maxLevel) {
        this.MAX_LEVEL = maxLevel;
        this.HEADER = new Node<>(null, null, MAX_LEVEL);
        this.TRAILER = new Node<>(null, null, MAX_LEVEL);
        for (int i = 0; i < MAX_LEVEL; i++) {
            HEADER.forwards[i] = TRAILER;
        }
        this.rand = new Random(System.currentTimeMillis());
        this.levelCount = 1;
    }

    private static class Node<K, V> implements Entry<K, V> {
        K key;
        V value;
        int level;
        Node<K, V>[] forwards;

        @SuppressWarnings("unchecked")
        public Node(K key, V value, int level) {
            this.key = key;
            this.value = value;
            this.level = level;
            this.forwards = (Node<K, V>[]) new Node[level];
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }
    }

    private Node<K, V> search(K key) {
        /**
         * @return the entry with the least key value greater than or equal to key
         */
        Node<K, V> node = HEADER;
        for (int i = levelCount; i >= 0; i--) {
            while ((node.forwards[i] != TRAILER) && (node.forwards[i].key.compareTo(key) < 0)) {
                node = node.forwards[i];
            }
        }
        node = node.forwards[0];
        return node;
    }

    private int getRandomLevel() {
        int level = 1;
        while (rand.nextBoolean() && (level < MAX_LEVEL)) {
            level++;
        }
        return level;
    }

    private V insert(K key, V value) {
        @SuppressWarnings("unchecked")
        Node<K, V>[] update = (Node<K, V>[]) new Node[MAX_LEVEL];
        Node<K, V> node = HEADER;
        for (int i = levelCount; i >= 0; i--) {
            while ((node.forwards[i] != TRAILER) && (node.forwards[i].key.compareTo(key) < 0)) {
                node = node.forwards[i];
            }
            update[i] = node;
        }
        // update existed node
        node = node.forwards[0];
        if ((node != TRAILER) && (node.key.compareTo(key) == 0)) {
            V oldValue = node.value;
            node.value = value;
            return oldValue;
        }
        // insert new node
        int level = getRandomLevel();
        if (level > levelCount) {
            for (int i = levelCount + 1; i < MAX_LEVEL; i++) {
                update[i] = HEADER;
            }
            // each time only increment max skip-list level by 1;
            levelCount++;
            level = levelCount;
        }
        Node<K, V> newNode = new Node<>(key, value, level);
        for (int i = 0; i < newNode.level; i++) {
            newNode.forwards[i] = update[i].forwards[i];
            update[i].forwards[i] = newNode;
        }
        size++;
        return value;
    }

    private V delete(K key) {
        @SuppressWarnings("unchecked")
        Node<K, V>[] update = (Node<K, V>[]) new Node[levelCount + 1];
        Node<K, V> node = HEADER;
        for (int i = levelCount; i >= 0; i--) {
            while ((node.forwards[i] != TRAILER) && (node.forwards[i].key.compareTo(key) < 0)) {
                node = node.forwards[i];
            }
            // prev node just before to-be-removed node
            update[i] = node;
        }
        node = node.forwards[0];
        // invalid key, such node does not exist in skip-list
        if (node.key.compareTo(key) != 0) {
            return null;
        }

        for (int i = 0; i < node.level; i++) {
            update[i].forwards[i] = node.forwards[i];
        }
        size--;
        return node.value;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = levelCount; i >= 0; i--) {
            Node<K, V> node = HEADER;
            sb.append("SkipList[");
            while (node != TRAILER) {
                if (node != HEADER) {
                    sb.append(node.key + " ");
                }
                node = node.forwards[i];
            }
            sb.append("]");
        }
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public V get(K key) {
        Node<K, V> node = search(key);
        if (key.compareTo(node.key) != 0) {
            return null;
        }
        return node.value;
    }

    @Override
    public V put(K key, V value) {
        return insert(key, value);
    }

    @Override
    public V remove(K key) {
        return delete(key);
    }

    @Override
    public boolean containsKey(K key) {
        Node<K, V> node = search(key);
        if (node.key != key) {
            return false;
        }
        return true;
    }

    @Override
    public Iterable<Entry<K, V>> entrySet() {
        ArrayList<Entry<K, V>> snapshot = new ArrayList<>(size);
        Node<K, V> node = HEADER;
        while (node != TRAILER) {
            if (node != HEADER) {
                snapshot.add(node);
            }
            node = node.forwards[0];
        }
        return snapshot;
    }

    @Override
    public Entry<K, V> firstEntry() {
        Node<K, V> node = HEADER;
        if (node.forwards[0] == TRAILER) {
            return null;
        }
        return node.forwards[0];
    }

    @Override
    public Entry<K, V> lastEntry() {
        Node<K, V> node = HEADER;
        for (int i = levelCount; i >= 0; i--) {
            while (node.forwards[i] != TRAILER) {
                node = node.forwards[i];
            }
        }
        return node;
    }

    @Override
    public Entry<K, V> floorEntry(K key) {
        /**
         * @return the entry with the greatest key value less than or equal to key
         */
        Node<K, V> node = HEADER;
        for (int i = levelCount; i >= 0; i--) {
            while ((node.forwards[i] != TRAILER) && (node.forwards[i].key.compareTo(key) <= 0)) {
                node = node.forwards[i];
            }
        }
        return node;
    }

    @Override
    public Entry<K, V> ceilingEntry(K key) {
        /**
         * @return the entry with the least key value greater than or equal to key
         */
        Node<K, V> node = HEADER;
        for (int i = levelCount; i >= 0; i--) {
            while ((node.forwards[i] != TRAILER) && (node.forwards[i].key.compareTo(key) < 0)) {
                node = node.forwards[i];
            }
        }
        return node.forwards[0];
    }

    @Override
    public Entry<K, V> lowerEntry(K key) {
        /**
         * @return the entry with the greatest key value strictly less than key
         */
        Node<K, V> node = HEADER;
        for (int i = levelCount; i >= 0; i--) {
            while ((node.forwards[i] != TRAILER) && (node.forwards[i].key.compareTo(key) < 0)) {
                node = node.forwards[i];
            }
        }
        return node;
    }

    @Override
    public Entry<K, V> higherEntry(K key) {
        /**
         * @return the entry with the least key value strictly greater than key
         */
        Node<K, V> node = HEADER;
        for (int i = levelCount; i >= 0; i--) {
            while ((node.forwards[i] != TRAILER) && (node.forwards[i].key.compareTo(key) <= 0)) {
                node = node.forwards[i];
            }
        }
        return node.forwards[0];
    }

    @Override
    public Iterable<Entry<K, V>> subMap(K fromKey, K toKey) {
        /**
         * @return a view of all entries with key greater than or equal to fromKey, but
         *         strictly less than toKey
         */
        // check invalid arguments
        if (fromKey.compareTo(toKey) >= 0) {
            return null;
        }

        ArrayList<Entry<K, V>> view = new ArrayList<>();
        Node<K, V> node = HEADER;
        for (int i = levelCount; i >= 0; i--) {
            while ((node.forwards[i] != TRAILER) && (node.forwards[i].key.compareTo(fromKey) < 0)) {
                node = node.forwards[i];
            }
        }
        node = node.forwards[0];
        while (node.key.compareTo(toKey) < 0) {
            view.add(node);
            node = node.forwards[0];
        }
        return view;
    }
}
