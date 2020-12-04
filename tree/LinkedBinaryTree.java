package tree;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class LinkedBinaryTree<E> implements BinaryTree<E> {
    protected Node<E> root = null;
    protected int size = 0;

    public LinkedBinaryTree() {
    }

    protected static class Node<E> implements Position<E> {
        private E element;
        private Node<E> parent;
        private Node<E> left;
        private Node<E> right;

        public Node(E e, Node<E> parent, Node<E> left, Node<E> right) {
            this.element = e;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }

        @Override
        public E getElement() {
            return this.element;
        }

        public Node<E> getParent() {
            return this.parent;
        }

        public Node<E> getLeft() {
            return this.left;
        }

        public Node<E> getRight() {
            return this.right;
        }

        public void setElement(E e) {
            this.element = e;
        }

        public void setParent(Node<E> node) {
            this.parent = node;
        }

        public void setLeft(Node<E> node) {
            this.left = node;
        }

        public void setRight(Node<E> node) {
            this.right = node;
        }
    }

    protected Node<E> createNode(E e, Node<E> parent, Node<E> right, Node<E> left) {
        return new Node<>(e, parent, right, left);
    }

    protected Node<E> validate(Position<E> p) throws IllegalArgumentException {
        if (!(p instanceof Node)) {
            throw new IllegalArgumentException("not valid type");
        }
        Node<E> node = (Node<E>) p;
        // convention: set a node's parent to itself when it's removed
        if (node.getParent() == node) {
            throw new IllegalArgumentException("p is no longer in the tree");
        }
        return node;
    }

    public Position<E> addRoot(E e) throws IllegalStateException {
        if (!isEmpty()) {
            throw new IllegalStateException("tree is not empty");
        }
        root = createNode(e, null, null, null);
        size = 1;
        return root;
    }

    public Position<E> addLeft(Position<E> p, E e) throws IllegalArgumentException {
        Node<E> parent = validate(p);
        if (parent.getLeft() != null) {
            throw new IllegalArgumentException("node already has a left child");
        }
        Node<E> leftChild = createNode(e, parent, null, null);
        parent.setLeft(leftChild);
        size++;
        return leftChild;
    }

    public Position<E> addRight(Position<E> p, E e) throws IllegalArgumentException {
        Node<E> parent = validate(p);
        if (parent.getRight() != null) {
            throw new IllegalArgumentException("node already has a left child");
        }
        Node<E> rightChild = createNode(e, parent, null, null);
        parent.setRight(rightChild);
        size++;
        return rightChild;
    }

    public E set(Position<E> p, E e) {
        Node<E> node = validate(p);
        E temp = node.getElement();
        node.setElement(e);
        return temp;
    }

    public void attach(Position<E> parent, LinkedBinaryTree<E> leftTree, LinkedBinaryTree<E> rightTree) {
        Node<E> node = validate(parent);
        if (isInternal(node)) {
            throw new IllegalArgumentException("parent node must be a leaf");
        }
        size += leftTree.size() + rightTree.size();
        if (!leftTree.isEmpty()) {
            node.setLeft(leftTree.root);
            leftTree.root.setParent(node);
            leftTree.root = null;
            leftTree.size = 0;
        }
        if (!rightTree.isEmpty()) {
            rightTree.root.setParent(node);
            node.setRight(rightTree.root);
            rightTree.root = null;
            rightTree.size = 0;
        }
    }

    public E remove(Node<E> n) {
        Node<E> node = validate(n);
        if (numChildren(node) == 2) {
            throw new IllegalArgumentException("node has two children!");
        }
        Node<E> child = (node.getLeft() != null ? node.getLeft() : node.getRight());
        if (child != null) {
            child.setParent(node.getParent());
        }
        if (node == root) {
            root = child;
        } else {
            Node<E> parent = node.getParent();
            if (node == parent.getLeft()) {
                parent.setLeft(child);
            } else {
                parent.setRight(child);
            }
        }
        size--;
        E temp = node.getElement();
        node.setElement(null);
        node.setLeft(null);
        node.setRight(null);
        node.setParent(node);
        return temp;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Position<E> root() {
        return root;
    }

    @Override
    public Position<E> parent(Position<E> p) throws IllegalArgumentException {
        Node<E> node = validate(p);
        return node.getParent();
    }

    @Override
    public Position<E> left(Position<E> p) throws IllegalArgumentException {
        Node<E> node = validate(p);
        return node.getLeft();
    }

    @Override
    public Position<E> right(Position<E> p) throws IllegalArgumentException {
        Node<E> node = validate(p);
        return node.getRight();
    }

    private void preorderSubtree(Position<E> node, List<Position<E>> snapshot) {
        snapshot.add(node);
        for (Position<E> child : children(node)) {
            preorderSubtree(child, snapshot);
        }
    }

    private void postorderSubtree(Position<E> node, List<Position<E>> snapshot) {
        for (Position<E> child : children(node)) {
            postorderSubtree(child, snapshot);
        }
        snapshot.add(node);
    }

    private void inorderSubtree(Position<E> node, List<Position<E>> snapshot) {
        if (left(node) != null) {
            inorderSubtree(left(node), snapshot);
        }
        snapshot.add(node);
        if (right(node) != null) {
            inorderSubtree(right(node), snapshot);
        }
    }

    @Override
    public Iterator<E> iterator() {
        // iterate over all elements, adapt inorder traversal
        return new elementIterator();
    }

    private class elementIterator implements Iterator<E> {

        private final Iterator<Position<E>> nodeIterator = new nodeIterator();

        public elementIterator() {
        }

        @Override
        public boolean hasNext() {
            return nodeIterator.hasNext();
        }

        @Override
        public E next() {
            return nodeIterator.next().getElement();
        }
    }

    @Override
    public Iterator<Position<E>> nodes() {
        // iterate over positions of the binary tree, adapt inorder traversal
        return new nodeIterator();
    }

    private class nodeIterator implements Iterator<Position<E>> {
        /**
         * lazy iterator, inorder traversal
         */
        private int numVisitedNodes;
        private final ArrayDeque<Position<E>> stack;

        public nodeIterator() {
            numVisitedNodes = 0;
            stack = new ArrayDeque<>();
        }

        @Override
        public boolean hasNext() {
            return (numVisitedNodes != size());
        }

        @Override
        public Position<E> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (stack.isEmpty()) {
                Position<E> node = root;
                while (node != null) {
                    stack.push(node);
                    node = left(node);
                }
            }
            Position<E> res = stack.pop();
            Position<E> node = right(res);
            while (node != null) {
                stack.push(node);
                node = left(node);
            }
            numVisitedNodes++;
            return res;
        }
    }
}