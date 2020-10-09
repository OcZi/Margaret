package me.oczi.common.api.collections;

import me.oczi.common.api.Emptyble;

public interface Pair<L, R> extends Emptyble {

  L getLeft();

  R getRight();

  @Override
  default boolean isEmpty() {
    return getLeft() == null && getRight() == null;
  }
}
