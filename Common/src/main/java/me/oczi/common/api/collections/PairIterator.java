package me.oczi.common.api.collections;

import java.util.Iterator;

public class PairIterator<T> implements Iterator<T> {
  private T head;
  private T tail;

  public PairIterator(TypePair<T> ts) {
    this(ts.getLeft(), ts.getRight());
  }

  public PairIterator(T left, T right) {
    this.head = left;
    this.tail = right;
  }

  @Override
  public boolean hasNext() {
    return head != null;
  }

  @Override
  public T next() {
    T next = head;
    this.head = tail;
    this.tail = null;
    return next;
  }
}
