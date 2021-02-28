/**
 * A Top-down Splay Tree After a node is accessed, it (or its parent node as in
 * remove() method) is moved to be the new root. In a top-down splay tree, he
 * splay operation is performed during the initial access path. Smaller amount
 * of overhead because of no second upward traversal. No need for a parent link,
 * nor a stack to store access path. Two auxilary root nodes (leftTreeRoot and
 * rightTreeRoot) help restructuring the tree in the splay operation.
 */
package bst;

public class SplayTree<K extends Comparable<K>, V> implements SortedMap<K, V> {
    private Node<K, V> root;
    private int size;
    protected int cmpCount;
    private final Node<K, V> header; // for splay

    public SplayTree() {
        this.header = new Node<>(null, null); // for splay
    }

    private static class Node<K, V> implements TreeMap.Entry<K, V> {
        K key;
        V value;
        Node<K, V> left, right;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
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

    private void splay(K key, Node<K, V> node) {
        /**
         * @param node cannot be null, check case of empty tree before calling splay()
         */
        Node<K, V> l = header, r = header;
        Node<K, V> temp = null;
        while (true) {
            int cmp = key.compareTo(node.key);
            if (cmp < 0) {
                if (node.left == null) {
                    break;
                }
                cmpCount++;
                if (key.compareTo(node.left.key) < 0) {
                    // zig-zig, rotate right
                    temp = node.left;
                    node.left = temp.right;
                    temp.right = node;
                    node = temp;
                    if (node.left == null) {
                        break;
                    }
                }
                cmpCount++;
                // link right
                r.left = node;
                r = node;
                node = node.left;
            } else if (cmp > 0) {
                if (node.right == null) {
                    break;
                }
                cmpCount++;
                if (key.compareTo(node.right.key) > 0) {
                    // zig-zig, rotate left
                    temp = node.right;
                    node.right = temp.left;
                    temp.left = node;
                    node = temp;
                    if (node.right == null) {
                        break;
                    }
                }
                cmpCount++;
                // link left
                l.right = node;
                l = node;
                node = node.right;
            } else {
                break;
            }
            cmpCount++;
        }
        // assemble
        r.left = node.right;
        l.right = node.left;
        node.left = header.right;
        node.right = header.left;
        root = node;
    }

    public V get(K key) {
        if (root == null) {
            return null;
        }
        splay(key, root);
        if (key.compareTo(root.key) == 0) {
            return root.value;
        }
        return null;
    }

    public V put(K key, V value) {
        // empty tree
        if (root == null) {
            root = new Node<>(key, value);
            size++;
            return value;
        }
        // top-down splay, and search for key
        splay(key, root);
        int cmp = key.compareTo(root.key);
        cmpCount++;
        // update existed node
        if (cmp == 0) {
            V oldValue = root.value;
            root.value = value;
            return oldValue;
        }
        // insert new node
        Node<K, V> newNode = new Node<>(key, value);
        if (cmp < 0) {
            newNode.left = root.left;
            newNode.right = root;
            root.left = null;
        } else {
            newNode.right = root.right;
            newNode.left = root;
            root.right = null;
        }
        root = newNode;
        size++;
        return value;
    }

    public V remove(K key) {
        // empty tree
        if (root == null) {
            return null;
        }
        // top-down splay, and search for key
        splay(key, root);
        cmpCount++;
        if (key.compareTo(root.key) != 0) {
            return null;
        }
        V oldValue = root.value;
        if (root.left == null) {
            root = root.right;
        } else {
            Node<K, V> rightSubtree = root.right;
            root = root.left;
            splay(key, root); // move the maximum node (rightmost) of left subtree to the root
            root.right = rightSubtree;
        }
        cmpCount++;
        size--;
        return oldValue;
    }

    public int size() {
        return size;
    }

    private int dfs(Node<K, V> node, int depth) {
        if (node == null) {
            return depth;
        }
        int leftDepth = dfs(node.left, depth + 1);
        int rightDepth = dfs(node.right, depth + 1);
        return (leftDepth > rightDepth ? leftDepth : rightDepth);
    }

    public int getHeight() {
        /**
         * @return tree height starts at 0, when tree is empty, return -1.
         */
        return dfs(root, -1);
    }

    @Override
    public boolean containsKey(K key) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Iterable<K> keySet() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterable<V> valueSet() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterable<Entry<K, V>> entrySet() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Entry<K, V> firstEntry() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Entry<K, V> lastEntry() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Entry<K, V> ceilingEntry(K key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Entry<K, V> floorEntry(K key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Entry<K, V> lowerEntry(K key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Entry<K, V> higherEntry(K key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterable<Entry<K, V>> subMap(K fromKey, K toKey) {
        // TODO Auto-generated method stub
        return null;
    }
}
