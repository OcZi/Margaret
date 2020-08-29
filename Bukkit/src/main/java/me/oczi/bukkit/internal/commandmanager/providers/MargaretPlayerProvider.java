package me.oczi.bukkit.internal.commandmanager.providers;

import app.ashcon.intake.argument.CommandArgs;
import app.ashcon.intake.argument.MissingArgumentException;
import app.ashcon.intake.argument.Namespace;
import app.ashcon.intake.bukkit.parametric.provider.BukkitProvider;
import app.ashcon.intake.parametric.ProvisionException;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.utils.MargaretPlayers;
import me.oczi.bukkit.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class MargaretPlayerProvider implements BukkitProvider<MargaretPlayer> {

  @Override
  public MargaretPlayer get(CommandSender commandSender,
                            CommandArgs commandArgs,
                            List<? extends Annotation> list)
      throws ProvisionException, MissingArgumentException {
    final String arg = commandArgs.next();
    final MargaretPlayer margaretPlayer = MargaretPlayers
        .getAsMargaretPlayer(arg);
    if (!margaretPlayer.isEmpty()) {
      return margaretPlayer;
    }

    throw ConditionException.newRuntimeException(
        Messages.PLAYER_ARG_OFFLINE, arg);
  }

  @Override
  public List<String> getSuggestions(String prefix,
                                     Namespace namespace,
                                     List<? extends Annotation> modifiers) {
    List<String> suggestions = new ArrayList<>();
    for (Player player : Bukkit.getOnlinePlayers()) {
      String playerName = player.getName();
      if (playerName.startsWith(prefix)) {
        suggestions.add(playerName);
      }
    }
    return suggestions;
  }

  @Override
  public String getName() {
    return "player";
  }
}
