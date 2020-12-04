package linkedlist;

public class SinglyLinkedList<E> {

    private int size = 0;
    private Node<E> head = null;
    private Node<E> tail = null;

    public SinglyLinkedList() {
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
        return head.getElement();
    }

    public E last() {
        if (isEmpty()) {
            return null;
        }
        return tail.getElement();
    }

    public void addFirst(E e) {
        Node<E> node = new Node<>(e);
        if (head == null) {
            head = node;
            tail = head;
        } else {
            node.setNext(head);
            head = node;
        }
        size++;
    }

    public void addLast(E e) {
        Node<E> node = new Node<>(e);
        if (head == null) {
            head = node;
        } else {
            tail.setNext(node);
        }
        tail = node;
        size++;
    }

    public E removeFirst() {
        if (isEmpty()) {
            return null;
        }
        E removedElem = head.getElement();
        head = head.getNext();
        size--;
        if (size == 0) {
            tail = null;
        }
        return removedElem;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node<E> node = head;
        while (node != null) {
            sb.append(node.getElement() + "->");
            node = node.getNext();
        }
        sb.append("null");
        return sb.toString();
    }
}