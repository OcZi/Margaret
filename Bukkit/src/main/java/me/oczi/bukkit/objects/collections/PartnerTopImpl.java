package me.oczi.bukkit.objects.collections;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import me.oczi.bukkit.internal.database.DbTasks;
import me.oczi.bukkit.objects.player.PlayerData;
import me.oczi.bukkit.objects.player.PlayerDataPair;
import me.oczi.common.storage.sql.dsl.result.ResultMap;
import me.oczi.common.storage.sql.dsl.result.SqlObject;
import me.oczi.common.utils.CommonsUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class PartnerTopImpl
    implements PartnerTop {
  private final LoadingCache<Integer, PlayerDataPair> entries;
  private final int limitRows;

  private final DbTasks dbTasks;

  public PartnerTopImpl(int limitRows,
                        long timeout,
                        DbTasks dbTasks) {
    this.limitRows = limitRows;
    this.entries = Caffeine.newBuilder()
        .expireAfterWrite(timeout, TimeUnit.SECONDS)
        .maximumSize(limitRows)
        .build(this::loader);
    this.dbTasks = dbTasks;
    renovate();
  }

  @Override
  public PlayerDataPair get(int index) {
    return entries.get(index);
  }

  private PlayerDataPair loader(int i) {
    renovate();
    return entries.getIfPresent(i);
  }

  private void renovate() {
    clear();
    if (isEmpty()) {
      Map<Integer, PlayerDataPair> firstPartners = getFirstPartners(limitRows);
      if (!firstPartners.isEmpty()) {
        entries.putAll(firstPartners);
      }
    }
  }

  /**
   * Query the first rows of Partner's data
   * by creation date.
   * @param limitRows The limit of rows to query.
   * @return Result of query.
   */
  private Map<Integer, PlayerDataPair> getFirstPartners(int limitRows) {
    ResultMap firstPartners = dbTasks.getTopOfPartners(limitRows);
    List<String> params = new ArrayList<>();
    for (Map<String, SqlObject> row : firstPartners.getRows()) {
      params.add(
          row.get("player1").getString());
      params.add(
          row.get("player2").getString());
    }
    if (CommonsUtils.isNullOrEmpty(params)) {
      return Collections.emptyMap();
    }
    ResultMap result = dbTasks.getAllPlayerData(params);
    return createTopPartners(result.getRows());
  }

  /**
   * Create the Partner's top by a result of rows.
   * @param rows Rows of result.
   * @return Map of top.
   */
  private Map<Integer, PlayerDataPair> createTopPartners(List<Map<String, SqlObject>> rows) {
    Map<Integer, PlayerDataPair> pairs = new HashMap<>();
    List<String> ignored = new ArrayList<>();
    int i = 0;
    for (Map<String, SqlObject> row1 : rows) {
      String id1 = row1.get("id").getString();
      // Ignore it if has been loaded before.
      if (ignored.contains(id1)) {
        continue;
      }
      String partnerid = row1.get("partnerid").getString();
      for (Map<String, SqlObject> row2 : rows) {
        String id2 = row2.get("id").getString();
        if (id2.equals(id1)) {
          continue;
        }
        String partnerid2 = row2.get("partnerid").getString();
        if (partnerid2.equals(partnerid)) {
          i++;
          pairs.put(i, new PlayerDataPair(
              getPlayerDataFrom(row1),
              getPlayerDataFrom(row2)));
          // Partner of player in row2 has been loaded.
          // ignored it to avoid double iteration in row1.
          ignored.add(id2);
          break;
        }
      }
    }
    return pairs;
  }

  /**
   * Convert a row of Player's data to object.
   * @param row Row of Player's data table.
   * @return Player data object.
   */
  private PlayerData getPlayerDataFrom(Map<String, SqlObject> row) {
    String id = row.get("id").getString();
    String name = row.get("name").getString();
    String gender = row.get("gender").getString();

    return new PlayerData(UUID.fromString(id),
        name,
        null,
        gender);
  }

  @Override
  public int size() {
    return asMap().size();
  }

  @Override
  public boolean isEmpty() {
    return asMap().isEmpty();
  }

  @SuppressWarnings("RedundantCollectionOperation")
  @Override
  public boolean contains(Object o) {
    return asMap().values().contains(o);
  }

  @NotNull
  @Override
  public Iterator<PlayerDataPair> iterator() {
    renovate();
    return asMap().values().iterator();
  }

  @NotNull
  @Override
  public Object[] toArray() {
    return asMap().values().toArray();
  }

  @SuppressWarnings("SuspiciousToArrayCall")
  @NotNull
  @Override
  public <T> T[] toArray(@NotNull T[] ts) {
    return asMap().values().toArray(ts);
  }

  @Override
  public boolean add(PlayerDataPair playerData) {
    throw new UnsupportedOperationException("Partner's top cannot be mutated.");
  }

  @Override
  public boolean remove(Object o) {
    throw new UnsupportedOperationException("Partner's top cannot be mutated.");
  }

  @Override
  public boolean containsAll(@NotNull Collection<?> collection) {
    return asMap().values().containsAll(collection);
  }

  @Override
  public boolean addAll(@NotNull Collection<? extends PlayerDataPair> collection) {
    throw new UnsupportedOperationException("Partner's top cannot be mutated.");
  }

  @Override
  public boolean retainAll(@NotNull Collection<?> collection) {
    throw new UnsupportedOperationException("Partner's top cannot be mutated.");
  }

  @Override
  public boolean removeAll(@NotNull Collection<?> collection) {
    throw new UnsupportedOperationException("Partner's top cannot be mutated.");
  }

  @Override
  public void clear() {
    entries.cleanUp();
  }

  @Override
  public ConcurrentMap<Integer, PlayerDataPair> asMap() {
    return entries.asMap();
  }
}
