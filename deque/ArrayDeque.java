package deque;

public class ArrayDeque<E> implements Deque<E> {
    /**
     * fixed-size circular array based deque implementation
     */

    private static final int CAPACITY = 16;
    private Object[] elementData;
    private int f = 0; // the front of the deque
    private int size = 0;

    public ArrayDeque() {
        this(CAPACITY);
    }

    public ArrayDeque(int cap) {
        elementData = new Object[cap];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    @Override
    public E first() {
        if (isEmpty()) {
            return null;
        }
        @SuppressWarnings("unchecked")
        final E e = (E) elementData[f];
        return e;
    }

    @Override
    public E last() {
        if (isEmpty()) {
            return null;
        }
        int idx = (f + size - 1) % elementData.length;
        @SuppressWarnings("unchecked")
        final E e = (E) elementData[idx];
        return e;
    }

    @Override
    public void addFirst(E e) {
        if (size == elementData.length) {
            throw new IllegalStateException("deque is full");
        }
        f = (f - 1 + elementData.length) % elementData.length;
        elementData[f] = e;
        size++;
    }

    @Override
    public void addLast(E e) {
        if (size == elementData.length) {
            throw new IllegalStateException("deque is full");
        }
        int idx = (f + size) % elementData.length;
        elementData[idx] = e;
        size++;
    }

    @Override
    public E removeFirst() {
        if (isEmpty()) {
            return null;
        }
        @SuppressWarnings("unchecked")
        final E e = (E) elementData[f];
        elementData[f] = null;
        f = (f + 1) % elementData.length;
        size--;
        return e;
    }

    @Override
    public E removeLast() {
        if (isEmpty()) {
            return null;
        }
        int idx = (f + size - 1) % elementData.length;
        @SuppressWarnings("unchecked")
        final E e = (E) elementData[idx];
        elementData[idx] = null;
        size--;
        return e;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size(); i++) {
            int idx = (f + i) % elementData.length;
            sb.append(elementData[idx] + " ");
        }
        return sb.toString();
    }
}