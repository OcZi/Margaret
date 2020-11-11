package me.oczi.bukkit.internal.commandflow.parts;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.utils.Commands;
import me.oczi.bukkit.utils.MargaretPlayers;
import me.oczi.bukkit.utils.Messages;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class PartnershipSenderPart extends MargaretAbstractPart {

  public PartnershipSenderPart(@NotNull String name,
                               boolean optional) {
    super(true, name, optional);
  }

  @Override
  public List<?> parseValue(CommandContext context,
                            ArgumentStack stack)
      throws ArgumentParseException {
    MargaretPlayer margaretPlayer = MargaretPlayers
        .getAsMargaretPlayer(
            Commands.toSender(context));
    if (margaretPlayer.isEmpty()) {
      throw ConditionException.newException(
          Messages.ONLY_PLAYER);
    }
    Partnership partnership = margaretPlayer.getPartnership();
    if (partnership.isEmpty() && !optional) {
      throw ConditionException.newException(
          Messages.YOU_NOT_HAVE_A_PARTNER);
    }
    return Collections.singletonList(partnership);
  }

  @Override
  public Type getType() {
    return Partnership.class;
  }
}
