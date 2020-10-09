package me.oczi.bukkit.objects.player;

import me.oczi.common.api.collections.PairIterator;
import me.oczi.common.api.collections.TypePair;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class PlayerDataPair implements TypePair<PlayerData> {
  private final Iterator<PlayerData> iterator;
  private PlayerData left;
  private PlayerData right;

  public PlayerDataPair(PlayerData left,
                        PlayerData right) {
    this.left = left;
    this.right = right;
    this.iterator = new PairIterator<>(left, right);
  }

  @Override
  public boolean contains(PlayerData type) {
    return type == left || type == right;
  }

  @Override
  public PlayerData findNotMatch(PlayerData type) {
    if (left != type) {
      return left;
    } else if (right != type) {
      return right;
    } else {
      return null;
    }
  }

  public void setLeft(PlayerData left) {
    this.left = left;
  }

  public void setRight(PlayerData right) {
    this.right = right;
  }

  public void setBySide(int side,  PlayerData type) {
    if (side == 1) {
      setLeft(type);
    } else if (side == 2) {
      setRight(type);
    }
  }

  public boolean isFull() {
    return right != null && left != null;
  }

  @NotNull
  @Override
  public Iterator<PlayerData> iterator() {
    return iterator;
  }

  @Override
  public PlayerData getLeft() {
    return left;
  }

  @Override
  public PlayerData getRight() {
    return right;
  }

  @Override
  public String toString() {
    return "PlayerDataPair{" +
        "iterator=" + iterator +
        ", left=" + left +
        ", right=" + right +
        '}';
  }
}
