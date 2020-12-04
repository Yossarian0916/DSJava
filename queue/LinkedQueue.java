package queue;

import linkedlist.SinglyLinkedList;

public class LinkedQueue<E> implements Queue<E> {
    private SinglyLinkedList<E> list;

    public LinkedQueue() {
        list = new SinglyLinkedList<>();
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public void enqueue(E e) {
        list.addLast(e);
    }

    @Override
    public E dequeue() {
        if (isEmpty()) {
            return null;
        }
        E e = list.removeFirst();
        return e;
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public E first() {
        if (isEmpty()) {
            return null;
        }
        return list.first();
    }

    @Override
    public String toString() {
        return list.toString();
    }
}
