package me.oczi.bukkit.commands;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.utils.*;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.format.TextDecoration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class CommandVersion implements CommandClass {

  @Command(
      names = {"version", "ver"},
      desc = "%translatable:version.desc%")
  public void version(CommandSender sender,
                      Plugin plugin) {
    PluginDescriptionFile description = plugin.getDescription();
    MessageUtils.compose(sender,
        Messages.MARGARET_INFO,
        true,
        description.getVersion());
    String authors = String.join(
        ", ",
        description.getAuthors());
    MessageUtils.compose(sender,
        Messages.AUTHORS,
        true,
        authors);
    TextComponent component = composeGithubMessage(description);
    MessageUtils.composeInteractive(sender,
        Messages.GITHUB_URL,
        true,
        component);
    if (sender instanceof Player) {
      MargaretPlayer margaretPlayer = MargaretPlayers
          .getAsMargaretPlayer(sender);
      SoundUtils.playSound(margaretPlayer,
          VersionSound.LEVEL_UP);
    }
  }

  private TextComponent composeGithubMessage(
      PluginDescriptionFile description) {
    String website = description.getWebsite();
    MargaretColor color = MargaretColor.IMPORTANT;
    return TextComponent.of(website)
        .color(color.getDefaultTextColor())
        .decoration(TextDecoration.UNDERLINED, true)
        .clickEvent(ClickEvent.openUrl(website))
        .hoverEvent(MessageUtils
            .hoverTextOf(Messages.GITHUB_HOVER));
  }
}
