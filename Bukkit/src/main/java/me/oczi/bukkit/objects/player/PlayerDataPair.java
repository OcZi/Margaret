package me.oczi.bukkit.objects.player;

import me.oczi.common.api.collections.PairIterator;
import me.oczi.common.api.collections.TypePair;
import me.oczi.common.storage.sql.dsl.result.SqlObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Date;
import java.util.Iterator;
import java.util.Map;

public class PlayerDataPair
    implements TypePair<PlayerData>,
    Comparable<PlayerDataPair> {
  private final Iterator<PlayerData> iterator;
  private PlayerData left;
  private PlayerData right;

  @Nullable
  private Date date;

  public static PlayerDataPair empty() {
    return new PlayerDataPair(null, null, null);
  }

  public static PlayerDataPair serializePartnership(Map<String, SqlObject> row,
                                                    @NotNull TypePair<String> namePair,
                                                    @Nullable PlayerDataPair pair) {
    if (pair == null) {
      pair = PlayerDataPair.empty();
    }
    PlayerData playerData = PlayerData.serialize(row);
    if (pair.getDate() == null) {
      pair.setDate(
          row.get("creation_date").getSqlDate());
    }
    int i = namePair.getSide(playerData.getUniqueId().toString());
    pair.setBySide(i, playerData);
    return pair;
  }

  public PlayerDataPair(PlayerData left,
                        PlayerData right,
                        @Nullable Date date) {
    this.left = left;
    this.right = right;
    this.date = date;
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

  public void setDate(@Nullable Date date) {
    this.date = date;
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

  @Nullable
  public Date getDate() {
    return date;
  }

  @Override
  public String toString() {
    return "PlayerDataPair{" +
        "iterator=" + iterator +
        ", left=" + left +
        ", right=" + right +
        '}';
  }

  @Override
  public int compareTo(@NotNull PlayerDataPair pair) {
    return date == null || pair.getDate() == null
        ? 0
        : date.compareTo(pair.getDate());
  }
}
