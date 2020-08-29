package me.oczi.bukkit.utils;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import me.oczi.bukkit.MargaretMain;
import me.oczi.bukkit.PluginCore;
import me.oczi.bukkit.events.PartnerEndEvent;
import me.oczi.bukkit.events.PartnerStartEvent;
import me.oczi.bukkit.internal.CooldownManager;
import me.oczi.bukkit.internal.MemoryManager;
import me.oczi.bukkit.internal.database.DbTasks;
import me.oczi.bukkit.internal.objectcycle.partner.PartnerObjectLoader;
import me.oczi.bukkit.objects.Home;
import me.oczi.bukkit.objects.PartnerHome;
import me.oczi.bukkit.objects.collections.HomeList;
import me.oczi.bukkit.objects.collections.PartnerPermissionSet;
import me.oczi.bukkit.objects.id.HomeID;
import me.oczi.bukkit.objects.id.ID;
import me.oczi.bukkit.objects.partner.Partner;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.objects.player.MargaretPlayerTypePair;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.other.exceptions.IdCollisionException;
import me.oczi.bukkit.storage.yaml.MargaretYamlStorage;
import me.oczi.common.api.TypePair;
import me.oczi.common.api.TypePairImpl;
import me.oczi.common.storage.sql.orm.result.ResultMap;
import me.oczi.common.utils.BitMasks;
import me.oczi.common.utils.CommonsUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static me.oczi.bukkit.utils.CommandPreconditions.*;
import static me.oczi.bukkit.utils.MargaretPlayers.cleanUpProposals;
import static me.oczi.bukkit.utils.MargaretPlayers.getAsMargaretPlayer;

public final class Partners {
  private static final PluginCore core = MargaretMain.getCore();
  private static final MemoryManager memoryManager = core.getMemoryManager();
  private static final DbTasks dbTasks = core.getDatabaseTask();

  private static final PartnerObjectLoader loader = core
      .getObjectCycleManager()
      .getPartnerLoader();

  /**
   * Load a partner into {@link MemoryManager}.
   * @param id ID to load from the database.
   */
  public static void loadPartner(String id) {
    loader.load(id);
  }

  /**
   * Close {@link Partner} session.
   * @param partner Partner to close session.
   */
  public static void closePartner(Partner partner) {
    closePartner(partner.getId());
  }

  /**
   * Close {@link Partner} session.
   * @param id ID of Partner.
   */
  public static void closePartner(String id) {
    loader.close(id);
  }

  /**
   * Create a new partner and load it
   * into {@link MemoryManager}.
   * @param margaretPlayer1 Player 1.
   * @param margaretPlayer2 Player 2.
   */
  public static void newPartner(MargaretPlayer margaretPlayer1,
                                MargaretPlayer margaretPlayer2) {
    loader.newPartner(margaretPlayer1, margaretPlayer2);
    announcePartner(Messages.PARTNER_STARTED,
        margaretPlayer1.getName(),
        margaretPlayer2.getName());
  }

  /**
   * Reload all the permissions of partner.
   * @param player Player that calls this method.
   * @param partner Partner to reload permissions.
   * @param update Update in database.
   */
  public static void reloadPartnerPermission(Player player,
                                             Partner partner,
                                             boolean update) {
    PartnerPermissionSet permissions = partner.getPermissions();
    int oldBits = permissions.getBits();
    int newBits = transformPermissions(
        oldBits,
        player);
    if (oldBits != newBits) {
      permissions.setBits(newBits);
      if (update) {
        updatePartnerPermission(partner);
      }
    }
  }

  /**
   * Transform player's permissions
   * into Partner's permissions in bits.
   * @param bits Starter bits.
   * @param players Players to get their permissions.
   * @return Bits + Player's permission in bits.
   */
  public static int transformPermissions(int bits, Player... players) {
    return transformPermissions(bits, Arrays.asList(players));
  }

