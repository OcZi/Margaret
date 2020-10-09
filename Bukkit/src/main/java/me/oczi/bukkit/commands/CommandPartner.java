package me.oczi.bukkit.commands;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import app.ashcon.intake.dispatcher.Dispatcher;
import app.ashcon.intake.parametric.annotation.Default;
import me.oczi.bukkit.internal.commandmanager.CommandManager;
import me.oczi.bukkit.objects.collections.HomeList;
import me.oczi.bukkit.objects.partner.Partner;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.utils.*;
import me.oczi.common.utils.CommonsUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

import static me.oczi.bukkit.utils.CommandPreconditions.*;

public class CommandPartner {

  // Generic command help.
  @Command(
      aliases = {"help", "?", ""},
      desc = "Partner command.")
  public void mainCommand(@Sender CommandSender sender,
                          CommandManager commandManager,
                          @Default("") String arg) {
    Dispatcher dispatcher = commandManager
        .getPartnerNode().getDispatcher();
    MargaretPlayer margaretPlayer = MargaretPlayers
        .getAsMargaretPlayer(sender);
    Commands.composeFullHelp(sender,
        dispatcher,
        "partner",
        "partner",
        true,
        partnerExtraMessage(margaretPlayer));
  }

  private String partnerExtraMessage(MargaretPlayer margaretPlayer) {
    String partnerName = !margaretPlayer.isEmpty()
        ? Partners.getNameOfPartner(margaretPlayer, true)
        : MessageUtils.getMessageTranslated(Messages.NONE);
    return MessageUtils.composeMessage(
        Messages.CURRENT_PARTNER,
        false,
        partnerName);
  }

  @Command(
      aliases = {"information", "info"},
      desc = "See the information of your partner.",
      perms = "margaret.partner.info")
  public void info(@Sender CommandSender sender,
                   @Default("") String playerName)
      throws ConditionException {
    MargaretPlayer margaretPlayer;
    if (playerName.isEmpty()) {
      checkInstanceOfPlayer(sender,
          Messages.NEEDS_ARGUMENT);
      margaretPlayer = MargaretPlayers
          .getAsMargaretPlayer(sender);
      checkHavePartner(margaretPlayer);
    } else {
      Player player = Bukkit.getPlayer(playerName);
      checkPlayerOnline(player, playerName);
      margaretPlayer = MargaretPlayers
          .getAsMargaretPlayer(player);
    }
    sendInfo(sender, margaretPlayer);
  }

  private void sendInfo(CommandSender sender,
                        MargaretPlayer margaretPlayer)
      throws ConditionException {
    checkHavePartner(margaretPlayer,
        Messages.PLAYER_HAVE_PARTNER,
        margaretPlayer.getName());
    Partner partner = margaretPlayer.getPartner();
    String playerName1 = MargaretPlayers
        .getNameOfProfile(partner.getUuid1());
    String playerName2 = MargaretPlayers.
        getNameOfProfile(partner.getUuid2());
    HomeList homeList = partner.getHomeList();
    List<String> permissions = partner
        .getPermissions()
        .stream()
        .map(perm -> perm.toString().toLowerCase())
        .collect(Collectors.toList());
    MessageUtils.compose(sender, "ID: " +
        partner.getId(), true);
    MessageUtils.compose(sender, "Player 1: " +
        playerName1, true);
    MessageUtils.compose(sender, "Player 2: " +
        playerName2, true);
    MessageUtils.compose(sender, "Relation: " +
        partner.getRelation(), true);
    MessageUtils.compose(sender, "Homes: " +
        getHomesId(homeList), true);
    MessageUtils.compose(sender, "Max homes: " +
        homeList.getMaxHomes(), true);
    MessageUtils.compose(sender, "Permissions: " +
        CommonsUtils.joinIterable(
            permissions), true);
  }

  private String getHomesId(HomeList homeList) {
    String homeIds = CommonsUtils.joinIterable(
        homeList.getIds());
    return homeIds.isEmpty()
        ? MessageUtils.getMessageTranslated(
            Messages.NONE)
        : homeIds;
  }

  // TEST
  @Command(
      aliases = "check",
      desc = "Check partner.",
      perms = "margaret.partner.check")
  public void check(@Sender CommandSender sender)
      throws ConditionException {
    checkInstanceOfPlayer(sender);
    MargaretPlayer margaretPlayer = MargaretPlayers
        .getAsMargaretPlayer(sender);
    checkHavePartner(margaretPlayer);
    MessageUtils.compose(margaretPlayer,
        Messages.YOU_HAVE_A_PARTNER,
        true);
  }


  @Command(
      aliases = {"end", "divorce"},
      desc = "End your partnership.")
  public void end(@Sender CommandSender sender)
      throws ConditionException {
    checkInstanceOfPlayer(sender);
    MargaretPlayer margaretPlayer = MargaretPlayers
        .getAsMargaretPlayer(sender);
    checkHavePartner(margaretPlayer);
    Partners.endPartner(margaretPlayer.getPartner());
  }
}

