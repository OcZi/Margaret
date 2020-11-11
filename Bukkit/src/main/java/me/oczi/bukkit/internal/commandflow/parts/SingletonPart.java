package me.oczi.bukkit.internal.commandflow.parts;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;

import java.util.Objects;

/**
 * A {@link PartFactory} of the same instance of object.
 *
 * @param <T> Type of object.
 */
public class SingletonPart<T> implements CommandPart {
  private final String name;
  private final T object;

  public static <T> CommandPart of(T object,
                                   String name) {
    return new SingletonPart<>(object, name);
  }

  SingletonPart(T object,
                String name) {
    this.object = object;
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void parse(CommandContext context,
                    ArgumentStack stack)
      throws ArgumentParseException {
    context.setValue(this, object);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SingletonPart<?> that = (SingletonPart<?>) o;
    return Objects.equals(name, that.name) &&
        Objects.equals(object, that.object);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, object);
  }
}
