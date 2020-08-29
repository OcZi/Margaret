package me.oczi.bukkit.internal.commandmanager.providers;

import app.ashcon.intake.argument.ArgumentException;
import app.ashcon.intake.argument.CommandArgs;
import app.ashcon.intake.argument.Namespace;
import app.ashcon.intake.bukkit.parametric.provider.BukkitProvider;
import app.ashcon.intake.parametric.ProvisionException;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.utils.Messages;
import me.oczi.bukkit.utils.settings.EnumSettings;
import me.oczi.bukkit.utils.settings.PlayerSettings;
import org.bukkit.command.CommandSender;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SettingProvider implements BukkitProvider<EnumSettings> {
  private final Map<String, EnumSettings> settings = PlayerSettings.getAllSettings();

  @Override
  public EnumSettings get(CommandSender commandSender,
                          CommandArgs commandArgs,
                          List<? extends Annotation> list)
      throws ArgumentException, ProvisionException {
    final String arg = commandArgs.next().toLowerCase();
    for (EnumSettings value : settings.values()) {
      if (value.getFormalName().startsWith(arg)) {
        return value;
      }
    }
    throw ConditionException.newRuntimeException(
        Messages.INVALID_SETTING, arg);
  }

  @Override
  public List<String> getSuggestions(String prefix,
                                     Namespace namespace,
                                     List<? extends Annotation> modifiers) {
    prefix = prefix.toLowerCase();
    List<String> suggestions = new ArrayList<>();
    for (EnumSettings value : settings.values()) {
      String formalName = value.getFormalName();
      if (formalName.startsWith(prefix)) {
        suggestions.add(formalName);
      }
    }
    return suggestions;
  }

  @Override
  public String getName() {
    return "setting";
  }
}
