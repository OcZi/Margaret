package me.oczi.bukkit.utils;

import me.oczi.bukkit.objects.Proposal;
import me.oczi.bukkit.objects.collections.HomeList;
import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.utils.settings.EnumSetting;
import me.oczi.common.api.Emptyble;
import me.oczi.common.utils.CommonsUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

/**
 * Preconditions for commands.
 */
public interface CommandPreconditions {


  /**
   * Throw a {@link ConditionException} if the predicate is true.
   *
   * @param t         Type that have the predicate.
   * @param predicate Predicate.
   * @param message   Message to send with the exception.
   * @param objects   Objects to format message.
   * @param <T>       Type that have the predicate.
   * @throws ConditionException If predicate is true or parameter t is null.
   */
  static <T> void throwIf(@Nullable T t,
                          Predicate<T> predicate,
                          Messages message,
                          Object... objects)
      throws ConditionException {
    if (t == null || predicate.test(t)) {
      throw ConditionException.newException(message, objects);
    }
  }

  static void checkHealth(int i1, int i2)
      throws ConditionException {
    throwIf(i1,
        i -> i >= i2,
        Messages.INSUFFICIENT_HEALTH);
  }

  static void checkInt(int i1,
                       int i2,
                       Messages message,
                       Object... objects)
      throws ConditionException {
    throwIf(i1,
        i -> i == i2,
        message,
        objects);
  }

  static void checkIsAlive(Player player, Messages message)
      throws ConditionException {
    throwIf(player.getHealth(),
        i -> i <= 0,
        message);
  }

  static void checkGender(String arg)
      throws ConditionException {
    throwIf(arg,
        g -> !Genders.genderExist(g),
        Messages.INVALID_GENDER, arg);
  }

  static void checkPlayerOnline(Player player)
      throws ConditionException {
    throwIf(player,
        Objects::isNull,
        Messages.PLAYER_OFFLINE);
  }

  static void checkPlayerOnline(Player player,
                                String playerName)
      throws ConditionException {
    throwIf(player,
        Objects::isNull,
        Messages.PLAYER_ARG_OFFLINE,
        playerName);
  }

  static void checkPlayerGamemode(Player player, GameMode requiredMode)
      throws ConditionException {
    GameMode playerMode = player.getGameMode();
    throwIf(playerMode,
        gameMode -> gameMode != requiredMode,
        Messages.INVALID_GAMEMODE,
        playerMode.toString().toLowerCase());
  }

  static void checkMargaretPlayerOnline(MargaretPlayer margaretPlayer)
      throws ConditionException {
    checkIsEmpty(margaretPlayer,
        Messages.PLAYER_OFFLINE);
  }

  static void checkMargaretPlayerOnline(MargaretPlayer margaretPlayer,
                                        String playerName)
      throws ConditionException {
    checkIsEmpty(margaretPlayer,
        Messages.PLAYER_ARG_OFFLINE,
        playerName);
  }

  static void checkProposal(Proposal proposal,
                            Messages message,
                            Object... objects)
      throws ConditionException {
    checkIsEmpty(proposal, message, objects);
  }

  static void checkIsEmpty(Emptyble emptyble,
                           Messages message,
                           Object... objects)
      throws ConditionException {
    throwIf(emptyble,
        Emptyble::isEmpty,
        message,
        objects);
  }

  static void checkProposalList(MargaretPlayer margaretPlayer1,
                                MargaretPlayer margaretPlayer2)
      throws ConditionException {
    throwIf(margaretPlayer2,
        m -> !(margaretPlayer1.containsProposalOf(m)),
        Messages.NOT_IN_PROPOSAL_LIST,
        margaretPlayer2.getName());
  }

  static void checkCurrentProposal(MargaretPlayer margaretPlayer1)
      throws ConditionException {
    Proposal proposal = margaretPlayer1.getCurrentProposal();
    throwIf(proposal,
        prop -> !prop.isEmpty(),
        Messages.PROPOSAL_ALREADY_SENT,
        proposal.getReceiver().getName());
  }

  static void checkSetting(MargaretPlayer margaretPlayer,
                           EnumSetting enumSetting,
                           Messages message,
                           Object... objects)
      throws ConditionException {
    throwIf(margaretPlayer.isSetting(enumSetting),
        setting -> !setting,
        message,
        objects);
  }

  static void checkInstanceOfPlayer(CommandSender sender)
      throws ConditionException {
    checkInstanceOfPlayer(sender, Messages.ONLY_PLAYER);
  }