  /**
   * Transform player's permissions
   * into Partner's permissions in bits.
   * @param bits Starter bits.
   * @param players Players to get their permissions.
   * @return Bits + Player's permission in bits.
   */
  public static int transformPermissions(int bits, Iterable<Player> players) {
    Map<String, PartnerPermission> permissions = PartnerPermission.getPermissions();
    List<Integer> sumBits = new ArrayList<>();
    for (@NotNull Player player : players) {
      for (Map.Entry<String, PartnerPermission> perm : permissions.entrySet()) {
        if (player.hasPermission(perm.getKey())) {
          sumBits.add(BitMasks.mask(perm.getValue()));
        }
      }
    }
    return BitMasks.sumBits(bits, sumBits);
  }

  /**
   * Update the Partner's permissions of Partner in database.
   * @param partner Partner to update.
   */
  public static void updatePartnerPermission(Partner partner) {
    int bits = partner.getPermissions().getBits();
    dbTasks.updatePartnerProperty(
        "bitpermissions", bits, partner.getId());
  }

  /**
   * Create a new Home and put into the database.
   * @param partnerId Partner ID.
   * @param alias Alias of home.
   * @param location Location of Home.
   * @return Home created.
   */
  public static Home newHome(String partnerId,
                             @Nullable String alias,
                             Location location) {
    String id = foundUnusedHomeId();
    alias = CommonsUtils.isNullOrEmpty(alias) ||
            alias.length() > 20
        ? "unknown-" + id
        : alias;
    Home home = new PartnerHome(id, alias, location);
    dbTasks.setupPartnerHome(partnerId, home);
    return home;
  }

  /**
   * Delete a home from the HomeList and database.
   * @param homeList HomeList.
   * @param home Home to delete.
   */
  public static void deleteHome(HomeList homeList, Home home) {
    dbTasks.deletePartnerHome(home.getId());
    homeList.remove(home);
  }

  /**
   * Delete home of {@link HomeList} in database.
   * @param homeList HomeList.
   * @param i Slot of Home to delete.
   */
  public static void deleteHomeAndUpdate(HomeList homeList, int i) {
    homeList.remove(i);
  }

  /**
   * Update {@link HomeList} of partner in database.
   * @param id ID of partner.
   * @param homeList HomeList to update in database.
   */
  public static void updateHomeList(String id, HomeList homeList) {
    List<Object> params = Lists.newArrayList(id);
    params.addAll(homeList.getIds());
    int maxPossibleHomes = MargaretYamlStorage.getMaxPossibleHomes();
    for (int i = homeList.size(); i < maxPossibleHomes; i++) {
      params.add(null);
    }
    dbTasks.setHomeList(id, params);
  }

  /**
   * Set relation of partner and update it in database.
   * @param partner Partner to update.
   * @param relation Relation to set.
   */
  public static void setRelation(Partner partner, String relation) {
    partner.setRelation(relation);
    dbTasks.updatePartnerData(
        "relation", relation, partner.getId());
  }

  /**
   * Clear proposals of every player in partner and set.
   * @param partner Partner.
   */
  public static void clearAndSetPartner(Partner partner,
                                        MargaretPlayer margaretPlayer1,
                                        MargaretPlayer margaretPlayer2) {
    cleanUpProposals(margaretPlayer1, margaretPlayer2);
    setPartner(partner, margaretPlayer1, margaretPlayer2);
  }

  /**
   * Set partner of {@link MargaretPlayer}s.
   * @param partner Partner to set.
   * @param margaretPlayer1 MargaretPlayer 1.
   * @param margaretPlayer2 MargaretPlayer 2.
   */
  public static void setPartner(Partner partner,
                                MargaretPlayer margaretPlayer1,
                                MargaretPlayer margaretPlayer2) {
    dbTasks.updateDoublePlayerData("partnerid",
        partner.getId(),
        margaretPlayer1.getUniqueId(),
        margaretPlayer2.getUniqueId());

    margaretPlayer1.setPartner(partner);
    margaretPlayer2.setPartner(partner);
  }

