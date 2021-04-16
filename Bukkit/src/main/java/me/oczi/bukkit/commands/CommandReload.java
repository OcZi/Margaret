package me.oczi.bukkit.commands;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.storage.yaml.MargaretYamlStorage;
import me.oczi.bukkit.utils.MessageUtils;
import me.oczi.bukkit.utils.Messages;
import me.oczi.common.utils.CommonsUtils;
import org.bukkit.command.CommandSender;

public class CommandReload implements CommandClass {

  @Command(
      names = "reload",
      desc = "%translatable:reload.desc%",
      permission = "margaret.configuration-reload")
  public void reload(CommandSender sender,
                     String arg)
      throws ConditionException {
    if (CommonsUtils.stringEqualsTo(arg,
        "message", "messages", "msg")) {
      MargaretYamlStorage.reloadMessages();
      MessageUtils.compose(sender, Messages.CONFIGURATION_RELOADED,
          true, "message");
      return;
    }
    if (CommonsUtils.stringEqualsTo(arg,
        "message-colors", "colors")) {
      MargaretYamlStorage.reloadMessageColors();
      MessageUtils.compose(sender, Messages.CONFIGURATION_RELOADED,
          true, "message-colors");
      return;
    }

    throw ConditionException.newException(
        Messages.CONFIGURATION_NOT_FOUND, arg);
  }
}
