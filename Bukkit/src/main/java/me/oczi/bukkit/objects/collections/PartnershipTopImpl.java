package me.oczi.bukkit.objects.collections;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.google.common.collect.Lists;
import me.oczi.bukkit.internal.database.DbTasks;
import me.oczi.bukkit.objects.player.PlayerDataPair;
import me.oczi.bukkit.other.PartnershipTopWriter;
import me.oczi.common.api.collections.TypePair;
import me.oczi.common.storage.sql.dsl.result.ResultMap;
import me.oczi.common.storage.sql.dsl.result.SqlObject;
import me.oczi.common.utils.CommonsUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class PartnershipTopImpl
    implements PartnershipTop {
  private final LoadingCache<Integer, PlayerDataPair> entries;
  private final int ENTRIES_PER_PAGE;
  private final int MAX_ENTRIES;

  private final DbTasks dbTasks;
  private List<List<PlayerDataPair>> pages;

  public PartnershipTopImpl(int maxEntries,
                            int entriesPerPage,
                            long timeout,
                            DbTasks dbTasks) {
    this.MAX_ENTRIES = maxEntries;
    this.ENTRIES_PER_PAGE = entriesPerPage;
    this.entries = Caffeine.newBuilder()
        .expireAfterWrite(timeout, TimeUnit.SECONDS)
        .maximumSize(MAX_ENTRIES)
        .writer(new PartnershipTopWriter(this))
        .build(this::loader);
    this.dbTasks = dbTasks;
    renovate();
  }

  @Override
  public PlayerDataPair get(int index) {
    renovate();
    return entries.get(index);
  }

  @Override
  public List<PlayerDataPair> getPage(int numPage) {
    renovate();
    if (pages.size() <= numPage) {
      return pages.isEmpty()
          ? Collections.emptyList()
          : pages.get(pages.size() - 1);
    }
    return pages.get(numPage);
  }

  @Override
  public List<List<PlayerDataPair>> getPages() {
    renovate();
    return pages == null
        ? Collections.emptyList()
        : pages;
  }

  @Override
  public int getEntryStartedOfPage(int numPage) {
    if (CommonsUtils.isNullOrEmpty(pages)) {
      return 0;
    }
    if (pages.size() <= numPage) {
      numPage = pages.size() - 1;
    }

    return numPage > 1
        ? ENTRIES_PER_PAGE * (numPage) : 1;
  }

  @Override
  public int getMaxEntries() {
    return MAX_ENTRIES;
  }

  @Override
  public int getEntriesPerPage() {
    return ENTRIES_PER_PAGE;
  }

  private PlayerDataPair loader(int i) {
    renovate();
    return entries.getIfPresent(i);
  }

  private void renovate() {
    clear();
    if (isEmpty()) {
      List<PlayerDataPair> firstPartners = getFirstPartners(MAX_ENTRIES);
      if (!firstPartners.isEmpty()) {
        for (int i = 0; i < firstPartners.size(); i++) {
          entries.put(i + 1, firstPartners.get(i));
        }
        this.pages = CommonsUtils.partitionList(
            Lists.newArrayList(asMap().values()),
            ENTRIES_PER_PAGE);
      } else {
        this.pages = Collections.emptyList();
        entries.put(1, PlayerDataPair.empty());
      }
    }
  }

  /**
   * Query the first rows of Partner's data
   * by creation date.
   *
   * @param maxEntries The limit of rows to query.
   * @return Result of query.
   */
  private List<PlayerDataPair> getFirstPartners(int maxEntries) {
    ResultMap firstPartners = maxEntries > 0
        ? dbTasks.getTopOfPartnerships(maxEntries)
        : dbTasks.getAnythingOfPartnershipData();
    Map<String, TypePair<String>> namePairs = new HashMap<>();
    List<String> values = new ArrayList<>();
    for (Map<String, SqlObject> row : firstPartners.getRows()) {
      String player1 = row.get("player1").getString();
      String player2 = row.get("player2").getString();
      String id = row.get("id").getString();
      namePairs.put(id, TypePair.of(player1, player2));
      values.add(player1);
      values.add(player2);
    }
    if (CommonsUtils.isNullOrEmpty(values)) {
      return Collections.emptyList();
    }
    ResultMap result = dbTasks.getAllPlayerData(values);
    return createTopPartners(result.getRows(), namePairs);
  }

  /**
   * Create the Partner's top by a result of rows.
   *
   * @param rows      Rows of result.
   * @param namePairs Pairs of name to maintain order.
   * @return Map of top.
   */
  private List<PlayerDataPair> createTopPartners(List<Map<String, SqlObject>> rows,
                                                 Map<String, TypePair<String>> namePairs) {
    Map<String, PlayerDataPair> pairs = new HashMap<>();
    for (Map<String, SqlObject> row : rows) {
      String partnerid = row.get("partnerid").getString();
      pairs.compute(partnerid,
          (k, v) ->
              PlayerDataPair.serializePartnership(
                  row,
                  namePairs.get(k),
                  v)
      );
    }
    List<PlayerDataPair> list = Lists.newArrayList(pairs.values());
    Collections.sort(list);
    return list;
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
  public void clearPages() {
    this.pages = null;
  }

  @Override
  public ConcurrentMap<Integer, PlayerDataPair> asMap() {
    return entries.asMap();
  }
}
