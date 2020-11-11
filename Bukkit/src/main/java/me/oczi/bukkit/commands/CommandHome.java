package me.oczi.bukkit.commands;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.oczi.bukkit.internal.commandflow.CommandFlow;
import me.oczi.bukkit.internal.database.DatabaseManager;
import me.oczi.bukkit.objects.Home;
import me.oczi.bukkit.objects.collections.HomeList;
import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.utils.*;
import me.oczi.common.utils.CommonsUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.oczi.bukkit.utils.CommandPreconditions.*;

@Command(
    names = {"home", "h"},
    desc = "%translatable:home.desc%",
    permission = "margaret.partnership.home")
public class CommandHome implements CommandClass {

  // Generic command help.
  @Command(
      names = {"help", "?"},
      desc = "%translatable:home.help.desc%")
  public void mainCommand(CommandSender sender,
                          CommandFlow commandFlow) {
    Commands.composeFullChildrenHelp(sender,
        commandFlow.getSubCommandsOf("home"),
        "margaret",
        "home");
  }

  @Command(
      names = {"teleport", "tp"},
      desc = "%translatable:home.teleport.desc%",
      permission = "margaret.partnership.home.tp")
  public void teleport(@Sender Player sender,
                       @Sender Partnership partnershipSender,
                       @OptArg("") String homeId)
      throws ConditionException {
    if (homeId.isEmpty()) {
      homeId = "1";
    }
    Home home = switchHome(partnershipSender, homeId);
    home.teleport(sender);
    MessageUtils.compose(sender,
        Messages.HOME_TELEPORT,
        true,
        homeId,
        home.getId());
  }

  @Command(
      names = {"claim", "create", "set"},
      desc = "%translatable:home.claim.desc%",
      permission = "margaret.home.control")
  public void set(DatabaseManager databaseManager,
                  @Sender Player sender,
                  @Sender Partnership partnershipSender,
                  @OptArg("") String alias)
      throws ConditionException {
    checkIsNotNumeric(alias,
        Messages.INVALID_HOME_ALIAS,
        alias);

    HomeList homeList = partnershipSender.getHomeList();
    checkMaxHomes(homeList);
    checkGreaterOrEquals(homeList.size(),
        databaseManager.getMaxPossibleHomes(),
        Messages.HOME_LIMIT_REACHED);
    checkHomeListAlias(homeList, alias);

    Location location = sender.getLocation();
    homeList.add(Partnerships
        .newHome(partnershipSender.getId(),
            alias,
            location));
    Messages message;
    Object[] objects = new Object[2];
    objects[0] = BukkitUtils.locationToString(location);
    if (alias.isEmpty()) {
      message = Messages.HOME_CREATED;
    } else {
      message = Messages.HOME_CREATED_AS;
      objects[1] = alias;
    }
    MessageUtils.compose(sender,
        message,
        true,
        objects);
  }

  @Command(
      names = {"delete", "del", "remove"},
      desc = "%translatable:home.remove.desc%",
      permission = "margaret.home.control")
  public void remove(CommandSender sender,
                     @Sender Partnership partnership,
                     String homeId) {
    HomeList homeList = partnership.getHomeList();
    Home home = switchHome(partnership, homeId);
    Partnerships.deleteHome(homeList, home);
    MessageUtils.compose(sender,
        Messages.HOME_DELETED,
        true,
        homeId);
  }

  @Command(
      names = {"set-alias", "alias"},
      desc = "%translatable:home.alias.desc%",
      permission = "margaret.partnership.home.alias")
  public void alias(CommandSender sender,
                    @Sender Partnership partnership,
                    String homeId,
                    String alias) {
    HomeList homeList = partnership.getHomeList();
    Home home = switchHome(partnership, homeId);
    throwIf(alias,
        h -> h.length() > 20,
        Messages.INVALID_HOME_ALIAS);
    checkHomeListAlias(homeList, alias);
    Partnerships.setHomeAlias(home, alias);
    MessageUtils.compose(sender,
        Messages.HOME_ALIAS_SET_TO,
        true,
        homeId,
        alias);
  }

  @Command(
      names = {"set-location", "location"},
      desc = "%translatable:home.location.desc%",
      permission = "margaret.partnership.home.location")
  public void location(CommandSender sender,
                       @Sender Partnership partnership,
                       @Sender Location location,
                       String homeId) {
    Home home = switchHome(partnership, homeId);
    Partnerships.setHomeLocation(
        home, partnership.getId(), location);
    MessageUtils.compose(sender,
        Messages.HOME_LOCATION_SET_TO,
        true,
        homeId,
        BukkitUtils.locationToString(location));
  }

  @Command(
      names = {"information", "info"},
      desc = "%translatable:home.information.desc%",
      permission = "margaret.partnership.home.information")
  public void info(CommandSender sender,
                   @Sender Partnership partnership,
                   String homeId) {
    Home home = switchHome(partnership, homeId);
    Partnerships.sendHomeInfo(sender, home);
  }

  private Home switchHome(Partnership partnership, String homeId) {
    return switchHome(partnership.getHomeList(), homeId);
  }

  private Home switchHome(HomeList homeList, String homeId) {
    return CommonsUtils.isNumeric(homeId)
        ? getHome(homeList, Integer.parseInt(homeId))
        : getHomeByName(homeList, homeId);
  }

  private Home getHome(HomeList homeList, int i)
      throws ConditionException {
    Home home = homeList.get(i - 1);
    checkIsEmpty(home, Messages.HOME_NOT_EXIST, i);
    return home;
  }

  private Home getHomeByName(HomeList homeList,
                             String alias)
      throws ConditionException {
    throwIf(alias,
        h -> h.length() > 20,
        Messages.INVALID_HOME_ALIAS,
        alias);
    Home home = homeList.get(alias);
    checkIsEmpty(home, Messages.HOME_NOT_EXIST, alias);
    return home;
  }
}