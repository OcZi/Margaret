package me.oczi.bukkit.utils;

import com.google.common.base.Strings;
import me.oczi.bukkit.MargaretMain;
import me.oczi.bukkit.objects.partner.Partner;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.storage.yaml.MargaretYamlStorage;
import me.oczi.common.api.TypePair;
import me.oczi.common.utils.CommonsUtils;
import net.kyori.text.Component;
import net.kyori.text.TextComponent;
import net.kyori.text.adapter.bukkit.TextAdapter;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.format.TextColor;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

/**
 * Utils related to messages.
 */
public interface MessageUtils {

  /**
   * Send a String to the server.
   *
   * @param message String to send
   * @param prefix Prefix use.
   */
  static void broadcast(Object message, boolean prefix) {
    broadcast(message, prefix,
        ArrayUtils.EMPTY_OBJECT_ARRAY);
  }

  /**
   * Send a String to the server.
   *
   * @param message String to send
   * @param prefix Prefix use.
   * @param format Placeholders to format.
   */
  static void broadcast(Object message,
                        boolean prefix,
                        Object... format) {
    String send = composeMessage(message, prefix, format);
    sendBroadcast(send);
  }

  /**
   * Send a String to the console.
   *
   * @param message String to send
   * @param prefix Prefix use.
   */
  static void console(Object message, boolean prefix) {
    console(message, prefix, ArrayUtils.EMPTY_OBJECT_ARRAY);
  }

  /**
   * Send a String to the console.
   *
   * @param message String to send
   * @param prefix Prefix use.
   * @param format Placeholders to format.
   */
  static void console(Object message,
                      boolean prefix,
                      Object... format) {
    String send = composeMessage(message, prefix, format);
    sendConsole(send);
  }

  /**
   * Send a message formatted to sender.
   *
   * @param sender Sender to send.
   * @param message Message to format and send.
   * @param prefix Prefix use.
   */
  static void compose(MargaretPlayer sender,
                      Object message,
                      boolean prefix) {
    compose(sender, message, prefix,
        ArrayUtils.EMPTY_OBJECT_ARRAY);
  }

  /**
   * Send a message formatted to sender.
   *
   * @param sender Sender to send.
   * @param message Message to format and send.
   * @param prefix Prefix use.
   */
  static void compose(MargaretPlayer sender,
                      Object message,
                      boolean prefix,
                      Object... format) {
    String send = composeMessage(message, prefix, format);
    sendMessage(sender, send);
  }

  /**
   * Send a message formatted to sender.
   *
   * @param sender  - Sender to send.
   * @param message - Message to format and send.
   * @param prefix  - Prefix use.
   */
  static void compose(Partner sender,
                      Object message,
                      boolean prefix) {
    compose(sender, message, prefix,
        ArrayUtils.EMPTY_OBJECT_ARRAY);
  }

  /**
   * Send a message formatted to sender.
   *
   * @param sender Sender to send.
   * @param message Message to format and send.
   * @param prefix Prefix use.
   */
  static void compose(Partner sender,
                      Object message,
                      boolean prefix,
                      Object... format) {
    String send = composeMessage(message, prefix, format);
    sendMessage(sender, send);
  }

  /**
   * Send a message formatted to sender.
   *
   * @param sender  - Sender to send.
   * @param message - Message to format and send.
   * @param prefix  - Prefix use.
   */
  static void compose(CommandSender sender,
                      Object message,
                      boolean prefix) {
    compose(sender, message, prefix,
        ArrayUtils.EMPTY_OBJECT_ARRAY);
  }

  /**
   * Send a message formatted to sender.
   *
   * @param sender Sender to send.
   * @param message Message to format and send.
   * @param prefix Prefix use.
   */
  static void compose(CommandSender sender,
                      Object message,
                      boolean prefix,
                      Object... format) {
    String send = composeMessage(message, prefix, format);
    sendMessage(sender, send);
  }

  /**
   * Send a message formatted to sender.
   *
   * @param sender Sender.
   * @param message Message to format and send.
   * @param prefix Use prefix.
   * @param sound Sound to send.
   */
  static void compose(CommandSender sender,
                      Object message,
                      boolean prefix,
                      MargaretSound sound) {
    compose(sender, message, prefix, sound,
        ArrayUtils.EMPTY_OBJECT_ARRAY);
  }

