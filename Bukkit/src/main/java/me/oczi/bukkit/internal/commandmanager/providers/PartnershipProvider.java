package me.oczi.bukkit.internal.commandmanager.providers;

import app.ashcon.intake.argument.ArgumentException;
import app.ashcon.intake.argument.CommandArgs;
import app.ashcon.intake.argument.Namespace;
import app.ashcon.intake.bukkit.parametric.provider.BukkitProvider;
import app.ashcon.intake.parametric.ProvisionException;
import me.oczi.bukkit.MargaretMain;
import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.utils.Messages;
import me.oczi.bukkit.utils.Partnerships;
import org.bukkit.command.CommandSender;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PartnershipProvider implements BukkitProvider<Partnership> {
  private final Set<String> partnersId = MargaretMain.getCore()
      .getMemoryManager()
      .getPersistenceCache()
      .partnerCacheAsMap()
      .keySet();

  @Override
  public Partnership get(CommandSender commandSender,
                         CommandArgs commandArgs,
                         List<? extends Annotation> list) throws ArgumentException, ProvisionException {
    final String arg = commandArgs.next();
    Partnership partnership = Partnerships.getAsPartner(arg);
    if (!partnership.isEmpty()) {
      return partnership;
    }
    throw ConditionException.newRuntimeException(
        Messages.INVALID_PARTNER, arg);
  }

  @Override
  public String getName() {
    return "partner";
  }

  @Override
  public List<String> getSuggestions(String prefix,
                                     Namespace namespace,
                                     List<? extends Annotation> modifiers) {
    List<String> suggestions = new ArrayList<>();
    for (String id : partnersId) {
      if (id.startsWith(prefix)) {
        suggestions.add(id);
      }
    }
    return suggestions;
  }
}
