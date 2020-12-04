package linkedlist;

public class DoublyLinkedList<E> {

    private int size = 0;
    private Node<E> header = null; // header sentinel node
    private Node<E> trailer = null; // trailer sentinel ode

    public DoublyLinkedList() {
        this.header = new Node<E>(null, null, null);
        this.trailer = new Node<E>(null, null, null);
        header.setNext(trailer);
        trailer.setPrev(header);
    }

    private static class Node<E> {

        private E element;
        private Node<E> next;
        private Node<E> prev;

        public Node(E e, Node<E> prev, Node<E> next) {
            this.element = e;
            this.next = next;
            this.prev = prev;
        }

        public E getElement() {
            return this.element;
        }

        public void setNext(Node<E> n) {
            this.next = n;
        }

        public Node<E> getNext() {
            return this.next;
        }

        public void setPrev(Node<E> p) {
            this.prev = p;
        }

        public Node<E> getPrev() {
            return this.prev;
        }
    }

    public int size() {
        return size;
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

    public boolean isEmpty() {
        return (size == 0);
    }

    private void addBetween(E e, Node<E> predecessor, Node<E> successor) {
        Node<E> node = new Node<>(e, predecessor, successor);
        predecessor.setNext(node);
        successor.setPrev(node);
        size++;
    }

    public void addFirst(E e) {
        addBetween(e, header, header.getNext());
    }

    public void addLast(E e) {
        addBetween(e, trailer.getPrev(), trailer);
    }

    private E remove(Node<E> node) {
        Node<E> predecessor = node.getPrev();
        Node<E> successor = node.getNext();
        predecessor.setNext(successor);
        successor.setPrev(predecessor);
        size--;
        return node.getElement();
    }

    public E removeFirst() {
        if (isEmpty()) {
            return null;
        }
        return remove(header.getNext());
    }

    public E removeLast() {
        if (isEmpty()) {
            return null;
        }
        return remove(trailer.getPrev());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<E> node = header.getNext();
        while (node != trailer) {
            sb.append(node.getElement() + "->");
            node = node.getNext();
        }
        sb.append("null");
        return sb.toString();
    }
}
