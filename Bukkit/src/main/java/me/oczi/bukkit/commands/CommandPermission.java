package me.oczi.bukkit.commands;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import me.oczi.bukkit.objects.collections.PartnershipPermissionSet;
import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.utils.MessageUtils;
import me.oczi.bukkit.utils.Messages;
import me.oczi.bukkit.utils.PartnershipPermission;
import me.oczi.bukkit.utils.Partnerships;
import org.bukkit.command.CommandSender;

import static me.oczi.bukkit.utils.CommandPreconditions.checkCollectionContains;
import static me.oczi.bukkit.utils.CommandPreconditions.checkCollectionNotContains;

public class CommandPermission {

  @Command(
      aliases = "add",
      desc = "Add permission to a partner.",
      perms = "margaret.permission.control")
  public void add(@Sender CommandSender sender,
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
      aliases = "remove",
      desc = "Remove permission to a partner.",
      perms = "margaret.permission.control")
  public void remove(@Sender CommandSender sender,
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
