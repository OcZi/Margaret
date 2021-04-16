package me.oczi.bukkit.internal.commandflow.parts;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.utils.Commands;
import me.oczi.bukkit.utils.Messages;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class LocationSenderPart extends MargaretAbstractPart {

  public LocationSenderPart(@NotNull String name, boolean optional) {
    super(true, name, optional);
  }

  @Override
  public List<?> parseValue(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
    Player player = Commands.toPlayer(context);
    if (player == null) {
      throw ConditionException
          .newException(Messages.ONLY_PLAYER);
    }
    return Collections.singletonList(player.getLocation());
  }

  @Override
  public Type getType() {
    return Location.class;
  }
}
