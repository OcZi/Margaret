package me.oczi.common.api.collections;

public interface TypePair<T>
    extends Pair<T, T>, Iterable<T> {

  boolean contains(T type);

  default int getSide(T type) {
    if (getLeft().equals(type)) {
      return 1;
    } else if (getRight().equals(type)) {
      return 2;
    } else {
      return 0;
    }
  }

  default T getBySide(int side) {
    if (side == 1) {
      return getLeft();
    } else if (side == 2) {
      return getLeft();
    } else {
      return null;
    }
  }

  default T findNotMatch(T type) {
    if (!getLeft().equals(type)) {
      return getLeft();
    } else if (!getRight().equals(type)) {
      return getRight();
    } else {
      return null;
    }
  }
}
