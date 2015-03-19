package net.evlikat.hexatrix.entities;

import java.util.Iterator;
import java.util.List;

/**
 * Created by RSProkhorov on 18.03.2015.
 */
class CompositeIterator<T> implements Iterator<T> {

    private final Iterator<List<T>> collectionIt;
    private Iterator<T> subCollectionIt;

    public CompositeIterator(Iterator<List<T>> collection) {
        this.collectionIt = collection;
    }

    public void afterLine() {
        // override me
    }

    @Override
    public boolean hasNext() {
        if (subCollectionIt == null || !subCollectionIt.hasNext()) {
            return collectionIt.hasNext();
        }
        return true;
    }

    @Override
    public T next() {
        if (subCollectionIt == null || !subCollectionIt.hasNext()) {
            if (subCollectionIt != null) {
                afterLine();
            }
            List<T> nextLine = collectionIt.next();
            subCollectionIt = nextLine.iterator();
        }
        return subCollectionIt.next();
    }

    @Override
    public void remove() {

    }
}