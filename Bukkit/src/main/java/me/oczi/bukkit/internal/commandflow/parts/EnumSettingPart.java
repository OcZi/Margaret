package me.oczi.bukkit.internal.commandflow.parts;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.utils.Messages;
import me.oczi.bukkit.utils.settings.EnumSetting;
import me.oczi.bukkit.utils.settings.PlayerSettings;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnumSettingPart extends MargaretAbstractPart {

  public EnumSettingPart(@NotNull String name) {
    super(false, name, false);
  }

  @Override
  public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
    String next = nextIfPresent(stack);
    List<String> settings = new ArrayList<>();
    for (EnumSetting value : PlayerSettings.getAllSettings().values()) {
      String formalName = value.getFormalName();
      if (formalName.startsWith(next)) {
        settings.add(formalName);
      }
    }
    return settings;
  }

  @Override
  public List<?> parseValue(CommandContext context,
                            ArgumentStack stack)
      throws ArgumentParseException {
    String next = stack.next();
    EnumSetting setting = PlayerSettings.getSetting(next);
    if (setting == null) {
      throw ConditionException.newException(
          Messages.INVALID_SETTING,
          next);
    }
    return Collections.singletonList(setting);
  }

  @Override
  public Type getType() {
    return EnumSetting.class;
  }
}
