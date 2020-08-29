package me.oczi.common.api;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class TypePairImpl<T> implements TypePair<T> {
  private final Iterator<T> iterator;
  private final T left;
  private final T right;

  public TypePairImpl(T left, T right) {
    this.right = right;
    this.left = left;
    this.iterator = new PairIterator<>(left, right);
  }

  @Override
  public T getLeft() {
    return left;
  }

  @Override
  public T getRight() {
    return right;
  }

  @Override
  public boolean contains(T type) {
    return type.equals(left) || type.equals(right);
  }

  @Override
  public T findNotMatch(T type) {
    if (!type.equals(left)) {
      return left;
    } else if (!type.equals(right)) {
      return right;
    } else {
      return null;
    }
  }

  @NotNull
  @Override
  public Iterator<T> iterator() {
    return iterator;
  }
}
