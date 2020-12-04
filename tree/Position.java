package tree;

public interface Position<E> {
    E getElement() throws IllegalStateException;
}