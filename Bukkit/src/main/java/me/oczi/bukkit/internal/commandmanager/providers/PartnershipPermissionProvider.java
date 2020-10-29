package me.oczi.bukkit.internal.commandmanager.providers;

import app.ashcon.intake.argument.ArgumentException;
import app.ashcon.intake.argument.CommandArgs;
import app.ashcon.intake.argument.Namespace;
import app.ashcon.intake.bukkit.parametric.provider.BukkitProvider;
import app.ashcon.intake.parametric.ProvisionException;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.utils.Messages;
import me.oczi.bukkit.utils.PartnershipPermission;
import me.oczi.common.utils.CommonsUtils;
import org.bukkit.command.CommandSender;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PartnershipPermissionProvider implements BukkitProvider<PartnershipPermission> {
  private final Set<String> permissions =
      PartnershipPermission.getPermissions().keySet();

  @Override
  public PartnershipPermission get(CommandSender commandSender,
                                   CommandArgs commandArgs,
                                   List<? extends Annotation> list)
      throws ArgumentException, ProvisionException {
    final String arg = commandArgs.next();
    if (CommonsUtils.enumExist(arg, PartnershipPermission.class)) {
      return PartnershipPermission.valueOf(arg.toUpperCase());
    }
    throw ConditionException.newRuntimeException(
        Messages.INVALID_PARTNER_PERMISSION, arg);
  }

  @Override
  public String getName() {
    return "partner permission";
  }

  @Override
  public List<String> getSuggestions(String prefix,
                                     Namespace namespace,
                                     List<? extends Annotation> modifiers) {
    List<String> suggestions = new ArrayList<>();
    for (String permission : permissions) {
      if (permission.startsWith(prefix)) {
        suggestions.add(permission);
      }
    }
    return suggestions;
  }
}
