package me.oczi.common.utils;

import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Set;

public interface BitMasks {

  static <E extends Enum<E>> int subEnum(int bit,
                                         @NotNull E enumeration) {
    int bitSum = mask(enumeration);
    if (bitEquals(bit, bitSum)) {
      return subBits(bit, bitSum);
    }

    return bit;
  }

  static <E extends Enum<E>> int sumEnum(int bit,
                                         @NotNull E enumeration) {
    int bitSum = mask(enumeration);
    if (!bitEquals(bit, bitSum)) {
      return sumBits(bit, bitSum);
    }

    return bit;
  }

  static <E extends Enum<E>> int sumEnumClass(int bit,
                                              @NotNull Class<E> enums) {
    Set<E> enumSet = EnumSet.allOf(enums);
    for (E sum : enumSet) {
      sumEnum(bit, sum);
    }
    return bit;
  }

  static <E extends Enum<E>> int subEnumClass(int bit,
                                              @NotNull Class<E> enums) {
    Set<E> enumSet = EnumSet.allOf(enums);
    for (E sum : enumSet)  {
      subEnum(bit, sum);
    }
    return bit;
  }

  static int sumBits(int bit,
                     @NotNull int... bitsToSum) {
    for (int sum : bitsToSum) bit |= sum;
    return bit;
  }

  static int sumBits(int bit,
                     @NotNull Iterable<Integer> bitsToSum) {
    for (int sum : bitsToSum) bit |= sum;
    return bit;
  }

  static int subBits(int bit,
                     @NotNull int... bitsToSub) {
    for (long sum : bitsToSub) bit &= ~sum;
    return bit;
  }

  static int subBits(int bit,
                     @NotNull Iterable<Integer> bitsToSub) {
    for (long sum : bitsToSub) bit &= ~sum;
    return bit;
  }

  static boolean bitEquals(int bit, int mask) {
    return (bit & mask) == mask;
  }

  static <E extends Enum<E>> int mask(E num) {
    return 1 << (num.ordinal() + 1);
  }
}
