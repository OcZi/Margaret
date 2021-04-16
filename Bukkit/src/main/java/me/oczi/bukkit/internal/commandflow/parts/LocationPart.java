package me.oczi.bukkit.internal.commandflow.parts;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.exception.NoMoreArgumentsException;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.utils.Commands;
import me.oczi.bukkit.utils.Messages;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class LocationPart extends MargaretAbstractPart {

  public LocationPart(@NotNull String name, boolean optional) {
    super(false, name, optional);
  }

  @Override
  public @Nullable Component getLineRepresentation() {
    return TextComponent.of(
        String.format(
            "<%s> <%s> <%s>",
            "x", "y", "z"));
  }

  @Override
  public List<String> getSuggestions(CommandContext commandContext,
                                     ArgumentStack stack) {
    return Collections.emptyList();
  }

  @Override
  public List<?> parseValue(CommandContext context,
                            ArgumentStack stack)
      throws ArgumentParseException {
    Player sender = Commands.toPlayer(context);
    if (sender == null) {
      throw ConditionException.newException(
          Messages.ONLY_PLAYER);
    }
    World world = sender.getWorld();
    Integer x = null;
    Integer y = null;
    Integer z = null;
    try {
      x = stack.nextInt();
      y = stack.nextInt();
      z = stack.nextInt();
    } catch (NoMoreArgumentsException e) {
      throw ConditionException.newException(
          Messages.INVALID_HOME_LOCATION,
          x, y, z);
    }
    return Collections.singletonList(
        new Location(world, x, y, z));
  }

  @Override
  public Type getType() {
    return Location.class;
  }
}
