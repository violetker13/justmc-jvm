/*
 * Copyright (c) 1997, 2023, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package override.java.util;

public interface Collection<E> extends Iterable<E> {

    Object[] toArray();

    int size();

    boolean isEmpty();

    boolean contains(Object o);

    Iterator<E> iterator();

    // Modification Operations

    boolean add(E e);

    boolean remove(Object o);

    // Bulk Operations

    boolean containsAll(Collection<?> c);

    boolean addAll(Collection<? extends E> c);

    boolean removeAll(Collection<?> c);

    boolean retainAll(Collection<?> c);

    void clear();

}