  /**
   * Get {@link Partner} by partner ID.
   * @param partnerId ID of partner.
   * @return Partner, or EmptyPartner if not exist.
   */
  public static Partner getAsPartner(String partnerId) {
    return core.getPartner(partnerId);
  }

  /**
   * Found UUID of partner.
   * @param margaretPlayer Player.
   * @throws IllegalStateException If not found a opposite UUID.
   * @return UUID of partner.
   */
  public static UUID foundUuidOfPartner(MargaretPlayer margaretPlayer) {
    if (!margaretPlayer.havePartner()) {
      return margaretPlayer.getUniqueId();
    }

    Partner partner = margaretPlayer.getPartner();
    return getUuidOfPartner(
        margaretPlayer.getUniqueId(),
        partner.getUuid1(),
        partner.getUuid2());
  }

  /**
   * Get the UUID of the partner.
   * @param uuidNotMatch UUID of the player that calls the method.
   * @param uuid1 UUID 1 to pair.
   * @param uuid2 UUID 2 to pair.
   * @return UUID of partner.
   */
  public static UUID getUuidOfPartner(UUID uuidNotMatch,
                                      UUID uuid1,
                                      UUID uuid2) {
    return getUuidOfPartner(uuidNotMatch,
        new TypePairImpl<>(uuid1, uuid2));
  }

  /**
   * Get the UUID of the partner.
   * @param uuidNotMatch UUID of the player that calls the method.
   * @param uuids Pair of uuids.
   * @return UUID of partner.
   */
  public static UUID getUuidOfPartner(UUID uuidNotMatch,
                                      TypePair<UUID> uuids) {
    UUID resultUuid = uuids.findNotMatch(uuidNotMatch);

    if (resultUuid == null) {
      throw new IllegalStateException(
          "Cannot found the other uuid of partner " +
              "(Maybe all the uuids are the same?)");
    }
    return resultUuid;
  }

  /**
   * Found the partner of MargaretPlayer as {@link Player}.
   * @param margaretPlayer Player.
   * @return Partner As Player.
   */
  public static Player foundPartnerAsPlayer(MargaretPlayer margaretPlayer) {
    return Bukkit.getPlayer(
        foundUuidOfPartner(margaretPlayer));
  }

  /**
   * Found the partner of MargaretPlayer as another {@link MargaretPlayer}.
   * @param margaretPlayer Player.
   * @return Partner As MargaretPlayer.
   */
  public static MargaretPlayer foundPartnerAsMargaretPlayer(
      MargaretPlayer margaretPlayer) {
    return MargaretPlayers.getAsMargaretPlayer(
        foundUuidOfPartner(margaretPlayer));
  }

  /**
   * Get name of partner.
   * @param margaretPlayer Player to get Partner.
   * @return Name of partner.
   */
  public static String getNameOfPartner(MargaretPlayer margaretPlayer) {
    return getNameOfPartner(margaretPlayer, false);
  }

  /**
   * Get name of partner.
   * @param margaretPlayer Player to get Partner.
   * @param colorized Option to colorize name with gender's color.
   * @return Name of partner.
   */
  public static String getNameOfPartner(MargaretPlayer margaretPlayer,
                                        boolean colorized) {
    if (!margaretPlayer.havePartner()) {
      return MessageUtils.getMessageTranslated(Messages.NONE);
    }
    UUID partnerUuid = Partners.foundUuidOfPartner(margaretPlayer);
    return MargaretPlayers.getNameOfProfile(partnerUuid, colorized);
  }

  /**
   * Found a unused Home-ID.
   * @throws IdCollisionException throw if not found a id unused after 5 tries.
   * @return true if founded.
   */
  public static String foundUnusedHomeId() {
    ID id = new HomeID();
    int trying = 0;
    while (true) {
      if (trying >= 5) {
        throw new IdCollisionException(
            "Too much collisions of Home ID: " + id.getID());
      }
      if (!dbTasks.partnerHomeExist(id.getID())) {
        break;
      } else {
        trying++;
      }
    }
    return id.getID();
  }

