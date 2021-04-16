package me.oczi.bukkit.internal.commandflow.parts;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.stack.ArgumentStack;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class MargaretAbstractPart
    implements MargaretPart {
  protected final boolean component;
  protected final String name;
  protected final boolean optional;

  protected MargaretAbstractPart(boolean component,
                                 @NotNull String name,
                                 boolean optional) {
    this.component = component;
    this.name = name;
    this.optional = optional;
  }

  @Override
  public @Nullable Component getLineRepresentation() {
    return component
        ? null
        : TextComponent.of("<" + name + ">");
  }

  protected String nextIfPresent(ArgumentStack stack) {
    return stack.hasNext() ? stack.next() : "";
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public boolean isOptional() {
    return optional;
  }

  @Override
  public boolean isComponent() {
    return component;
  }

  @Override
  public abstract List<?> parseValue(CommandContext context,
                                     ArgumentStack stack)
      throws ArgumentParseException;
}