  /**
   * Send a message formatted to sender.
   *
   * @param sender Sender.
   * @param message Message to format and send.
   * @param prefix Use prefix.
   * @param sound Sound to send.
   * @param format Placeholders to format.
   */
  static void compose(CommandSender sender,
                      Object message,
                      boolean prefix,
                      MargaretSound sound,
                      Object... format) {
    String send = composeMessage(message, prefix, format);
    sendMessage(sender, send, sound);
  }

  /**
   * Send a {@link Component} to sender.
   *
   * @param sender Sender.
   * @param component Component to send.
   * @param prefix Use prefix.
   */
  static void composeInteractive(CommandSender sender,
                                 Component component,
                                 boolean prefix) {
    component = composeComponent(component, prefix);
    sendMessage(sender, component);
  }

  /**
   * Send a {@link Component} to sender.
   *
   * @param sender Sender.
   * @param base String base to compose components.
   * @param prefix Use prefix.
   * @param components components of message.
   */
  static void composeInteractive(CommandSender sender,
                                 Messages base,
                                 boolean prefix,
                                 Component... components) {
    Component component = composeComponent(
        getMessageTranslated(base),
        components,
        prefix);
    sendMessage(sender, component);
  }

  /**
   * Send a {@link Component} to sender.
   *
   * @param sender Sender.
   * @param base String base to compose components.
   * @param prefix Use prefix.
   * @param components Components of message.
   */
  static void composeInteractive(CommandSender sender,
                                 String base,
                                 boolean prefix,
                                 Component... components) {
    Component component = composeComponent(base, components, prefix);
    sendMessage(sender, component);
  }

  /**
   * Send a {@link Component} to sender.
   *
   * @param sender Sender.
   * @param base String base to compose components.
   * @param prefix Use prefix.
   * @param components components of message.
   */
  static void composeInteractive(CommandSender sender,
                                 Messages base,
                                 boolean prefix,
                                 TextComponent.Builder... components) {
    Component component = composeComponent(
        getMessageTranslated(base),
        components,
        prefix);
    sendMessage(sender, component);
  }

  /**
   * Send a {@link Component} to sender.
   *
   * @param sender Sender.
   * @param base String base to compose components.
   * @param prefix Use prefix.
   * @param components components of message.
   */
  static void composeInteractive(CommandSender sender,
                                 String base,
                                 boolean prefix,
                                 TextComponent.Builder... components) {
    Component component = composeComponent(base, components, prefix);
    sendMessage(sender, component);
  }

  static void composeInteractive(MargaretPlayer sender,
                                 Messages base,
                                 boolean prefix,
                                 Component... components) {
    Component component = composeComponent(
        getMessageTranslated(base),
        components,
        prefix);
    sendMessage(sender, component);
  }

  /**
   * Compose a colorized message to send with a concatenated string
   * without color.
   *
   * @param sender  - Sender to send.
   * @param message - Format message.
   * @param prefix  - Use or not prefix.
   * @param format  - Placeholders to format.
   */
  @Deprecated
  static void composeConcat(CommandSender sender,
                            Object message,
                            Object concat,
                            boolean prefix,
                            Object... format) {
    String send = composeMessage(message, prefix, format);
    sendMessage(sender, send + concat);
  }

  static void sendMessage(MargaretPlayer sender, Component send) {
    Player player = MargaretPlayers.getAsPlayer(sender);
    sendMessage(player, send);
  }

  static void sendMessage(CommandSender sender, Component send) {
    TextAdapter.sendMessage(sender, send);
  }

  static void sendMessage(MargaretPlayer sender, String send) {
    Player player = MargaretPlayers.getAsPlayer(sender);
    sendMessage(player, send);
  }

  static void sendMessage(Partner sender, String send) {
    TypePair<Player> pair = MargaretPlayers.getPlayerAsPair(sender);
    for (Player player : pair) {
      if (player != null) {
        sendMessage(player, send);
      }
    }
  }

