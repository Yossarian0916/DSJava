package bst;

public class RedBlackTree<K extends Comparable<K>, V> implements SortedMap<K, V> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private final Node sentinel;
    private Node root;
    private int size;

    protected long rotateCount; // count rotation
    protected long cmpCount; // count comparison

    public RedBlackTree() {
        this.sentinel = new Node(null, null);
        this.sentinel.color = BLACK;
        this.root = sentinel;
    }

    private final class Node {
        K key;
        V value;
        Node parent;
        Node left, right;
        boolean color;
        int size;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.color = RED;
            this.left = RedBlackTree.this.sentinel;
            this.right = RedBlackTree.this.sentinel;
        }
    }

    public V get(K key) {
        Node node = root;
        while (node != sentinel) {
            cmpCount++;
            if (key.compareTo(node.key) < 0) {
                node = node.left;
            } else if (key.compareTo(node.key) > 0) {
                node = node.right;
            } else {
                break;
            }
        }
        if (node == sentinel) {
            return null;
        }
        return node.value;
    }

    private void rotateLeft(Node node) {
        /**
         * turn node to be node.right's new left node
         */
        Node y = node.right;
        node.right = y.left; // turn y.left subtree to node.right subtree
        if (y.left != sentinel) {
            y.left.parent = node;
        }
        // link node.parent to y
        y.parent = node.parent;
        if (node.parent == sentinel) {
            root = y;
        } else if (node == node.parent.left) {
            node.parent.left = y;
        } else {
            node.parent.right = y;
        }
        y.left = node; // put node on y.left
        node.parent = y;
        rotateCount++;
    }

    private void rotateRight(Node node) {
        /**
         * turn node to be node.right's new right node
         */
        Node x = node.left;
        node.left = x.right; // turn x's right subtree to node's left subtree
        if (x.right != sentinel) {
            x.right.parent = node;
        }
        // link node.parent to x
        x.parent = node.parent;
        if (node.parent == sentinel) {
            root = x;
        } else if (node == node.parent.left) {
            node.parent.left = x;
        } else {
            node.parent.right = x;
        }
        x.right = node; // put node on x.right
        node.parent = x;
        rotateCount++;
    }

    private void insertFixUp(Node node) {
        while (node.parent.color == RED) {
            cmpCount++;
            if (node.parent == node.parent.parent.left) {
                Node uncle = node.parent.parent.right;
                cmpCount++;
                if (uncle.color == RED) {
                    // case: left leaning, RED -/ RED -/ BLACK -\ BLACK
                    // flip color
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    // move upwards
                    node = node.parent.parent;
                } else if (node == node.parent.right) {
                    // case: left leaning, RED -/ RED -/ BLACK -\ RED
                    // change case to: right leaning, BLACK -\ RED -\ RED
                    node = node.parent;
                    rotateLeft(node);
                } else {
                    // case: right leaning, BLACK -\ RED -\ RED
                    node.parent.color = BLACK;
                    node.parent.parent.color = RED;
                    rotateRight(node.parent.parent);
                }
                cmpCount++;
            } else { // node.parent == node.parent.parent.right
                Node uncle = node.parent.parent.left;
                cmpCount++;
                if (uncle.color == RED) {
                    // case: right leaning, BLACK -/ BLACK -\ RED -\ RED
                    // flip color
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    // move upwards
                    node = node.parent.parent;
                } else if (node == node.parent.left) {
                    // case: right leaning, RED -/ BLACK -\ RED -\ RED
                    // change case to: left leaning, RED -/ RED -/ BLACK
                    node = node.parent;
                    rotateRight(node);
                } else {
                    // case: left leaning, RED -/ RED -/ BLACK
                    node.parent.color = BLACK;
                    node.parent.parent.color = RED;
                    rotateLeft(node.parent.parent);
                }
                cmpCount++;
            }
        }
        root.color = BLACK;
    }

    public V put(K key, V value) {
        // search, p points to the new node's parent
        Node p = sentinel, t = root;
        while (t != sentinel) {
            p = t;
            cmpCount++;
            if (key.compareTo(t.key) < 0) {
                t = t.left;
            } else if (key.compareTo(t.key) > 0) {
                t = t.right;
            } else {
                V oldValue = t.value;
                t.key = key;
                t.value = value;
                return oldValue;
            }
        }
        Node newNode = new Node(key, value);
        newNode.parent = p;
        if (p == sentinel) {
            root = newNode;
        } else if (key.compareTo(p.key) < 0) {
            p.left = newNode;
        } else { // cmp > 0
            p.right = newNode;
        }
        cmpCount++;
        size++;
        insertFixUp(newNode);
        return value;
    }

    private void deleteFixUp(Node node) {
        while ((node != root) && (node.color == BLACK)) {
            if (node == node.parent.left) {
                Node sibling = node.parent.right;
                if (sibling.color == RED) {
                    sibling.color = BLACK;
                    node.parent.color = RED;
                    rotateLeft(node.parent);
                    sibling = node.parent.right;
                }
                if ((sibling.left.color == BLACK) && (sibling.right.color == BLACK)) {
                    sibling.color = RED;
                    node = node.parent;
                } else if (sibling.right.color == BLACK) {
                    sibling.left.color = BLACK;
                    sibling.color = RED;
                    rotateRight(sibling);
                    sibling = node.parent.right;
                } else {
                    sibling.color = node.parent.color;
                    node.parent.color = BLACK;
                    sibling.right.color = BLACK;
                    rotateLeft(node.parent);
                    node = root;
                }
            } else {
                Node sibling = node.parent.left;
                if (sibling.color == RED) {
                    sibling.color = BLACK;
                    node.parent.color = RED;
                    rotateRight(node.parent);
                    sibling = node.parent.left;
                }
                if ((sibling.left.color == BLACK) && (sibling.right.color == BLACK)) {
                    sibling.color = RED;
                    node = node.parent;
                } else if (sibling.left.color == BLACK) {
                    sibling.right.color = BLACK;
                    sibling.color = RED;
                    rotateLeft(sibling);
                    sibling = node.parent.left;
                } else {
                    sibling.color = node.parent.color;
                    node.parent.color = BLACK;
                    sibling.left.color = BLACK;
                    rotateRight(node.parent);
                    node = root;
                }
            }
        }
        node.color = BLACK;
    }

    public V remove(K key) {
        // search
        Node node = root;
        while (node != sentinel) {
            if (key.compareTo(node.key) < 0) {
                node = node.left;
            } else if (key.compareTo(node.key) > 0) {
                node = node.right;
            } else {
                break;
            }
        }
        // unsuccessful search
        if (node == sentinel) {
            return null;
        }
        // remove
        Node fixPoint = node;
        boolean movedColor = fixPoint.color;
        if (node.left == sentinel) {
            fixPoint = node.right;
            transplant(node, node.right);
        } else if (node.right == sentinel) {
            fixPoint = node.left;
            transplant(node, node.left);
        } else {
            Node succ = minimum(node.right);
            movedColor = succ.color;
            fixPoint = succ.right;
            if (succ.parent == node) {
                fixPoint.parent = succ;
            } else {
                transplant(succ, succ.right);
                succ.right = node.right;
                succ.right.parent = succ;
            }
            succ.left = node.left;
            succ.left.parent = succ;
            transplant(node, succ);
            succ.color = node.color;
        }
        if (movedColor == BLACK) {
            deleteFixUp(fixPoint);
        }
        V oldValue = node.value;
        size--;
        return oldValue;
    }

    private void transplant(Node u, Node v) {
        /**
         * utility function when removing node replace the subtree rooted at u with the
         * subtree rooted at v u's parent becomes v's parent replace u as a child of its
         * parent by v
         */
        if (u.parent == sentinel) {
            root = v;
        } else if (u == u.parent.left) {
            u.parent.left = v;
        } else {
            u.parent.right = v;
        }
        v.parent = u.parent;
    }

    private Node minimum(Node node) {
        while (node.left != sentinel) {
            cmpCount++;
            node = node.left;
        }
        return node;
    }

    public int size() {
        return size;
    }

    private int getHeight(Node node, int depth) {
        if (node == sentinel) {
            return depth;
        }
        int leftHeight = getHeight(node.left, depth + 1);
        int rightHeight = getHeight(node.right, depth + 1);
        return (leftHeight > rightHeight ? leftHeight : rightHeight);
    }

    public int getHeight() {
        return getHeight(root, -1);
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
