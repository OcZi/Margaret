package me.oczi.bukkit.internal.commandmanager.providers;

import app.ashcon.intake.argument.CommandArgs;
import app.ashcon.intake.argument.MissingArgumentException;
import app.ashcon.intake.argument.Namespace;
import app.ashcon.intake.bukkit.parametric.provider.BukkitProvider;
import app.ashcon.intake.parametric.ProvisionException;
import me.oczi.bukkit.objects.Gender;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.utils.Genders;
import me.oczi.bukkit.utils.Messages;
import org.bukkit.command.CommandSender;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class GenderProvider implements BukkitProvider<Gender> {

  @Override
  public Gender get(CommandSender commandSender,
                    CommandArgs commandArgs,
                    List<? extends Annotation> list)
      throws ProvisionException, MissingArgumentException {
    final String arg = commandArgs.next();
    if (Genders.genderExist(arg)) {
      return Genders.getGender(arg);
    }

    throw ConditionException.newRuntimeException(
        Messages.INVALID_GENDER, arg);
  }

  @Override
  public List<String> getSuggestions(String prefix,
                                     Namespace namespace,
                                     List<? extends Annotation> modifiers) {
    prefix = prefix.toLowerCase();
    List<String> suggestions = new ArrayList<>();
    for (Gender gender : Genders.getGenders()) {
      String genderName = gender.getRealName().toLowerCase();
      if (genderName.startsWith(prefix)) {
        suggestions.add(genderName);
      }
    }

    return suggestions;
  }

  @Override
  public String getName() {
    return "gender";
  }
}