  /**
   * End a partnership.
   * @param partner Partnership to end.
   */
  public static void endPartner(Partner partner) {
    if (callPartnerEndEvent(partner)) {
      return;
    }
    UUID uuid1 = partner.getUuid1();
    UUID uuid2 = partner.getUuid2();

    MargaretPlayer player1 = getAsMargaretPlayer(uuid1);
    MargaretPlayer player2 = getAsMargaretPlayer(uuid2);
    player1.clearPartner();
    player2.clearPartner();

    memoryManager.getPersistenceCache()
        .removePartner(partner.getId());
    endPartnerInternal(partner.getId(),
        uuid1.toString(),
        uuid2.toString());

    // Add ChatColor.RESET to avoid override colors
    String playerName1 = MargaretPlayers
        .getNameOfProfile(uuid1, true)
        + ChatColor.RESET;
    String playerName2 = MargaretPlayers
        .getNameOfProfile(uuid2, true)
        + ChatColor.RESET;
    if (MargaretYamlStorage.isAnnouncePartner()) {
      MessageUtils.broadcast(Messages.PARTNER_ENDED, false,
          playerName1, playerName2);
    }
    // Set cooldown to send proposals again.
    CooldownManager cooldownPlayer = core.getCooldownManager();
    cooldownPlayer.setProposalCooldown(uuid1);
    cooldownPlayer.setProposalCooldown(uuid2);
  }

  /**
   * Force a end of partnership.
   * @param id ID of partner.
   */
  public static void forceEndPartner(String id) {
    ResultMap partnerData = dbTasks.getPartnerData(id);
    String uuid1 = partnerData.get("uuid1").getString();
    String uuid2 = partnerData.get("uuid2").getString();
    endPartnerInternal(id, uuid1, uuid2);
  }

  /**
   * End Partner method internal.
   * @param id ID of Partner.
   * @param uuid1 UUID 1 of partner.
   * @param uuid2 UUID 2 of partner.
   */
  private static void endPartnerInternal(String id,
                                         Object uuid1,
                                         Object uuid2) {
    dbTasks.updateDoublePlayerData("partnerid",
        EmptyObjects.getEmptyPartnerId(), uuid1, uuid2);

    dbTasks.deletePartnerData(id);
    dbTasks.deletePartnerProperties(id);
    dbTasks.deletePartnerHomeList(id);
  }

  /**
   * Send gift between the {@link MargaretPlayer}s of partner.
   * @param margaretPlayer MargaretPlayer sender.
   * @throws ConditionException If gift is not valid.
   */
  public static void sendGift(MargaretPlayer margaretPlayer)
      throws ConditionException {
    Player player1 = MargaretPlayers.getAsPlayer(margaretPlayer);

    UUID uuid = foundUuidOfPartner(margaretPlayer);
    checkPartnerOnline(uuid);
    checkPlayerInventory(player1);
    Player player2 = Bukkit.getPlayer(uuid);

    PlayerInventory inventorySender = player1.getInventory();
    PlayerInventory inventoryReceiver = player2.getInventory();
    ItemStack gift = inventorySender.getItemInHand();
    checkItem(gift);

    inventorySender.remove(gift);
    inventoryReceiver.addItem(gift);
    String itemName = BukkitUtils.getNameOfItem(gift);
    if (CommonsUtils.isNullOrEmpty(itemName)) {
      itemName = MessageUtils.getMessageTranslated(Messages.ERROR_FATAL);
    }
    MessageUtils.compose(player1,
        Messages.GIFT_SENT,
        true,
        itemName);
    MessageUtils.compose(player2,
        Messages.GIFT_RECEIVED,
        true,
        itemName);
  }

