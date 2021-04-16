package me.oczi.bukkit.internal.commandflow.parts;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.stack.ArgumentStack;

import java.util.Collections;
import java.util.List;

public interface MargaretPart extends ArgumentPart {

  default boolean isOptional() {
    return false;
  }

  default boolean isComponent(){
    return false;
  }

  @Override
  default List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
    if (!isComponent()) {
      if (stack.hasNext()) {
        stack.next();
      }
    }
    return Collections.emptyList();
  }
}
