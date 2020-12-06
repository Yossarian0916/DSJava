package list;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayList<E> implements List<E>, Iterable<E> {
    /**
     * dynamic array implementation
     */

    private static final int CAPACITY = 16;
    private int size = 0;
    private Object[] elementData;

    public ArrayList() {
        this(CAPACITY);
    }

    public ArrayList(int capacity) {
        elementData = new Object[capacity];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    private void checkElementIndex(int i) throws IndexOutOfBoundsException {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException("Illegal index: " + i);
        }
    }

    private void checkPositionIndex(int i) throws IndexOutOfBoundsException {
        if (i < 0 || i > size) {
            throw new IndexOutOfBoundsException("Illegal index: " + i);
        }
    }

    @Override
    public E get(int i) throws IndexOutOfBoundsException {
        checkElementIndex(i);
        @SuppressWarnings("unchecked")
        final E e = (E) elementData[i];
        return e;
    }

    @Override
    public E set(int i, E e) throws IndexOutOfBoundsException {
        checkElementIndex(i);
        @SuppressWarnings("unchecked")
        final E temp = (E) elementData[i];
        elementData[i] = e;
        return temp;
    }

    public void resize(int capacity) {
        Object[] temp = new Object[capacity];
        System.arraycopy(elementData, 0, temp, 0, elementData.length);
        elementData = temp;
    }

    @Override
    public void add(int i, E e) throws IndexOutOfBoundsException {
        checkPositionIndex(i);
        if (size == elementData.length) {
            resize(elementData.length * 2);
        }
        for (int k = size - 1; k >= i; k--) {
            elementData[k + 1] = elementData[k];
        }
        elementData[i] = e;
        size++;
    }

    public void add(E e) {
        add(size, e);
    }

    public void addFirst(E e) {
        add(0, e);
    }

    public void addLast(E e) {
        add(size, e);
    }

    @Override
    public E remove(int i) throws IndexOutOfBoundsException {
        checkElementIndex(i);
        @SuppressWarnings("unchecked")
        final E temp = (E) elementData[i];
        for (int k = i; k < size - 1; k++) {
            elementData[k] = elementData[k + 1];
        }
        elementData[size - 1] = null;
        size--;
        return temp;
    }

    public E removeFirst() {
        return remove(0);
    }

    public E removeLast() {
        if (isEmpty()) {
            return null;
        }
        @SuppressWarnings("unchecked")
        final E temp = (E) elementData[size - 1];
        elementData[size - 1] = null;
        size--;
        return temp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            @SuppressWarnings("unchecked")
            final E e = (E) elementData[i];
            sb.append(e).append(" ");
        }
        return sb.toString();
    }

    @Override
    public Iterator<E> iterator() {
        return new listIterator();
    }

    private class listIterator implements Iterator<E> {
        private int lastReturnedIndex = -1;
        private int cursor;

        public listIterator() {
            cursor = 0;
        }

        @Override
        public boolean hasNext() {
            return (cursor < size);
        }

        @Override
        @SuppressWarnings("unchecked")
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastReturnedIndex = cursor;
            final E e = (E) ArrayList.this.elementData[lastReturnedIndex];
            cursor++;
            return e;
        }

        @Override
        public void remove() {
            if (lastReturnedIndex < 0) {
                throw new IllegalStateException();
            }
            try {
                ArrayList.this.remove(lastReturnedIndex);
                cursor = lastReturnedIndex;
                lastReturnedIndex = -1;
            } catch (IndexOutOfBoundsException ex) {
                throw new RuntimeException("concurrent modification while remove list elements");
            }
        }
    }
}