  static void sendMessage(CommandSender sender, String send) {
    sender.sendMessage(send);
  }

  static void sendMessage(CommandSender sender, String send, MargaretSound sound) {
    sendMessage(sender, send);
    if (sender instanceof Player) {
      SoundUtils.playSound((Player) sender, sound);
    }
  }

  static void sendMessage(MargaretPlayer sender, String send, MargaretSound sound) {
    sendMessage(sender, send);
    SoundUtils.playSound(sender, sound);
  }

  static void sendMessage(Player sender, String send) {
    sender.sendMessage(send);
  }


  static void sendConsole(TextComponent send) {
    Bukkit.getConsoleSender().sendMessage(toLegacy(send));
  }

  static void sendConsole(String send) {
    Bukkit.getConsoleSender().sendMessage(send);
  }

  static void sendBroadcast(TextComponent send) {
    Bukkit.broadcastMessage(toLegacy(send));
  }

  static String toLegacy(Component textComponent) {
    return LegacyComponentSerializer
        .legacy()
        .serialize(textComponent);
  }

  static void sendBroadcast(String send) {
    Bukkit.broadcastMessage(send);
  }

  static TextComponent messageOf(Messages message) {
    return TextComponent.of(getMessageTranslated(message));
  }

  static TextComponent messageOf(Messages message,
                                 TextColor color) {
    return TextComponent.of(getMessageTranslated(message))
        .color(color);
  }

  static TextComponent messageOf(Messages message,
                                 TextColor color,
                                 Messages showText) {
    return messageOf(message, color)
        .hoverEvent(HoverEvent.showText(messageOf(showText)));
  }

  static TextComponent messageOf(Messages message,
                                 TextColor color,
                                 HoverEvent hoverEvent) {
    return messageOf(message, color)
        .hoverEvent(hoverEvent);
  }

  static TextComponent messageOf(Messages message,
                                 TextColor color,
                                 Messages showText,
                                 String executeCommand) {
    return messageOf(message, color, showText)
        .clickEvent(ClickEvent.runCommand(executeCommand));
  }

  static TextComponent messageOf(Messages message,
                                 TextColor color,
                                 HoverEvent showText,
                                 String executeCommand) {
    return messageOf(message, color, showText)
        .clickEvent(ClickEvent.runCommand(executeCommand));
  }

  static TextComponent.Builder messageBuilder(Messages message) {
    return TextComponent.builder(getMessageTranslated(message));
  }

  static TextComponent.Builder messageBuilder(Messages message,
                                              TextColor color) {
    return messageBuilder(message).color(color);
  }

  static HoverEvent hoverTextOf(Messages message, Object... objects) {
    String text = getMessageTranslated(message);
    text = CommonsUtils.formatPlaceholder(text, objects);
    return HoverEvent.showText(TextComponent.of(text));
  }

  static void warning(String string) {
    getLogger().warning(string);
  }

  static void warning(String... string) {
    for (String s : string) {
      warning(s);
    }
  }

  /**
   * Get the logger of the main class.
   *
   * @return Logger
   */
  static Logger getLogger() {
    return MargaretMain.getPlugin().getLogger();
  }

  /**
   * Translate any MargaretColor symbol to Legacy ChatColor.
   *
   * @param message - String converted to Legacy ChatColor.
   */
  static String translateMargaretColor(String message) {
    for (int i = 0; i < MargaretColor.values().length; i++) {
      MargaretColor color = MargaretColor.values()[i];
      message = message.replace("$" + i,
          getChatColorOf(color).toString());
    }

    // Doesn't exist color number 5.
    // Just using to reset colors.
    return message.replace("$5", ChatColor.RESET.toString());
  }

  static String translateChatColor(String s) {
    return ChatColor.translateAlternateColorCodes('&', s);
  }


  /**
   * Compose all the components of message.
   *
   * @param message - Message to translate
   * @param prefix  - Using prefix.
   * @param objects - The objects used to replace the placeholders
   * @return Message as String
   */
  static String composeMessage(Object message,
                               boolean prefix,
                               Object... objects) {
    TextComponent.Builder builder = newBuilder(prefix);
    String send = processMessage(message);
    builder.append(
        objects.length > 0
            ? CommonsUtils.formatPlaceholder(send, objects)
            : send);
    return toLegacy(builder
        .colorIfAbsent(MargaretColor.BASE.getDefaultTextColor())
        .build());
  }