  /**
   * Send health between of {@link Player}s of partner.
   * @param player1 Player sender.
   * @param player2 Player receiver.
   * @param healthSend Health to send.
   * @throws ConditionException If health is invalid, Player 1 or 2 health is invalid or insufficient.
   */
  public static void sendHealth(Player player1,
                                Player player2,
                                int healthSend)
      throws ConditionException {
    checkInt(healthSend,
        0,
        Messages.INVALID_HEALTH,
        healthSend);
    double health1 = player1.getHealth();
    checkHealth(healthSend, (int) health1);

    checkIsAlive(player2, Messages.PARTNER_DEAD);
    double health2 = player2.getHealth();
    health1 -= healthSend;
    health2 += healthSend;
    checkGreaterOrEquals((int) health1,
        20,
        Messages.INVALID_HEALTH,
        health1);
    checkGreaterOrEquals((int) health2,
        20,
        Messages.INVALID_HEALTH,
        health2);
    player1.setHealth(health1 - healthSend);
    player2.setHealth(health2 + healthSend);

    MessageUtils.compose(player1,
        Messages.HEALTH_SENT,
        true,
        healthSend);
    MessageUtils.compose(player2,
        Messages.HEALTH_RECEIVED,
        true,
        healthSend);
  }

  /**
   * Add permission to a {@link Partner}.
   * @param id ID of Partner.
   * @param permissionSet PermissionSet of Partner.
   * @param permission Permission to add.
   */
  public static void addPermission(String id,
                                   PartnerPermissionSet permissionSet,
                                   PartnerPermission permission) {
    permissionSet.add(permission);
    dbTasks.updatePartnerProperty("bitpermissions",
        permissionSet.getBits(),
        id);
  }

  /**
   * remove permission to a {@link Partner}.
   * @param id ID of Partner.
   * @param permissionSet PermissionSet of Partner.
   * @param permission Permission to remove.
   */
  public static void removePermission(String id,
                                      PartnerPermissionSet permissionSet,
                                      PartnerPermission permission) {
    permissionSet.remove(permission);
    dbTasks.updatePartnerProperty("bitpermissions",
        permissionSet.getBits(),
        id);
  }

  /**
   * Announce a new {@link Partner}.
   * @param message Message to send.
   * @param name1 Player's name 1.
   * @param name2 Player's name 2.
   */
  public static void announcePartner(Messages message,
                                     String name1,
                                     String name2) {
    if (MargaretYamlStorage.isAnnouncePartner()) {
      MessageUtils.broadcast(message, false, name1, name2);
    }
  }

  /**
   * Call the event of Partner start.
   * @param margaretPlayer1 MargaretPlayer 1
   * @param margaretPlayer2 MargaretPlayer 2
   * @return Event is cancelled.
   */
  public static boolean callPartnerStartEvent(MargaretPlayer margaretPlayer1,
                                              MargaretPlayer margaretPlayer2) {
    return BukkitUtils.callEventAndGetResult
        (new PartnerStartEvent(margaretPlayer1, margaretPlayer2));
  }

  /**
   * Call the event of Partner end.
   * @param partner partner to end.
   * @return Event is cancelled.
   */
  public static boolean callPartnerEndEvent(Partner partner) {
    return BukkitUtils.callEventAndGetResult
        (new PartnerEndEvent(partner));
  }

  public static void checkPartner(@NotNull Partner partner) {
    checkPartner(partner,
        "Partner is null (Bad initialization?)");
  }

  public static void checkPartner(@NotNull Partner partner,
                                  String errMessage) {
    if (partner == EmptyObjects.getEmptyPartner())
      throw new NullPointerException(errMessage);
  }

  public static void checkPartnerId(String id) {
    if (Strings.isNullOrEmpty(id)) {
      throw new NullPointerException
          ("Partner ID is null or empty (Bad initialization?)");
    }

    if (id.equals(EmptyObjects.getEmptyPartnerId())) {
      throw new NullPointerException
          ("Partner ID is invalid (Bad initialization?)");
    }
  }
}
