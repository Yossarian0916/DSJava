package stack;

import linkedlist.SinglyLinkedList;

/* 
 * adapter SinglyLinkedList class
 * support stack ADT operations
 * insert new Node at the head of the linked list
 */
public class LinkedStack<E> implements Stack<E> {
    private SinglyLinkedList<E> list;

    public LinkedStack() {
        list = new SinglyLinkedList<>();
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public E pop() {
        return list.removeFirst();
    }

    @Override
    public void push(E e) {
        list.addFirst(e);
    }

    @Override
    public E top() {
        return list.first();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public String toString() {
        /* from stack top to bottom */
        return list.toString();
    }
}
