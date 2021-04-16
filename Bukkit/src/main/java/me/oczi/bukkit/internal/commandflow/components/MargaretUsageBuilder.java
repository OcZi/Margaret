package me.oczi.bukkit.internal.commandflow.components;

import com.google.common.base.CaseFormat;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.command.Command;
import me.fixeddev.commandflow.usage.UsageBuilder;
import me.oczi.bukkit.utils.Commands;
import me.oczi.bukkit.utils.MessageUtils;
import me.oczi.bukkit.utils.Messages;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;

import java.util.Arrays;

public class MargaretUsageBuilder implements UsageBuilder {

  @Override
  public Component getUsage(CommandContext commandContext) {
    Command command = commandContext.getCommand();

    String label = String.join(
        " ",
        commandContext.getLabels());

    String labelComponent = MessageUtils
        .getMessageTranslated(Messages.USAGE_ENTRY, true);
    Component partComponents = command
        .getPart().getLineRepresentation();
    Component description = Commands.getTranslatedDescription(command);

    if (partComponents == null) {
      partComponents = TextComponent.empty();
    }
    if (MessageUtils.isNullOrEmpty(description)) {
      description = MessageUtils.messageOf(Messages.UNKNOWN_DESC);
    }
    TextComponent component = TextComponent.builder(label)
        .append(" ")
        .append(formatPart(partComponents))
        .build();
    return MessageUtils.composeComponent(labelComponent,
        Arrays.asList(component, description),
        false);
  }

  private Component formatPart(Component component) {
    if (MessageUtils.componentIsEmpty(component)) {
      return component;
    }
    String[] parts = MessageUtils.toLegacy(component).split("<");
    TextComponent.Builder builder = TextComponent.builder();
    for (int i = 0; i < parts.length; i++) {
      String part = parts[i];
      if (i > 0) {
        part = "<" + part;
      }
      builder
          .append(
          CaseFormat.LOWER_CAMEL.to
              (CaseFormat.LOWER_HYPHEN, part));
    }
    return builder.build();
  }
}
