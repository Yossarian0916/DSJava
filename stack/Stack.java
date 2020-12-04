package stack;

public interface Stack<E> {
    int size();

    boolean isEmpty();

    E pop();

    void push(E e);

    E top();
}
