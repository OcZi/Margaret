package me.oczi.bukkit.commands;

import app.ashcon.intake.Command;
import app.ashcon.intake.bukkit.parametric.annotation.Sender;
import me.oczi.bukkit.other.exceptions.ConditionException;
import me.oczi.bukkit.storage.yaml.MargaretYamlStorage;
import me.oczi.bukkit.utils.MessageUtils;
import me.oczi.bukkit.utils.Messages;
import me.oczi.common.utils.CommonsUtils;
import org.bukkit.command.CommandSender;

public class CommandReload {

  @Command(
      aliases = "reload",
      desc = "Reload configuration.")
  public void reload(@Sender CommandSender sender,
                     String arg)
      throws ConditionException {
    if (CommonsUtils.stringEqualsTo(arg,
        "message", "messages", "msg")) {
      MargaretYamlStorage.reloadMessages();
      MessageUtils.compose(sender, Messages.CONFIGURATION_RELOADED,
          true, arg);
      return;
    }
    if (CommonsUtils.stringEqualsTo(arg,
        "message-colors", "colors")) {
      MargaretYamlStorage.reloadMessageColors();
      MessageUtils.compose(sender, Messages.CONFIGURATION_RELOADED,
          true, arg);
      return;
    }

    throw ConditionException.newException(
        Messages.CONFIGURATION_NOT_FOUND, arg);
  }
}
