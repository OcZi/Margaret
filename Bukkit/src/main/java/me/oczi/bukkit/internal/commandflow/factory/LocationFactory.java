package me.oczi.bukkit.internal.commandflow.factory;

import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.part.CommandPart;
import me.oczi.bukkit.internal.commandflow.parts.LocationPart;
import me.oczi.common.utils.CommonsUtils;

import java.lang.annotation.Annotation;
import java.util.List;

public class LocationFactory implements PartFactory {

  @Override
  public CommandPart createPart(String s, List<? extends Annotation> list) {
    boolean optional = CommonsUtils.findAnnotation(list, OptArg.class);
    return new LocationPart(s, optional);
  }
}