  static void checkInstanceOfPlayer(CommandSender sender,
                                    Messages message)
      throws ConditionException {
    throwIf(sender,
        s -> !(s instanceof Player),
        message);
  }

  static void checkPlayerPermission(CommandSender player, String permission)
      throws ConditionException {
    throwIf(player,
        p -> !(p instanceof Player) &&
            !p.hasPermission(permission),
        Messages.PLAYER_NO_PERMISSION);
  }

  static void checkPlayerInventory(Player player)
      throws ConditionException {
    throwIf(player,
        BukkitUtils::isInventoryFull,
        Messages.INVENTORY_FULL);
  }

  static void checkItem(ItemStack item)
      throws ConditionException {
    throwIf(item,
        i -> !BukkitUtils.isValidItem(i),
        Messages.NEED_ITEM_IN_HAND);
  }

  static void checkPartnerOnline(Player player)
      throws ConditionException {
    throwIf(player,
        Objects::isNull,
        Messages.PARTNER_OFFLINE);
  }

  static void checkPartnerOnline(MargaretPlayer margaretPlayer)
      throws ConditionException {
    checkIsEmpty(margaretPlayer,
        Messages.PARTNER_OFFLINE,
        true);
  }

  static void checkPartnerOnline(UUID uuid)
      throws ConditionException {
    throwIf(uuid,
        uuid1 -> Bukkit.getPlayer(uuid) == null,
        Messages.PARTNER_OFFLINE);
  }

  static void checkPartnerPermission(Partnership partnership,
                                     PartnershipPermission permission)
      throws ConditionException {
    throwIf(partnership,
        p -> !p.hasPermission(permission),
        Messages.PARTNERSHIP_NO_PERMISSION,
        permission);
  }

  static void checkMargaretPlayerHavePartner(MargaretPlayer margaretPlayer)
      throws ConditionException {
    throwIf(margaretPlayer,
        MargaretPlayer::havePartner,
        Messages.PLAYER_HAVE_PARTNER,
        margaretPlayer.getName());
  }

  static void checkNotHavePartner(MargaretPlayer margaretPlayer)
      throws ConditionException {
    throwIf(margaretPlayer,
        MargaretPlayer::havePartner,
        Messages.YOU_HAVE_A_PARTNER);
  }

  static void checkHavePartner(MargaretPlayer margaretPlayer)
      throws ConditionException {
    checkHavePartner(margaretPlayer,
        Messages.YOU_NOT_HAVE_A_PARTNER);
  }

  static void checkHavePartner(MargaretPlayer margaretPlayer,
                               Messages message,
                               Object... objects)
      throws ConditionException {
    throwIf(margaretPlayer,
        m -> !m.havePartner(),
        message,
        objects);
  }

  static void checkMaxHomes(HomeList homes)
      throws ConditionException {
    throwIf(homes.size(),
        h -> h >= homes.getMaxHomes(),
        Messages.NO_MORE_HOMES);
  }

  static void checkGreaterOrEquals(int i1, int i2,
                                   Messages message,
                                   Object... objects)
      throws ConditionException {
    throwIf(i1,
        integer -> i1 >= i2,
        message,
        objects);
  }

  static void checkIsNotNumeric(String string,
                                Messages message,
                                Object... objects)
      throws ConditionException {
    throwIf(string,
        CommonsUtils::isNumeric,
        message,
        objects);
  }

  static void checkStringEquals(String s1,
                                String s2,
                                Messages message,
                                Object... objects)
      throws ConditionException {
    throwIf(s1,
        s -> s.equalsIgnoreCase(s2),
        message,
        objects);
  }

  static <T> void checkCollectionNotContains(Collection<T> collection,
                                             T object,
                                             Messages message,
                                             Object... objects)
      throws ConditionException {
    throwIf(collection,
        c -> (c.contains(object)),
        message,
        objects);
  }

  static <T> void checkCollectionContains(Collection<T> collection,
                                          T object,
                                          Messages message,
                                          Object... objects)
      throws ConditionException {
    throwIf(collection,
        c -> !(c.contains(object)),
        message,
        objects);
  }

  static void checkHomeListAlias(HomeList homeList,
                                 String alias) {
    throwIf(alias,
        homeList::containsAlias,
        Messages.HOME_ALREADY_HAVE_ALIAS,
        alias);
  }
}
