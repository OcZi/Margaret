package me.oczi.bukkit.commands;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import me.oczi.bukkit.objects.collections.PartnerPermissionSet;
import me.oczi.bukkit.objects.partner.Partner;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.utils.MessageUtils;
import me.oczi.bukkit.utils.Messages;
import me.oczi.bukkit.utils.PartnerPermission;
import me.oczi.bukkit.utils.Partners;
import org.bukkit.command.CommandSender;

import static me.oczi.bukkit.utils.CommandPreconditions.checkCollectionContains;
import static me.oczi.bukkit.utils.CommandPreconditions.checkCollectionNotContains;

public class CommandPermission {

  @Command(
      aliases = "add",
      desc = "Add permission to a partner.",
      perms = "margaret.permission.control")
  public void add(@Sender CommandSender sender,
                  Partner partner,
                  PartnerPermission permission)
      throws ConditionException {
    PartnerPermissionSet permissionSet =
        partner.getPermissions();
    checkCollectionNotContains(permissionSet,
        permission,
        Messages.PARTNER_HAVE_PERMISSION,
        partner.getId());
    Partners.addPermission(partner.getId(),
        permissionSet,
        permission);
    MessageUtils.compose(sender,
        Messages.PERMISSION_ADDED,
        true,
        permission.toString().toLowerCase(),
        partner.getId());
  }

  @Command(
      aliases = "remove",
      desc = "Remove permission to a partner.",
      perms = "margaret.permission.control")
  public void remove(@Sender CommandSender sender,
                     Partner partner,
                     PartnerPermission permission)
      throws ConditionException {
    PartnerPermissionSet permissionSet =
        partner.getPermissions();
    checkCollectionContains(permissionSet,
        permission,
        Messages.PARTNER_NOT_HAVE_PERMISSION,
        partner.getId());
    Partners.removePermission(partner.getId(),
        permissionSet,
        permission);
    MessageUtils.compose(sender,
        Messages.PERMISSION_REMOVE,
        true,
        permission.toString().toLowerCase(),
        partner.getId());
  }
}
