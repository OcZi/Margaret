package me.oczi.bukkit.internal.commandflow.parts;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.stack.ArgumentStack;
import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.utils.Messages;
import me.oczi.bukkit.utils.Partnerships;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class PartnershipPart extends MargaretAbstractPart {

  public PartnershipPart(String name,
                         boolean optional) {
    super(false, name, optional);
  }

  @Override
  public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
    return Collections.emptyList();
  }

  @Override
  public List<?> parseValue(CommandContext context, ArgumentStack stack) throws ArgumentParseException {
    String next = stack.next();
    Partnership partner = Partnerships.getAsPartner(next);
    if (partner.isEmpty() && !optional) {
      throw ConditionException.newException(
          Messages.INVALID_PARTNER, next);
    }
    return Collections.singletonList(partner);
  }

  @Override
  public Type getType() {
    return Partnership.class;
  }
}
