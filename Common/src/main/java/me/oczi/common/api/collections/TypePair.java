package me.oczi.common.api.collections;

import java.util.List;

public interface TypePair<T>
    extends Pair<T, T>, Iterable<T> {

  static <T> TypePair<T> of(T left, T right) {
    return new TypePairImpl<>(left, right);
  }

  static <T> TypePair<T> toPair(List<T> list) {
    if (list.isEmpty()) {
      return of(null, null);
    }
    T left = list.get(0);
    if (list.size() < 2) {
      return of(left, null);
    }
    T right = list.get(1);
    return of(left, right);
  }

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
