package tree;

import java.util.ArrayList;
import java.util.List;

public interface BinaryTree<E> extends Tree<E> {

    // return the left child of the given node
    Position<E> left(Position<E> p) throws IllegalArgumentException;

    // return the right child of the given node
    Position<E> right(Position<E> p) throws IllegalArgumentException;

    /* default implementations */
    // return the sibling nodes of the given node
    default Position<E> sibling(Position<E> p) throws IllegalArgumentException {
        Position<E> parent = parent(p);
        if (parent == null) {
            return null;
        }
        if (p == left(parent)) {
            return right(parent);
        } else {
            return left(parent);
        }
    }

    @Override
    default int numChildren(Position<E> p) throws IllegalArgumentException {
        int count = 0;
        if (left(p) != null) {
            count++;
        }
        if (right(p) != null) {
            count++;
        }
        return count;
    }

    @Override
    default Iterable<Position<E>> children(Position<E> p) throws IllegalArgumentException {
        List<Position<E>> snapshot = new ArrayList<>(2);
        if (left(p) != null) {
            snapshot.add(left(p));
        }
        if (right(p) != null) {
            snapshot.add(right(p));
        }
        return snapshot;
    }
}