package me.oczi.bukkit.commands;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import app.ashcon.intake.parametric.annotation.Default;
import me.oczi.bukkit.internal.database.DatabaseManager;
import me.oczi.bukkit.objects.Home;
import me.oczi.bukkit.objects.collections.HomeList;
import me.oczi.bukkit.objects.partner.Partner;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.utils.*;
import me.oczi.common.utils.CommonsUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.oczi.bukkit.utils.CommandPreconditions.*;

public class CommandHome {

  @Command(
      aliases = {"teleport", "tp"},
      desc = "Teleport to partner home.",
      perms = "margaret.home")
  public void teleport(@Sender CommandSender sender,
                       @Default("") String alias)
      throws ConditionException {
    checkInstanceOfPlayer(sender);
    Player player = (Player) sender;
    MargaretPlayer margaretPlayer = MargaretPlayers
        .getAsMargaretPlayer(player);
    checkHavePartner(margaretPlayer);
    if (alias.isEmpty()) {
      alias = "1";
    }
    Home home = CommonsUtils.isNumeric(alias)
        ? getHome(margaretPlayer, Integer.parseInt(alias))
        : getHomeByName(margaretPlayer, alias);
    home.teleport(player);
    MessageUtils.compose(sender,
        Messages.HOME_TELEPORT,
        true,
        alias);
  }

  @Command(
      aliases = {"claim", "create", "set"},
      desc = "Create a new home.",
      perms = "margaret.home.control")
  public void set(@Sender CommandSender sender,
                  DatabaseManager databaseManager,
                  @Default("") String alias)
      throws ConditionException {
    checkInstanceOfPlayer(sender);
    MargaretPlayer margaretPlayer = MargaretPlayers
        .getAsMargaretPlayer(sender);
    checkHavePartner(margaretPlayer);
    checkIsNotNumeric(alias,
        Messages.INVALID_HOME_ALIAS);

    Partner partner = margaretPlayer.getPartner();
    HomeList homeList = partner.getHomeList();
    Player player = (Player) sender;
    checkMaxHomes(homeList, player);
    checkGreaterOrEquals(homeList.size(),
        databaseManager.getMaxPossibleHomes(),
        Messages.HOME_LIMIT_REACHED);

    Location location = player.getLocation();
    homeList.add(
        Partners
            .newHome(partner.getId(),
                alias,
                location));
    if (alias.isEmpty()) {
      MessageUtils.compose(sender,
          Messages.HOME_CREATED,
          true,
          BukkitUtils.locationToString(location));
    } else {
      MessageUtils.compose(sender,
          Messages.HOME_CREATED_AS,
          true,
          BukkitUtils.locationToString(location),
          alias);
    }
  }

  @Command(
      aliases = {"remove", "delete"},
      desc = "Delete a home.",
      perms = "margaret.home.control")
  public void remove(@Sender CommandSender sender,
                     int i)
      throws ConditionException {
    checkInstanceOfPlayer(sender);
    MargaretPlayer margaretPlayer = MargaretPlayers
        .getAsMargaretPlayer(sender);
    checkHavePartner(margaretPlayer);

    HomeList homeList = margaretPlayer.getPartner().getHomeList();
    Home home = getHome(homeList, i);
    Partners.deleteHome(homeList, home);
    MessageUtils.compose(sender,
        Messages.HOME_DELETED,
        true,
        i);
  }

  private Home getHome(MargaretPlayer margaretPlayer, int i )
      throws ConditionException {
    return getHome(margaretPlayer.getPartner().getHomeList(), i);
  }

  private Home getHome(HomeList homeList, int i)
      throws ConditionException {
    Home home = homeList.get(i - 1);
    checkIsEmpty(home, Messages.HOME_NOT_EXIST, i);
    return home;
  }

  private Home getHomeByName(MargaretPlayer margaretPlayer,
                             String alias)
      throws ConditionException {
    HomeList homeList = margaretPlayer.getPartner().getHomeList();
    Home home = homeList.get(alias);
    checkIsEmpty(home, Messages.HOME_NOT_EXIST, alias);
    return home;
  }
}
