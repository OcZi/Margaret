package me.oczi.bukkit.objects.collections;

import com.google.common.collect.ForwardingList;
import me.oczi.bukkit.objects.Home;
import me.oczi.bukkit.utils.EmptyObjects;
import me.oczi.bukkit.utils.Partnerships;
import me.oczi.common.utils.CommonsUtils;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A Synchronized List with the database
 * for the Partner's home.
 */
public class HomeList extends ForwardingList<Home> {
  private final String partnerId;
  private final List<Home> homeList;

  private int maxHomes;

  public HomeList(String partnerId,
                  List<Home> homeList,
                  int maxHomes) {
    this.partnerId = partnerId;
    this.homeList = homeList;
    this.maxHomes = maxHomes;
  }

  public Home getFirstHome() {
    return homeList.get(0);
  }

  public Home getLastHome() {
    return homeList.get(homeList.size() - 1);
  }

  public Home getHome(int i) {
    return homeList.get(i);
  }

  @Override
  public Home get(int index) {
    return index <= -1 || homeList.size() <= index
        ? EmptyObjects.getEmptyHome()
        : super.get(index);
  }

  /**
   * Get home by alias.
   * @param alias Alias of home
   * @return A home, or a EmptyHome if not found.
   */
  public Home get(String alias) {
    if (!CommonsUtils.isNullOrEmpty(alias)) {
      for (Home home : homeList) {
        String homeAlias = home.getAlias();
        if (homeAlias.startsWith(alias)) {
          return home;
        }
      }
    }

    return EmptyObjects.getEmptyHome();
  }

  @Override
  public boolean add(@Nullable Home home) {
    if (home == null || home.isEmpty()) { return true; }
    boolean result = super.add(home);
    update();
    return result;
  }

  @Override
  public void add(int index, @Nullable Home home) {
    if (home == null || home.isEmpty()) { return; }
    super.add(index, home);
    update();
  }

  @Override
  public boolean addAll(int index,
                        Collection<? extends Home> elements) {
    boolean result = super.addAll(index, elements);
    update();
    return result;
  }

  @Override
  public Home set(int i,
                  @Nullable Home home) {
    if (home == null || home.isEmpty()) { return home; }
    Home set = super.set(i, home);
    update();
    return set;
  }

  @Override
  public boolean remove(@Nullable Object home) {
    if (!(home instanceof Home)) {
      return true;
    }
    boolean result = super.standardRemove(home);
    update();
    return result;
  }

  @Override
  public Home remove(int index) {
    Home result = super.remove(index);
    update();
    return result;
  }

  private void update() {
    Partnerships.updateHomeList(partnerId, this);
  }

  public void setMaxHomes(int maxHomes) {
    this.maxHomes = maxHomes;
  }

  @Override
  protected List<Home> delegate() {
    return homeList;
  }

  public List<String> getIds() {
    List<String> ids = new ArrayList<>();
    for (Home home : homeList) {
      ids.add(home.getId());
    }
    return ids;
  }

  public int getMaxHomes() {
    return maxHomes;
  }
}
