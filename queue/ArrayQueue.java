package queue;

public class ArrayQueue<E> implements Queue<E> {
    /**
     * fixed-size array based queue implementation
     */

    public static final int CAPACITY = 16;
    private int len; // real length of data array, len = capacity + 1 for easy implementation of
                     // isEmpty() method
    private Object[] element;
    private int f = 0; // the front of queue
    private int r = 0; // the one element after the tail of queue

    public ArrayQueue() {
        this(CAPACITY);
    }

    public ArrayQueue(int cap) {
        this.len = cap + 1;
        element = new Object[this.len];
    }

    @Override
    public int size() {
        return (len + r - f) % len;
    }

    @Override
    public boolean isEmpty() {
        return (r == f);
    }

    @Override
    public E first() {
        if (isEmpty()) {
            return null;
        }
        @SuppressWarnings("unchecked")
        final E firstElem = (E) element[f];
        return firstElem;
    }

    @Override
    public void enqueue(E e) {
        if (size() == len - 1) {
            throw new IllegalStateException("queue is full");
        }
        element[r] = e;
        r = (r + 1) % len;
    }

    @Override
    public E dequeue() {
        if (isEmpty()) {
            return null;
        }
        @SuppressWarnings("unchecked")
        final E e = (E) element[f];
        element[f] = null;
        f = (f + 1) % len;
        return e;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int idx = f;
        while (idx != r) {
            sb.append(element[idx] + " ");
            idx = (idx + 1) % len;
        }
        return sb.toString();
    }
}
