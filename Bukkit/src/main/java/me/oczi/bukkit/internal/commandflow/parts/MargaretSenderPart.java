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
  public static final String MARGARET_SENDER = "MARGARET_PLAYER_SENDER";

  public MargaretSenderPart(@NotNull String name, boolean optional) {
    super(true, name, optional);
  }

  @Override
  public List<?> parseValue(CommandContext context,
                            ArgumentStack stack)
      throws ArgumentParseException {
    MargaretPlayer margaretPlayer = parsePlayer(context, stack, optional);
    context.setObject(MargaretPlayer.class, MARGARET_SENDER, margaretPlayer);
    return Collections.singletonList(margaretPlayer);
  }

  public static MargaretPlayer parsePlayer(CommandContext context,
                                           ArgumentStack stack,
                                           boolean optional)
      throws ArgumentParseException {
    MargaretPlayer margaretPlayer =
        Commands.toMargaretPlayer(context);
    if (margaretPlayer.isEmpty() && !optional) {
      throw ConditionException.newException(
          Messages.ONLY_PLAYER);
    }
    return margaretPlayer;
  }

  @Override
  public Type getType() {
    return MargaretPlayer.class;
  }
}
