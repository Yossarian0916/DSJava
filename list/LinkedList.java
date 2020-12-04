package list;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedList<E> implements List<E>, Iterable<E> {
    private final Node<E> header;
    private final Node<E> trailer;
    private int size;

    public LinkedList() {
        this.header = new Node<>(null, null, null);
        this.trailer = new Node<>(null, null, null);
        this.header.setNext(this.trailer);
        this.trailer.setPrev(this.header);
    }

    private static class Node<E> {
        private E element;
        private Node<E> prev;
        private Node<E> next;

        public Node(E e, Node<E> prev, Node<E> next) {
            this.element = e;
            this.prev = prev;
        }

        public E getElement() {
            return this.element;
        }

        public Node<E> getPrev() {
            return this.prev;
        }

        public Node<E> getNext() {
            return this.next;
        }

        public void setElement(E e) {
            this.element = e;
        }

        public void setPrev(Node<E> p) {
            this.prev = p;
        }

        public void setNext(Node<E> n) {
            this.next = n;
        }
    }

    public E first() {
        if (isEmpty()) {
            return null;
        }
        return header.getNext().getElement();
    }

    public E last() {
        if (isEmpty()) {
            return null;
        }
        return trailer.getPrev().getElement();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    // check if i is the index of an existing element
    private boolean isElementIndex(int i) {
        return (i >= 0 && i < size);
    }

    // check if i is a valid position to add new element
    private boolean isPositionIndex(int i) {
        return (i >= 0 && i <= size);
    }

    private void checkElementIndex(int i) {
        if (!isElementIndex(i)) {
            throw new IndexOutOfBoundsException("the index is out of bound");
        }
    }

    private void checkPositionIndex(int i) {
        if (!isPositionIndex(i)) {
            throw new IndexOutOfBoundsException("the index is not a valid position to add new element");
        }
    }

    private Node<E> nodeAt(int i) {
        checkElementIndex(i);
        Node<E> node;
        if (i < this.size / 2) {
            // i locates at the first half of the linked list, iterate from the head node
            node = header.getNext();
            for (int k = 0; k < i; k++) {
                node = node.getNext();
            }
            return node;
        } else {
            // i locates at the second half of the linked list, iterate from the tail node
            node = trailer.getPrev();
            for (int k = size - 1; k > i; k--) {
                node = node.getPrev();
            }
            return node;
        }
    }

    @Override
    public E get(int i) throws IndexOutOfBoundsException {
        if (isEmpty()) {
            return null;
        }
        checkElementIndex(i);
        Node<E> node = nodeAt(i);
        return node.getElement();
    }

    @Override
    public E set(int i, E e) throws IndexOutOfBoundsException {
        if (isEmpty()) {
            return null;
        }
        checkElementIndex(i);
        Node<E> node = nodeAt(i);
        final E oldElement = node.getElement();
        node.setElement(e);
        return oldElement;
    }

    private void addBetween(E e, Node<E> pred, Node<E> succ) {
        Node<E> node = new Node<>(e, pred, succ);
        pred.setNext(node);
        succ.setPrev(node);
        size++;
    }

    public void addFirst(E e) {
        addBetween(e, header, header.getNext());
    }

    public void addLast(E e) {
        addBetween(e, trailer.getPrev(), trailer);
    }

    @Override
    public void add(int i, E e) throws IndexOutOfBoundsException {
        checkPositionIndex(i);
        Node<E> node = nodeAt(i);
        Node<E> pred = node.getPrev();
        Node<E> succ = node.getNext();
        addBetween(e, pred, succ);
    }

    private void purgeNode(Node<E> node) {
        // help with gc,
        // when called, check node validation already
        node.setElement(null);
        node.setNext(null);
        node.setPrev(null);
    }

    @Override
    public E remove(int i) throws IndexOutOfBoundsException {
        checkElementIndex(i);
        Node<E> node = nodeAt(i);
        final E removedElement = node.getElement();
        Node<E> predecessor = node.getPrev();
        Node<E> successor = node.getNext();
        predecessor.setNext(successor);
        successor.setPrev(predecessor);
        size--;
        purgeNode(node);
        return removedElement;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<E> node = header.getNext();
        for (int i = 0; i < size; i++) {
            sb.append(node.getElement()).append(" ");
            node = node.getNext();
        }
        return sb.toString();
    }

    @Override
    public Iterator<E> iterator() {
        return new listIterator();
    }

    private class listIterator implements Iterator<E> {
        private Node<E> lastReturned = null;
        private Node<E> next;
        private int nextIndex;

        public listIterator() {
            next = header.getNext();
            nextIndex = 0;
        }

        @Override
        public boolean hasNext() {
            return (nextIndex < size);
        }

        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastReturned = next;
            next = next.getNext();
            nextIndex++;
            return lastReturned.getElement();
        }

        public void reset() {
            lastReturned = null;
            next = header.getNext();
            nextIndex = 0;
        }
    }
}
