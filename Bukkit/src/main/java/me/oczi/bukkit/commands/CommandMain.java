package me.oczi.bukkit.commands;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.SubCommandClasses;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import me.oczi.bukkit.internal.commandflow.CommandFlow;
import me.oczi.bukkit.objects.Gender;
import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.utils.*;
import me.oczi.bukkit.utils.settings.EnumSetting;
import me.oczi.bukkit.utils.settings.PartnershipSetting;
import org.bukkit.command.CommandSender;

@Command(
    names = {"margaret", "mr", "mg"},
    desc = "%translatable:margaret.desc%")
@SubCommandClasses({
    CommandAdmin.class,
    CommandList.class,
    CommandPartnership.class,
    CommandPermission.class,
    CommandProposal.class,
    CommandReload.class,
    CommandVersion.class})
public class CommandMain implements CommandClass {

  // Generic command help.
  @Command(
      names = {"help", "?"},
      desc = "%translatable:margaret.help.desc%")
  public void mainCommand(CommandSender sender,
                          CommandFlow commandFlow) {
    Commands.composeCategory(
        sender,
        commandFlow
            .getSubCommandsOf("margaret"));
  }

  @Command(
      names = {"gender", "g"},
      desc = "%translatable:gender.desc%",
      permission = "margaret.gender")
  public void gender(@Sender MargaretPlayer margaretSender,
                     Gender gender)
      throws ConditionException {
    MargaretPlayers.setGender(margaretSender, gender);
    MessageUtils.compose(margaretSender,
        Messages.SET_GENDER,
        true,
        gender.getFormalNameColorized());
  }

  @Command(
      names = {"setting", "option"},
      desc = "%translatable:setting.desc%",
      permission = "margaret.setting")
  public void setting(@Sender MargaretPlayer margaretSender,
                      EnumSetting setting)
      throws ConditionException {
    if (setting instanceof PartnershipSetting) {
      if (!margaretSender.havePartner()) {
        MessageUtils.compose(margaretSender,
            Messages.SETTING_PARTNER_ONLY,
            true);
        return;
      } else {
        Partnership partnership = margaretSender.getPartnership();
        PartnershipPermission permissionEquivalent =
            setting.getPermissionEquivalent();
        if (!partnership.hasPermission(permissionEquivalent)) {
          MessageUtils.compose(margaretSender,
              Messages.SETTING_PARTNER_NOT_APPLY,
              true);
          return;
        }
      }
    }
    boolean result = MargaretPlayers
        .toggleSetting(margaretSender, setting);

    MessageUtils.compose(margaretSender,
        Messages.SETTING_ENTRY,
        true,
        setting.getFormalName(),
        result);
  }
}
