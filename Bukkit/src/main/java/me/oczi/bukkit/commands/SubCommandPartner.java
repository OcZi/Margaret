package me.oczi.bukkit.commands;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import me.oczi.bukkit.objects.partner.Partner;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.storage.yaml.MargaretYamlStorage;
import me.oczi.bukkit.utils.*;
import me.oczi.bukkit.utils.settings.CacheSettings;
import me.oczi.bukkit.utils.settings.EnumSettings;
import me.oczi.bukkit.utils.settings.PartnerSettings;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static me.oczi.bukkit.utils.CommandPreconditions.*;

public class SubCommandPartner {

  // Legacy commands to switch settings.
  @Command(
      aliases = {"chat", "ch"},
      desc = "Partner chat.")
  public void chat(@Sender CommandSender sender)
      throws ConditionException {
    executeGenericSetting(sender, CacheSettings.CHAT);
  }

  @Command(
      aliases = "pvp",
      desc = "Partner pvp.")
  public void pvp(@Sender CommandSender sender)
      throws ConditionException {
    executeGenericSetting(sender, PartnerSettings.ALLOW_PVP);
  }

  @Command(
      aliases = {"mount", "mt"},
      desc = "Partner mount.")
  public void mount(@Sender CommandSender sender)
      throws ConditionException {
    executeGenericSetting(sender, PartnerSettings.ALLOW_MOUNT);
  }

  @Command(
      aliases = {"teleport", "tpa", "tp"},
      desc = "Partner teleport.")
  public void teleport(@Sender CommandSender sender)
      throws ConditionException {
    MargaretPlayer margaretPlayer1 = getCheckedMargaretPlayer(
        sender, PartnerPermission.TP);
    MargaretPlayer margaretPlayer2 = Partners
        .foundPartnerAsMargaretPlayer(margaretPlayer1);
    checkPartnerOnline(margaretPlayer2);

    checkSetting(margaretPlayer2,
        PartnerSettings.ALLOW_TELEPORT,
        Messages.SETTING_ERROR,
        PartnerSettings.ALLOW_TELEPORT.getName(),
        true);
    MargaretPlayers.teleport(
        margaretPlayer1, margaretPlayer2);
    MessageUtils.compose(margaretPlayer1,
        Messages.PARTNER_TELEPORT_SUCCESS,
        true);
    MessageUtils.compose(margaretPlayer2,
        Messages.PARTNER_TELEPORT_TO_YOU,
        true);
  }

  @Command(
      aliases = {"gift", "give"},
      desc = "Gift something to partner.")
  public void gift(@Sender CommandSender sender)
      throws ConditionException {
    MargaretPlayer margaretPlayer = getCheckedMargaretPlayer(
        sender, PartnerPermission.GIFT);
    Partners.sendGift(margaretPlayer);
  }

  @Command(
      aliases = {"heal", "health"},
      desc = "Give health to your partner.")
  public void heal(@Sender CommandSender sender,
                   int health)
      throws ConditionException {
    MargaretPlayer margaretPlayer = getCheckedMargaretPlayer(
        sender, PartnerPermission.HEAL);
    Player player2 = Partners
        .foundPartnerAsPlayer(margaretPlayer);
    checkPartnerOnline(player2);

    Player player1 = (Player) sender;
    checkPlayerGamemode(player1, GameMode.SURVIVAL);

    Partners.sendHealth(player1, player2, health);
  }

  @Command(
      aliases = {"relation", "rt"},
      desc = "Set relation.")
  public void relation(@Sender CommandSender sender,
                       String relation)
      throws ConditionException {
    MargaretPlayer margaretPlayer = getCheckedMargaretPlayer(
        sender, PartnerPermission.RELATION);
    Partner partner = margaretPlayer.getPartner();
    if (!partner.hasPermission(PartnerPermission.CUSTOM_RELATION)) {
      List<String> relations = MargaretYamlStorage.getAllowedRelations();
      // If (!relations.contains(relation)) equivalent
      throwIf(relations,
          r -> !r.contains(relation),
          Messages.INVALID_RELATION,
          relation);
    }
    throwIf(relation,
        r -> r.length() > 16,
        Messages.INVALID_RELATION,
        relation);
    Partners.setRelation(partner, relation);
    MessageUtils.compose(sender,
        Messages.RELATION_SET,
        true,
        relation);
  }

  private void executeGenericSetting(CommandSender sender,
                                     EnumSettings setting)
      throws ConditionException {
    MargaretPlayer margaretPlayer = getCheckedMargaretPlayer(
        sender,
        setting.getPermissionEquivalent());
    boolean result = MargaretPlayers.toggleSetting(
        margaretPlayer,
        setting);
    MessageUtils.compose(sender,
        Messages.SETTING_ENTRY,
        true,
        setting.getFormalName(),
        result);
  }

  private MargaretPlayer getCheckedMargaretPlayer(CommandSender sender,
                                                  @Nullable PartnerPermission permission)
      throws ConditionException {
    checkInstanceOfPlayer(sender);

    MargaretPlayer margaretPlayer = MargaretPlayers
        .getAsMargaretPlayer(sender);
    checkHavePartner(margaretPlayer);

    if (permission != null) {
      checkPartnerPermission(
          margaretPlayer.getPartner(),
          permission);
    }
    return margaretPlayer;
  }
}
