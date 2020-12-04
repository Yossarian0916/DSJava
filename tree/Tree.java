package tree;

import java.util.Iterator;

public interface Tree<E> extends Iterable<E> {
    Position<E> root();

    // return the parent node of the given node
    Position<E> parent(Position<E> p) throws IllegalArgumentException;

    // return the children nodes of the given node
    Iterable<Position<E>> children(Position<E> p) throws IllegalArgumentException;

    int numChildren(Position<E> p) throws IllegalArgumentException;

    int size();

    Iterator<E> iterator();

    Iterator<Position<E>> nodes();

    /* default implementations */
    default boolean isInternal(Position<E> p) throws IllegalArgumentException {
        return numChildren(p) > 0;
    };

    default boolean isExternal(Position<E> p) throws IllegalArgumentException {
        return numChildren(p) == 0;
    };

    default boolean isRoot(Position<E> p) throws IllegalArgumentException {
        return p == root();
    };

    default boolean isEmpty() {
        return size() == 0;
    };

    default int depth(Position<E> p) {
        int d = 0;
        while (!isRoot(p)) {
            d++;
            p = parent(p);
        }
        return d;
    }

    default int height(Position<E> p) {
        int h = 0;
        for (Position<E> c : children(p)) {
            h = Math.max(h, 1 + height(c));
        }
        return h;
    }
}