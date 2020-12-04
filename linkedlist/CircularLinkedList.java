package linkedlist;

public class CircularLinkedList<E> {
    private int size = 0;
    private Node<E> tail = null;

    public CircularLinkedList() {
    }

    private static class Node<E> {
        private E element;
        private Node<E> next;

        public Node(E e) {
            this(e, null);
        }

        public Node(E e, Node<E> next) {
            this.element = e;
            this.next = next;
        }

        public E getElement() {
            return this.element;
        }

        public void setNext(Node<E> node) {
            this.next = node;
        }

        public Node<E> getNext() {
            return this.next;
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public E first() {
        if (isEmpty()) {
            return null;
        }
        return tail.getNext().getElement();
    }

    public E last() {
        if (isEmpty()) {
            return null;
        }
        return tail.getElement();
    }

    public void rotate() {
        if (tail != null) {
            tail = tail.getNext();
        }
    }

    public void addFirst(E e) {
        Node<E> node = new Node<>(e);
        if (isEmpty()) {
            tail = node;
            tail.setNext(tail);
        } else {
            Node<E> head = tail.getNext();
            tail.setNext(node);
            node.setNext(head);
        }
        size++;
    }

    public void addLast(E e) {
        addFirst(e);
        tail = tail.getNext();
    }

    public E removeFirst() {
        if (isEmpty()) {
            return null;
        }
        Node<E> head = tail.getNext();
        if (head == tail) {
            tail = null;
        } else {
            tail.setNext(head.getNext());
        }
        size--;
        return head.getElement();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<E> node = this.tail.getNext();
        sb.append("->");
        for (int i = 0; i < size(); i++) {
            sb.append(node.getElement() + "->");
            node = node.getNext();
        }
        return sb.toString();
    }
}