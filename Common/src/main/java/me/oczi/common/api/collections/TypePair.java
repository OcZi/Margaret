package me.oczi.common.api.collections;

public interface TypePair<T>
    extends Pair<T, T>, Iterable<T> {

  boolean contains(T type);

  T findNotMatch(T type);
}
