package me.oczi.bukkit.commands;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.storage.yaml.MargaretYamlStorage;
import me.oczi.bukkit.utils.*;
import me.oczi.bukkit.utils.settings.CacheSetting;
import me.oczi.bukkit.utils.settings.EnumSetting;
import me.oczi.bukkit.utils.settings.PartnershipSetting;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

import static me.oczi.bukkit.utils.CommandPreconditions.*;

public class SubCommandPartnership implements CommandClass {

  // Legacy commands to switch settings.
  @Command(
      names = {"chat", "ch"},
      desc = "%translatable:partnership.chat.desc%",
      permission = "margaret.setting")
  public void chat(@Sender MargaretPlayer margaretSender,
                   @Sender Partnership partnershipSender)
      throws ConditionException {
    executeGenericSetting(margaretSender,
        partnershipSender,
        CacheSetting.CHAT);
  }

  @Command(
      names = "pvp",
      desc = "%translatable:partnership.pvp.desc%",
      permission = "margaret.setting")
  public void pvp(@Sender MargaretPlayer margaretSender,
                  @Sender Partnership partnershipSender)
      throws ConditionException {
    executeGenericSetting(margaretSender,
        partnershipSender,
        PartnershipSetting.ALLOW_PVP);
  }

  @Command(
      names = {"mount", "mt"},
      desc = "%translatable:partnership.mount.desc%",
      permission = "margaret.setting")
  public void mount(@Sender MargaretPlayer margaretPlayer,
                    @Sender Partnership partnership)
      throws ConditionException {
    executeGenericSetting(margaretPlayer,
        partnership,
        PartnershipSetting.ALLOW_MOUNT);
  }

  @Command(
      names = {"teleport", "tpa", "tp"},
      desc = "%translatable:partnership.teleport.desc%",
      permission = "margaret.partnership.teleport")
  public void teleport(@Sender MargaretPlayer margaretSender,
                       @Sender Partnership partnershipSender)
      throws ConditionException {
    checkPartnerPermission(partnershipSender,
        PartnershipPermission.TP);
    MargaretPlayer margaretPartner = Partnerships
        .foundPartnerAsMargaretPlayer(margaretSender);
    checkPartnerOnline(margaretPartner);

    checkSetting(margaretPartner,
        PartnershipSetting.ALLOW_TELEPORT,
        Messages.SETTING_ERROR,
        PartnershipSetting.ALLOW_TELEPORT.getName(),
        true);
    MargaretPlayers.teleport(
        margaretSender, margaretPartner);
    MessageUtils.compose(margaretSender,
        Messages.PARTNER_TELEPORT_SUCCESS,
        true);
    MessageUtils.compose(margaretPartner,
        Messages.PARTNER_TELEPORT_TO_YOU,
        true);
  }

  @Command(
      names = {"gift", "give"},
      desc = "%translatable:partnership.gift.desc%",
      permission = "margaret.partnership.gift")
  public void gift(@Sender MargaretPlayer margaretSender,
                   @Sender Partnership partnershipSender)
      throws ConditionException {
    checkPartnerPermission(partnershipSender, PartnershipPermission.GIFT);
    Partnerships.sendGift(margaretSender);
  }

  @Command(
      names = {"heal", "health"},
      desc = "%translatable:partnership.heal.desc%",
      permission = "margaret.partnership.heal")
  public void heal(@Sender Player player,
                   @Sender Partnership partnershipSender,
                   int health)
      throws ConditionException {
    checkPartnerPermission(partnershipSender, PartnershipPermission.HEAL);
    UUID uuid = Partnerships.getUuidOfPartner(
        player.getUniqueId(),
        partnershipSender.getUuid1(),
        partnershipSender.getUuid2());
    Player player2 = Bukkit.getPlayer(uuid);
    checkPartnerOnline(player2);
    checkPlayerGamemode(player, GameMode.SURVIVAL);

    Partnerships.sendHealth(player, player2, health);
  }

  @Command(
      names = {"relation", "rt"},
      desc = "%translatable:partnership.relation.desc%",
      permission = "margaret.partnership.relation")
  public void relation(@Sender MargaretPlayer margaretPlayer,
                       @Sender Partnership partnership,
                       String relation)
      throws ConditionException {
    if (!partnership.hasPermission(PartnershipPermission.CUSTOM_RELATION)) {
      List<String> relations = MargaretYamlStorage.getAllowedRelations();
      // If (!relations.contains(relation)) equivalent
      throwIf(relations,
          r -> !r.contains(relation),
          Messages.INVALID_RELATION,
          relation);
    } else {
      throwIf(relation,
          r -> r.length() > 16,
          Messages.RELATION_TOO_LONG,
          relation);
    }
    Partnerships.setRelation(partnership, relation);
    MessageUtils.compose(margaretPlayer,
        Messages.RELATION_SET,
        true,
        relation);
  }

  private void executeGenericSetting(MargaretPlayer margaretPlayer,
                                     Partnership partnership,
                                     EnumSetting setting)
      throws ConditionException {
    PartnershipPermission permission =
        setting.getPermissionEquivalent();
    if (permission != null) {
      checkPartnerPermission(partnership, permission);
    }
    boolean result = MargaretPlayers.toggleSetting(
        margaretPlayer,
        setting);
    MessageUtils.compose(margaretPlayer,
        Messages.SETTING_ENTRY,
        true,
        setting.getFormalName(),
        result);
  }
}
