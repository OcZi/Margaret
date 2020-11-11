package me.oczi.bukkit.internal.commandflow.parts;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.utils.Commands;
import me.oczi.bukkit.utils.Messages;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class MargaretSenderPart extends MargaretAbstractPart {

  public MargaretSenderPart(@NotNull String name, boolean optional) {
    super(true, name, optional);
  }

  @Override
  public List<?> parseValue(CommandContext context,
                            ArgumentStack stack)
      throws ArgumentParseException {
    MargaretPlayer margaretPlayer =
        Commands.toMargaretPlayer(context);
    if (margaretPlayer.isEmpty() && !optional) {
      throw ConditionException.newException(
          Messages.ONLY_PLAYER);
    }
    return Collections.singletonList(margaretPlayer);
  }

  @Override
  public Type getType() {
    return MargaretPlayer.class;
  }
}
