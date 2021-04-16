package me.oczi.bukkit.internal.commandflow.factory;

import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.part.CommandPart;
import me.oczi.bukkit.internal.commandflow.parts.PartnershipPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class PartnershipFactory implements PartFactory {

  @Override
  public CommandPart createPart(String name, List<? extends Annotation> modifiers) {
    boolean optional = false;
    for (Annotation modifier : modifiers) {
      if (modifier instanceof OptArg) {
        optional = true;
        break;
      }
    }
    return new PartnershipPart(name, optional);
  }
}
