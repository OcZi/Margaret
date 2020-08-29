package me.oczi.common.api;

public interface TypePair<T>
    extends Pair<T, T>, Iterable<T> {

  boolean contains(T type);

  T findNotMatch(T type);
}
