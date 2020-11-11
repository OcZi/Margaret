package me.oczi.bukkit.internal.commandflow.parts;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.utils.MargaretPlayers;
import me.oczi.bukkit.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MargaretPlayerPart extends MargaretAbstractPart {

  public MargaretPlayerPart(@NotNull String name,
                            boolean optional) {
    super(false, name, optional);
  }

  @Override
  public List<String> getSuggestions(CommandContext commandContext,
                                     ArgumentStack stack) {
    String next = nextIfPresent(stack);
    List<String> players = new ArrayList<>();
    for (Player player : Bukkit.getOnlinePlayers()) {
      String name = player.getName();
      if (name.startsWith(next)) {
        players.add(name);
      }
    }
    return players;
  }

  @Override
  public List<?> parseValue(CommandContext context,
                            ArgumentStack stack)
      throws ArgumentParseException {
    String next = stack.next();
    System.out.println("next = " + next);
    MargaretPlayer player = MargaretPlayers.getAsMargaretPlayer(next);
    if (player.isEmpty() && !optional) {
      throw ConditionException.newException(
          Messages.PLAYER_OFFLINE, next);
    }
    System.out.println("player = " + player);
    return Collections.singletonList(player);
  }

  @Override
  public Type getType() {
    return MargaretPlayer.class;
  }
}