  static Component composeComponent(String base,
                                    TextComponent.Builder[] builders,
                                    boolean prefix) {
    Component[] components = new Component[builders.length];
    for (int i = 0; i < builders.length; i++) {
      components[i] = builders[i].build();
    }
    return composeComponent(base, components, prefix);
  }

  static Component composeComponent(String base,
                                    Component[] components,
                                    boolean prefix) {
    TextComponent component = formatComponent(base, components);
    return composeComponent(component, prefix);
  }

  static Component composeComponent(Component message,
                                    boolean prefix) {
    return newBuilder(prefix)
        .append(message)
        .colorIfAbsent(MargaretColor.BASE.getDefaultTextColor())
        .build();
  }

  static TextComponent.Builder newBuilder(boolean prefix) {
    TextComponent.Builder builder = TextComponent.builder();
    if (prefix) {
      builder.append(processPrefix());
    }
    return builder;
  }

  static TextComponent formatComponent(String base,
                                       Component... components) {
    TextComponent.Builder builder = TextComponent.builder();
    for (int i = 0; i < components.length; i++) {
      Component component = components[i];
      String placeholder = "{" + i + "}";
      if (base.isEmpty() || !base.contains(placeholder)) {
        break;
      }

      int position = base.indexOf(placeholder);
      String start = base.substring(0, position);
      // Sum +3 to pass placeholder
      base = base.substring(position + 3);
      builder.append(start)
          .append(component);
    }
    return builder
        .append(base)
        .build();
  }

  static String composeMessageStripped(Object message,
                                       boolean prefix,
                                       Object... objects) {
    return ChatColor.stripColor(
        composeMessage(message, prefix, objects));
  }

  static String processPrefix() {
    return getMessageTranslated(Messages.PREFIX);
  }

  static String processMessage(Object send) {
    if (send == null) {
      warning("A message is null and trying to be process.");
      return MessageUtils.getMessageTranslated(Messages.ERROR_FATAL);
    }

    if (send instanceof TextComponent) {
      return toLegacy((TextComponent) send);
    }

    String message;
    if (send instanceof String) {
      message = (String) send;
    } else if (send instanceof Messages) {
      message = getMessageTranslated((Messages) send);
    } else {
      message = send.toString();
    }

    return translateMargaretColor(message);
  }

  /**
   * Get the message translated by the messages YamlFile.
   * @param message - Message to get in yaml
   * @return Message translated or default.
   */
  static String getMessageTranslated(Messages message) {
    return getMessageTranslated(message, true);
  }

  /**
   * Get the message translated by the messages YamlFile.
   * @param message Message to get in yaml
   * @param colorize Translate Margaret colors.
   * @return Message translated or default.
   */
  static String getMessageTranslated(Messages message, boolean colorize) {
    FileConfiguration messagesConfig = MargaretYamlStorage
        .getMessageConfig()
        .getAccess();
    String translated = messagesConfig == null
        ? message.getMessage()
        : messagesConfig.getString(
            message.getNode(), message.getMessage());
    return colorize
        ? translateMargaretColor(translated)
        : translated;
  }

  static TextColor asTextColor(ChatColor chatColor) {
    return TextColor.valueOf(chatColor.name());
  }

  static TextColor getTextColorOf(MargaretColor margaretColor) {
    FileConfiguration colorConfig = MargaretYamlStorage
        .getMessageColorsConfig()
        .getAccess();
    if (colorConfig == null) {
      return margaretColor.getDefaultTextColor();
    }
    String textColorName = colorConfig
        .getString(margaretColor.toString().toLowerCase());
    return Strings.isNullOrEmpty(textColorName)
        ? margaretColor.getDefaultTextColor()
        : TextColor.valueOf(
            textColorName.toUpperCase());
  }

  static ChatColor getChatColorOf(MargaretColor margaretColor) {
    return ChatColor.valueOf(
        getTextColorOf(margaretColor)
            .toString().toUpperCase());
  }
}
