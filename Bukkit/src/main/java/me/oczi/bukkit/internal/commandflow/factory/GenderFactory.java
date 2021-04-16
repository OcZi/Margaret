package me.oczi.bukkit.internal.commandflow.factory;

import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.part.CommandPart;
import me.oczi.bukkit.internal.commandflow.parts.GenderPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class GenderFactory implements PartFactory {

  @Override
  public CommandPart createPart(String name, List<? extends Annotation> list) {
    return new GenderPart(name);
  }
}
