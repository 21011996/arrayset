package ru.ifmo.ctddev.kachalskiy.arrayset;

import java.util.*;

/**
 * Created by Илья on 25.02.2015.
 */
public class ArraySet<E> extends AbstractSet<E> implements SortedSet<E> {
    protected final Comparator<? super E> comparator;
    protected final List<E> array;
    protected boolean defaultComparator;

    public ArraySet() {
        comparator = null;
        array = new ArrayList<>();
    }

    public ArraySet(Collection<E> collection, Comparator<? super E> comparator) {
        List<E> tmp = new ArrayList<>(collection);
        this.comparator = comparator;
        Collections.sort(tmp, comparator);
        List<E> uniqueSet = new ArrayList<>();
        if (tmp.size() > 0) {
            uniqueSet.add(tmp.get(0));
        }
        for (int i = 1; i < tmp.size(); ++i) {
            if (comparator.compare(uniqueSet.get(uniqueSet.size() - 1),
                    tmp.get(i)) != 0) {
                uniqueSet.add(tmp.get(i));
            }
        }
        array = new ArrayList<>(uniqueSet);
        defaultComparator = false;
    }

    protected ArraySet(List<E> collection, Comparator<? super E> comparator,
                       boolean defaultComparator) {
        this.array = collection;
        this.comparator = comparator;
        this.defaultComparator = defaultComparator;
    }

    public ArraySet(Collection<E> c) {
        this(c, new Comparator<E>() {
            @SuppressWarnings("unchecked")
            public int compare(E o1, E o2) {
                return ((Comparable<? super E>) o1).compareTo(o2);
            }
        });
        defaultComparator = true;
    }

    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        return Collections.binarySearch(array, (E) o, comparator) >= 0;
    }

    public Iterator<E> iterator() {
        return new Iterator<E>() {
            Iterator<E> it = array.iterator();

            public boolean hasNext() {
                return it.hasNext();
            }

            public E next() {
                return it.next();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public int size() {
        return array.size();
    }

    public Comparator<? super E> comparator() {
        return defaultComparator ? null : comparator;
    }

    public SortedSet<E> subSet(E fromElement, E toElement) {
        return tailSet(fromElement).headSet(toElement);
    }

    public SortedSet<E> headSet(E toElement) {
        int index = Collections.binarySearch(array, toElement, comparator);
        if (index < 0) {
            index = -index - 1;
        }
        return new ArraySet<E>(array.subList(0, index), comparator,
                defaultComparator);
    }

    public SortedSet<E> tailSet(E fromElement) {
        int index = Collections.binarySearch(array, fromElement, comparator);
        if (index < 0) {
            index = -index - 1;
        }
        return new ArraySet<E>(array.subList(index, array.size()), comparator,
                defaultComparator);
    }

    public E first() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return array.get(0);
    }

    public E last() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return array.get(size() - 1);
    }
}
