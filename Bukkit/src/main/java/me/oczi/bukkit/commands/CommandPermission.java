package me.oczi.bukkit.commands;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.oczi.bukkit.internal.commandflow.CommandFlow;
import me.oczi.bukkit.objects.collections.PartnershipPermissionSet;
import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.utils.*;
import org.bukkit.command.CommandSender;

import static me.oczi.bukkit.utils.CommandPreconditions.checkCollectionContains;
import static me.oczi.bukkit.utils.CommandPreconditions.checkCollectionNotContains;

@Command(
    names = {"permission", "perm"},
    desc = "%translatable:permission.desc%",
    permission = "margaret.partnership-permission")
public class CommandPermission implements CommandClass {

  @Command(
      names = {"help", "h"},
      desc = "%translatable:permission.help.desc%")
  public void mainCommand(CommandSender sender,
                          CommandFlow commandFlow) {
    Commands.composeFullChildrenHelp(sender,
        commandFlow.getSubCommandsOf("permission"),
        "margaret",
        "permission");
  }

  @Command(
      names = "add",
      desc = "%translatable:permission.add.desc%",
      permission = "margaret.partnership-permission")
  public void add(CommandSender sender,
                  Partnership partnership,
                  PartnershipPermission permission)
      throws ConditionException {
    PartnershipPermissionSet permissionSet =
        partnership.getPermissions();
    checkCollectionNotContains(permissionSet,
        permission,
        Messages.PARTNER_HAVE_PERMISSION,
        partnership.getId());
    Partnerships.addPermission(partnership.getId(),
        permissionSet,
        permission);
    MessageUtils.compose(sender,
        Messages.PERMISSION_ADDED,
        true,
        permission.toString().toLowerCase(),
        partnership.getId());
  }

  @Command(
      names = "remove",
      desc = "%translatable:permission.remove.desc%",
      permission = "margaret.partnership-permission")
  public void remove(CommandSender sender,
                     Partnership partnership,
                     PartnershipPermission permission)
      throws ConditionException {
    PartnershipPermissionSet permissionSet =
        partnership.getPermissions();
    checkCollectionContains(permissionSet,
        permission,
        Messages.PARTNER_NOT_HAVE_PERMISSION,
        partnership.getId());
    Partnerships.removePermission(partnership.getId(),
        permissionSet,
        permission);
    MessageUtils.compose(sender,
        Messages.PERMISSION_REMOVE,
        true,
        permission.toString().toLowerCase(),
        partnership.getId());
  }
}
