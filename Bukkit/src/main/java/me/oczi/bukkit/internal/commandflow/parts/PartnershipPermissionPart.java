package me.oczi.bukkit.internal.commandflow.parts;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.utils.Messages;
import me.oczi.bukkit.utils.PartnershipPermission;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PartnershipPermissionPart extends MargaretAbstractPart {
  private final Map<String, PartnershipPermission> permissions =
      PartnershipPermission.getFormattedPermissions();

  public PartnershipPermissionPart(@NotNull String name) {
    super(false, name, false);
  }

  @Override
  public List<String> getSuggestions(CommandContext commandContext,
                                     ArgumentStack stack) {
    String next = nextIfPresent(stack);
    List<String> suggestions = new ArrayList<>();
    for (String key : permissions.keySet()) {
      if (key.startsWith(next)) {
        suggestions.add(key);
      }
    }
    return suggestions;
  }

  @Override
  public List<?> parseValue(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
    String next = stack.next().toLowerCase();
    PartnershipPermission permission = permissions.get(next);
    if (permission == null) {
      throw ConditionException.newException(
          Messages.INVALID_PARTNER_PERMISSION, next);
    }
    return Collections.singletonList(permission);
  }

  @Override
  public Type getType() {
    return PartnershipPermission.class;
  }
}
