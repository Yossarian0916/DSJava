package bst;

public class AvlTree<K extends Comparable<K>, V> implements SortedMap<K, V> {
    private Node<K, V> root;
    private int size;

    public AvlTree() {
    }

    private static class Node<K, V> implements TreeMap.Entry<K, V> {
        K key;
        V value;
        Node<K, V> parent;
        Node<K, V> left, right;
        int height;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.height = 1;
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

    /** AVL tree operations */
    private Node<K, V> search(Node<K, V> node, K key) {
        while ((node != null) && (node.key != key)) {
            if (key.compareTo(node.key) > 0) {
                node = node.right;
            } else {
                node = node.left;
            }
        }
        return node;
    }

    public V get(K key) {
        Node<K, V> node = search(root, key);
        if (node == null) {
            return null;
        }
        return node.value;
    }

    private void rotateLeft(Node<K, V> node) {
        /**
         * turn node to be node.right's new left node
         */
        Node<K, V> y = node.right;
        node.right = y.left; // turn y.left subtree to node.right subtree
        if (y.left != null) {
            y.left.parent = node;
        }
        // link node.parent to y
        y.parent = node.parent;
        if (node.parent == null) {
            root = y;
        } else if (node == node.parent.left) {
            node.parent.left = y;
        } else {
            node.parent.right = y;
        }
        y.left = node; // put node on y.left
        node.parent = y;
        // update height
        node.height = max(height(node.left), height(node.right)) + 1;
        y.height = max(height(node.left), height(node.right)) + 1;
    }

    private void rotateRight(Node<K, V> node) {
        /**
         * turn node to be node.right's new right node
         */
        Node<K, V> x = node.left;
        node.left = x.right; // turn x's right subtree to node's left subtree
        if (x.right != null) {
            x.right.parent = node;
        }
        // link node.parent to x
        x.parent = node.parent;
        if (node.parent == null) {
            root = x;
        } else if (node == node.parent.left) {
            node.parent.left = x;
        } else {
            node.parent.right = x;
        }
        x.right = node; // put node on x.right
        node.parent = x;
        // update height
        node.height = max(height(node.left), height(node.right)) + 1;
        x.height = max(height(node.left), height(node.right)) + 1;
    }

    private void rebalance(Node<K, V> node) {
        /**
         * search unbalance from fixpoint upwards towards root
         */
        int heightDiff = height(node.left) - height(node.right);
        while (true) {
            if (heightDiff == 2) {
                if (height(node.left.left) > height(node.left.right)) {
                    // single right rotate
                    rotateRight(node);
                } else {
                    // left rotate -> right rotate
                    rotateLeft(node.left);
                    rotateRight(node);
                }
            } else if (heightDiff == -2) {
                if (height(node.right.right) > height(node.right.left)) {
                    // single left rotate
                    rotateLeft(node);
                } else {
                    // right rotate -> left rotate
                    rotateRight(node.right);
                    rotateLeft(node);
                }
            }
            // update node height
            node.height = max(height(node.left), height(node.right)) + 1;
            node = node.parent; // upwards
            if (node == null) {
                break;
            }
            heightDiff = height(node.left) - height(node.right);
        }
    }

    public V put(K key, V value) {
        // empty tree
        if (root == null) {
            root = new Node<>(key, value);
            return value;
        }
        // search, p points to the new node's parent
        Node<K, V> p = null, t = root;
        int cmp = 0;
        while (t != null) {
            p = t;
            cmp = key.compareTo(t.key);
            if (cmp < 0) {
                t = t.left;
            } else if (cmp > 0) {
                t = t.right;
            } else {
                V oldValue = t.value;
                t.key = key;
                t.value = value;
                return oldValue;
            }
        }
        if (cmp < 0) {
            p.left = new Node<>(key, value);
            p.left.parent = p;
            p.height = max(height(p.left), height(p.right)) + 1;
        } else { // cmp > 0
            p.right = new Node<>(key, value);
            p.right.parent = p;
            p.height = max(height(p.left), height(p.right)) + 1;
        }
        size++;
        rebalance(p);
        return value;
    }

    private Node<K, V> minimum(Node<K, V> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private void transplant(Node<K, V> u, Node<K, V> v) {
        /**
         * utility function when removing node replace the subtree rooted at u with the
         * subtree rooted at v u's parent becomes v's parent replace u as a child of its
         * parent by v
         */
        if (u.parent == null) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        if (v != null) {
            v.parent = u.parent;
        }
    }

    public V remove(K key) {
        Node<K, V> deleteNode = search(root, key);
        if (deleteNode == null) {
            return null;
        }
        V oldValue = deleteNode.value; // return value
        Node<K, V> fixPoint; // start point to rebalance
        if (deleteNode.left == null) {
            transplant(deleteNode, deleteNode.right);
            fixPoint = deleteNode.right;
        } else if (deleteNode.right == null) {
            transplant(deleteNode, deleteNode.left);
            fixPoint = deleteNode.left;
        } else {
            Node<K, V> succ = minimum(deleteNode.right);
            fixPoint = succ;
            if (succ.parent != deleteNode) { // save unnecessary reference changing
                fixPoint = succ.parent;
                // replace succ by its right child
                transplant(succ, succ.right);
                // turn deleteNode's right child to succ's right child
                succ.right = deleteNode.right;
                succ.right.parent = succ;
            }
            // turn deleteNode's left child to succ's left child
            succ.left = deleteNode.left;
            succ.left.parent = succ;
            // replace deleteNode as a child of its parent by succ
            transplant(deleteNode, succ);
        }
        size--;
        rebalance(fixPoint);
        return oldValue;
    }

    /** utility function */
    private int height(Node<K, V> node) {
        return (node == null ? 0 : node.height);
    }

    private int max(int x, int y) {
        return (x >= y ? x : y);
    }

    public int size() {
        return size;
    }

    @Override
    public boolean containsKey(K key) {
        return (search(root, key) == null ? false : true);
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
