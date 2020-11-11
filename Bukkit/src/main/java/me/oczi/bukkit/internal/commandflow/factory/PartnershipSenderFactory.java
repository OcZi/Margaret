package me.oczi.bukkit.internal.commandflow.factory;

import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.part.CommandPart;
import me.oczi.bukkit.internal.commandflow.parts.PartnershipSenderPart;
import me.oczi.common.utils.CommonsUtils;

import java.lang.annotation.Annotation;
import java.util.List;

public class PartnershipSenderFactory implements PartFactory {

  @Override
  public CommandPart createPart(String name, List<? extends Annotation> modifiers) {
    boolean optional = CommonsUtils.findAnnotation(modifiers, OptArg.class);
    return new PartnershipSenderPart(name, optional);
  }
}
