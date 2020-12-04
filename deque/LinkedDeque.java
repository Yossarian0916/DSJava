package deque;

import linkedlist.DoublyLinkedList;

public class LinkedDeque<E> implements Deque<E> {
    private DoublyLinkedList<E> dList;

    public LinkedDeque() {
        dList = new DoublyLinkedList<>();
    }

    @Override
    public int size() {
        return dList.size();
    }

    @Override
    public boolean isEmpty() {
        return dList.isEmpty();
    }

    @Override
    public E first() {
        return dList.first();
    }

    @Override
    public E last() {
        return dList.last();
    }

    @Override
    public void addFirst(E e) {
        dList.addFirst(e);
    }

    @Override
    public void addLast(E e) {
        dList.addLast(e);
    }

    @Override
    public E removeFirst() {
        return dList.removeFirst();
    }

    @Override
    public E removeLast() {
        return dList.removeLast();
    }

    @Override
    public String toString() {
        return dList.toString();
    }

}