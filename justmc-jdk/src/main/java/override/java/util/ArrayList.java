package override.java.util;

import justmc.Unsafe;

public class ArrayList<E> implements List<E> {

    Object[] elementData;

    public ArrayList() {
        this.elementData = new Object[0];
    }

    @Override
    public Object[] toArray() {
        return elementData;
    }

    @Override
    public int size() {
        return elementData.length;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public boolean add(E e) {
        return true;
    }

    @Override
    public boolean remove(Object o) {
        int i = indexOf(o);
        if (i > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return true;
    }

    @Override
    public boolean removeAll(override.java.util.Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(override.java.util.Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        elementData = new Object[0];
    }

    public int indexOf(Object o) {
        return 0;
    }

    public E get(int index) {
        return Unsafe.cast(elementData[index]);
    }
}
