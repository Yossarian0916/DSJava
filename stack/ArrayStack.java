package stack;

public class ArrayStack<E> implements Stack<E> {
    public static final int CAPACITY = 16;
    private int capacity;
    private Object[] data;
    private int top = -1;

    public ArrayStack() {
        this(CAPACITY);
    }

    public ArrayStack(int cap) {
        capacity = cap;
        data = new Object[capacity];
    }

    @Override
    public int size() {
        return (top + 1);
    }

    @Override
    public boolean isEmpty() {
        return (top <= -1);
    }

    @Override
    public E pop() {
        if (isEmpty()) {
            return null;
        }
        @SuppressWarnings("unchecked")
        final E element = (E) data[top];
        data[top] = null;
        top--;
        return element;
    }

    @Override
    public void push(E e) throws IllegalStateException {
        if (size() == capacity) {
            throw new IllegalStateException("overflow");
        }
        top++;
        data[top] = e;
    }

    @Override
    public E top() {
        if (isEmpty()) {
            return null;
        }
        @SuppressWarnings("unchecked")
        final E element = (E) data[top];
        return element;
    }

    @Override
    public String toString() {
        /* from stack top to bottom */
        StringBuilder sb = new StringBuilder();
        for (int i = top; i >= 0; i--) {
            sb.append(data + " ");
        }
        return sb.toString();
    }
}
