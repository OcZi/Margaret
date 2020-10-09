package me.oczi.bukkit.objects.player;

import com.google.common.collect.Lists;
import me.oczi.bukkit.utils.EmptyObjects;
import me.oczi.common.api.collections.TypePair;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class MargaretPlayerTypePair
    implements TypePair<MargaretPlayer> {
  private final Iterator<MargaretPlayer> iterator;
  private final MargaretPlayer left;
  private final MargaretPlayer right;

  public MargaretPlayerTypePair(MargaretPlayer left,
                                MargaretPlayer right) {
    this.left = left;
    this.right = right;
    this.iterator = Lists.newArrayList(left, right).iterator();
  }

  @Override
  public MargaretPlayer getLeft() {
    return getOrEmpty(left);
  }

  @Override
  public MargaretPlayer getRight() {
    return getOrEmpty(right);
  }

  public MargaretPlayer getOrEmpty(MargaretPlayer margaretPlayer) {
    return margaretPlayer == null
        ? EmptyObjects.getEmptyMargaretPlayer()
        : margaretPlayer;
  }

  @Override
  public boolean contains(MargaretPlayer type) {
    return left == type || right == type;
  }

  @Override
  public MargaretPlayer findNotMatch(MargaretPlayer type) {
    if (type == left) {
      return left;
    } else if (type == right) {
      return right;
    }
    return EmptyObjects.getEmptyMargaretPlayer();
  }

  @NotNull
  @Override
  public Iterator<MargaretPlayer> iterator() {
    return iterator;
  }

  @Override
  public String toString() {
    return "MargaretPlayerTypePair{" +
        "iterator=" + iterator +
        ", left=" + left +
        ", right=" + right +
        '}';
  }
}
