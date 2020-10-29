package me.oczi.bukkit.commands;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.storage.yaml.MargaretYamlStorage;
import me.oczi.bukkit.utils.*;
import me.oczi.bukkit.utils.settings.CacheSettings;
import me.oczi.bukkit.utils.settings.EnumSettings;
import me.oczi.bukkit.utils.settings.PartnershipSettings;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static me.oczi.bukkit.utils.CommandPreconditions.*;

public class SubCommandPartnership {

  // Legacy commands to switch settings.
  @Command(
      aliases = {"chat", "ch"},
      desc = "Partnership chat.")
  public void chat(@Sender CommandSender sender)
      throws ConditionException {
    executeGenericSetting(sender, CacheSettings.CHAT);
  }

  @Command(
      aliases = "pvp",
      desc = "Partnership pvp.")
  public void pvp(@Sender CommandSender sender)
      throws ConditionException {
    executeGenericSetting(sender, PartnershipSettings.ALLOW_PVP);
  }

  @Command(
      aliases = {"mount", "mt"},
      desc = "Partnership mount.")
  public void mount(@Sender CommandSender sender)
      throws ConditionException {
    executeGenericSetting(sender, PartnershipSettings.ALLOW_MOUNT);
  }

  @Command(
      aliases = {"teleport", "tpa", "tp"},
      desc = "Partnership teleport.")
  public void teleport(@Sender CommandSender sender)
      throws ConditionException {
    MargaretPlayer margaretPlayer1 = getCheckedMargaretPlayer(
        sender, PartnershipPermission.TP);
    MargaretPlayer margaretPlayer2 = Partnerships
        .foundPartnerAsMargaretPlayer(margaretPlayer1);
    checkPartnerOnline(margaretPlayer2);

    checkSetting(margaretPlayer2,
        PartnershipSettings.ALLOW_TELEPORT,
        Messages.SETTING_ERROR,
        PartnershipSettings.ALLOW_TELEPORT.getName(),
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
      desc = "Gift something to your partnership.")
  public void gift(@Sender CommandSender sender)
      throws ConditionException {
    MargaretPlayer margaretPlayer = getCheckedMargaretPlayer(
        sender, PartnershipPermission.GIFT);
    Partnerships.sendGift(margaretPlayer);
  }

  @Command(
      aliases = {"heal", "health"},
      desc = "Give health to your partnership.")
  public void heal(@Sender CommandSender sender,
                   int health)
      throws ConditionException {
    MargaretPlayer margaretPlayer = getCheckedMargaretPlayer(
        sender, PartnershipPermission.HEAL);
    Player player2 = Partnerships
        .foundPartnerAsPlayer(margaretPlayer);
    checkPartnerOnline(player2);

    Player player1 = (Player) sender;
    checkPlayerGamemode(player1, GameMode.SURVIVAL);

    Partnerships.sendHealth(player1, player2, health);
  }

  @Command(
      aliases = {"relation", "rt"},
      desc = "Set relation.")
  public void relation(@Sender CommandSender sender,
                       String relation)
      throws ConditionException {
    MargaretPlayer margaretPlayer = getCheckedMargaretPlayer(
        sender, PartnershipPermission.RELATION);
    Partnership partnership = margaretPlayer.getPartnership();
    if (!partnership.hasPermission(PartnershipPermission.CUSTOM_RELATION)) {
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
    Partnerships.setRelation(partnership, relation);
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
                                                  @Nullable PartnershipPermission permission)
      throws ConditionException {
    checkInstanceOfPlayer(sender);

    MargaretPlayer margaretPlayer = MargaretPlayers
        .getAsMargaretPlayer(sender);
    checkHavePartner(margaretPlayer);

    if (permission != null) {
      checkPartnerPermission(
          margaretPlayer.getPartnership(),
          permission);
    }
    return margaretPlayer;
  }
}
