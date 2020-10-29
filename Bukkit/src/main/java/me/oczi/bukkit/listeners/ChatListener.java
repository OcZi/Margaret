package me.oczi.bukkit.listeners;

import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.storage.yaml.MargaretYamlStorage;
import me.oczi.bukkit.utils.*;
import me.oczi.bukkit.utils.settings.CacheSettings;
import me.oczi.common.utils.CommonsUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static me.oczi.bukkit.utils.settings.BasicSettings.SHOW_GENDER;
import static me.oczi.bukkit.utils.settings.BasicSettings.SHOW_PARTNER;
import static me.oczi.bukkit.utils.settings.CacheSettings.CHAT;
import static me.oczi.common.utils.CommonsUtils.isNullOrEmpty;

public class ChatListener implements Listener {
  private final FileConfiguration mainConfig = MargaretYamlStorage
      .getMainConfig()
      .getAccess();

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
    final UUID uuid = e.getPlayer().getUniqueId();
    final MargaretPlayer margaretPlayer = MargaretPlayers
        .getAsMargaretPlayer(uuid);

    if (margaretPlayer.isEmpty()) { return; }
    if (margaretPlayer.isSetting(CHAT)) {
      sendPartnerChat(margaretPlayer, e.getMessage());
      e.setCancelled(true);
      return;
    }

    String sendFormat = createChatFormat(margaretPlayer);
    if (isNullOrEmpty(sendFormat, e.getFormat())) {
      return;
    }

    e.setFormat(sendFormat + e.getFormat());
  }

  private String createChatFormat(final MargaretPlayer margaretPlayer) {
    if (!MargaretPlayers.isSettings(margaretPlayer,
        SHOW_GENDER,
        SHOW_PARTNER)) {
      return "";
    }

    String chatFormat = mainConfig
        .getString("chat.chat-format", "{0}{1}");
    chatFormat = chatFormat
        .replace("{0}", formatGender(margaretPlayer))
        .replace("{1}", formatPartner(margaretPlayer));
    return MessageUtils.translateChatColor(
        chatFormat + ChatColor.RESET);
  }

  private String formatPartner(MargaretPlayer margaretPlayer) {
    if (margaretPlayer.isSetting(SHOW_PARTNER)) {
      Messages prefix = margaretPlayer.havePartner()
          ? Messages.PREFIX_PARTNER
          : Messages.PREFIX_NO_PARTNER;
      return MessageUtils.getMessageTranslated(
          prefix, false);
    }

    return "";
  }

  private String formatGender(MargaretPlayer margaretPlayer) {
    if (margaretPlayer.isSetting(SHOW_GENDER)) {
      String genderPrefix = margaretPlayer
          .getGender()
          .getPrefixColorized();
      return genderPrefix + ChatColor.RESET;
    }

    return "";
  }

  private void sendPartnerChat(MargaretPlayer margaretPlayer1,
                               String message) {
    UUID uuid = Partnerships.foundUuidOfPartner(margaretPlayer1);
    MargaretPlayer margaretPlayer2 = MargaretPlayers
        .getAsMargaretPlayer(uuid);
    if (margaretPlayer2.isEmpty()) {
      MessageUtils.compose(margaretPlayer1,
          Messages.PARTNER_OFFLINE,
          true);
      margaretPlayer1.toggleSetting(CHAT);
      return;
    }

    if (message.isEmpty()) {
      MessageUtils.compose(margaretPlayer1,
          Messages.CHAT_NO_MESSAGE,
          true);
      return;
    }

    String margaretPlayerName1 =
        MargaretPlayers.getNameColorized(margaretPlayer1);
    String margaretPlayerName2 =
        MargaretPlayers.getNameColorized(margaretPlayer2);

    String sent = MessageUtils.composeMessage(
        Messages.CHAT_MESSAGE_SENT,
        false,
        margaretPlayerName1,
        margaretPlayerName2,
        message);
    String receive = MessageUtils.composeMessage(
        Messages.CHAT_MESSAGE_RECEIVED,
        false,
        margaretPlayerName2,
        margaretPlayerName1,
        message);

    MessageUtils.sendMessage(margaretPlayer1, sent);
    MessageUtils.sendMessage(margaretPlayer2, receive,
        MargaretSound.NOTIFICATION);

    Partnership partnership = margaretPlayer1.getPartnership();
    List<Player> playerList = new ArrayList<>(
        Bukkit.getOnlinePlayers());
    for (Player player : playerList) {
      if (CommonsUtils.stringEqualsTo(player.getName(),
          margaretPlayer1.getName(),
          margaretPlayer2.getName())) {
        continue;
      }
      // Hardcoded permission because is not necessary create a entirely
      // class like PartnerPermission for a unique Player permission.
      if (player.hasPermission("margaret.admin.chat-spy")) {
        MargaretPlayer margaretPlayer = MargaretPlayers
            .getAsMargaretPlayer(player);
        if (margaretPlayer.isSetting(CacheSettings.CHAT_SPY)) {
          MessageUtils.compose(player,
              Messages.CHAT_SPY_RECEIVED,
              false,
              partnership.getId(),
              margaretPlayerName1,
              margaretPlayerName2,
              message);
        }
      }
    }
  }
}
