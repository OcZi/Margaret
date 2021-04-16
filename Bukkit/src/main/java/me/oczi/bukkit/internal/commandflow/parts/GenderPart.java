package me.oczi.bukkit.internal.commandflow.parts;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.oczi.bukkit.objects.Gender;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.utils.Genders;
import me.oczi.bukkit.utils.Messages;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenderPart extends MargaretAbstractPart {

  public GenderPart(@NotNull String name) {
    super(false, name, false);
  }

  @Override
  public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
    String next = nextIfPresent(stack);
    List<String> genders = new ArrayList<>();
    for (Gender gender : Genders.getGenders()) {
      String realName = gender.getRealName();
      if (realName.startsWith(next)) {
        genders.add(realName);
      }
    }
    return genders;
  }

  @Override
  public List<?> parseValue(CommandContext context,
                            ArgumentStack stack)
      throws ArgumentParseException {
    String next = stack.next();
    Gender gender = Genders.getGender(next);
    if (gender == null) {
      throw ConditionException.newException(
          Messages.INVALID_GENDER, next);
    }
    return Collections.singletonList(gender);
  }

  @Override
  public Type getType() {
    return Gender.class;
  }

  @Override
  public String getName() {
    return name;
  }
}
