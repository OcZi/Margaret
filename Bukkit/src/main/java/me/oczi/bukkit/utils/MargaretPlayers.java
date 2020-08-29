package me.oczi.bukkit.utils;

import me.oczi.bukkit.MargaretMain;
import me.oczi.bukkit.PluginCore;
import me.oczi.bukkit.events.ProposalSendEvent;
import me.oczi.bukkit.internal.database.DbTasks;
import me.oczi.bukkit.internal.objectcycle.player.PlayerObjectLoader;
import me.oczi.bukkit.objects.Gender;
import me.oczi.bukkit.objects.Proposal;
import me.oczi.bukkit.objects.partner.Partner;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.objects.player.MargaretPlayerProposal;
import me.oczi.bukkit.objects.player.MargaretPlayerTypePair;
import me.oczi.bukkit.storage.yaml.MargaretYamlStorage;
import me.oczi.bukkit.utils.settings.CacheSettings;
import me.oczi.bukkit.utils.settings.EnumSettings;
import me.oczi.common.api.TypePair;
import me.oczi.common.api.TypePairImpl;
import me.oczi.common.storage.sql.orm.result.ResultMap;
import me.oczi.common.storage.sql.orm.result.SqlObject;
import net.kyori.text.TextComponent;
import net.kyori.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class MargaretPlayers {
  private static final PluginCore core = MargaretMain.getCore();
  private static final DbTasks dbTasks = core.getDatabaseTask();

  private static final PlayerObjectLoader loader = core
      .getObjectCycleManager()
      .getPlayerLoader();

  /**
   * Load a {@link MargaretPlayer} from the database or Garbage cache.
   * @param player Player to load as MargaretPlayer.
   */
  public static void loadMargaretPlayer(Player player) {
    loader.load(player);
  }

  /**
   * Close session of {@link MargaretPlayer}.
   * @param player Player to close session.
   */
  public static void closeMargaretPlayer(Player player) {
    loader.close(player);
  }

  /**
   * Get the name of profile by uuid
   * from the server or database.
   * @param uuid UUID to query.
   * @return Name of profile.
   */
  public static String getNameOfProfile(UUID uuid) {
    return getNameOfProfile(uuid, false);
  }

  /**
   * Get the name of profile by uuid
   * from the server or database.
   * @param uuid UUID to query in server or database.
   * @param colorized Colorize name with gender.
   * @return Name of profile.
   */
  public static String getNameOfProfile(UUID uuid, boolean colorized) {
    MargaretPlayer partnerPlayer = MargaretPlayers
        .getAsMargaretPlayer(uuid);
    String partnerName;
    if (partnerPlayer.isEmpty()) {
      if (colorized) {
        ResultMap playerData = dbTasks.getPlayerData(uuid);
        SqlObject nameObject = playerData.get("name");
        SqlObject genderObject = playerData.get("gender");
        partnerName = nameObject.getString();
        String genderName = genderObject.getString();
        Gender gender = Genders.getGender(genderName);
        partnerName = gender.getChatColor() + partnerName;
      } else {
        SqlObject nameObject = dbTasks.getColumnPlayerData(
            uuid, "name");
        partnerName = nameObject.getString();
      }
    } else {
      partnerName = colorized
          ? MargaretPlayers.getNameColorized(partnerPlayer)
          : partnerPlayer.getName();
    }
    return partnerName;
  }

  /**
   * Get {@link MargaretPlayer} of UUID from the {@link PluginCore}.
   * @param uuid UUID of MargaretPlayer.
   * @return MargaretPlayer, or EmptyMargaretPlayer if not exist.
   */
  public static MargaretPlayer getAsMargaretPlayer(UUID uuid) {
    return core.getMargaretPlayer(uuid);
  }

  /**
   * Get {@link MargaretPlayer} of UUID from the {@link PluginCore}.
   * @param sender Sender to cast to Player.
   * @return MargaretPlayer, or EmptyMargaretPlayer if not exist.
   */
  public static MargaretPlayer getAsMargaretPlayer(CommandSender sender) {
    return !(sender instanceof Player)
        ? EmptyObjects.getEmptyMargaretPlayer()
        : getAsMargaretPlayer((Player) sender);
  }

  /**
   * Get {@link MargaretPlayer} of UUID from the {@link PluginCore}.
   * @param player Player to get UUID.
   * @return MargaretPlayer, or EmptyMargaretPlayer if not exist.
   */
  public static MargaretPlayer getAsMargaretPlayer(Player player) {
    return core.getMargaretPlayer(player.getUniqueId());
  }


  /**
   * Get {@link MargaretPlayer} of UUID from the {@link PluginCore}.
   * @param playerName Player's name to get in Bukkit.
   * @return MargaretPlayer, or EmptyMargaretPlayer if not exist.
   */
  public static MargaretPlayer getAsMargaretPlayer(String playerName) {
    Player player = Bukkit.getPlayer(playerName);
    return player != null
        ? core.getMargaretPlayer(player.getUniqueId())
        : EmptyObjects.getEmptyMargaretPlayer();
  }

  /**
   * Get {@link Player} of a {@link MargaretPlayer}.
   * @param margaretPlayer MargaretPlayer to Player.
   * @return Player.
   */
  public static Player getAsPlayer(MargaretPlayer margaretPlayer) {
    return Bukkit.getPlayer(margaretPlayer.getUniqueId());
  }

  /**
   * Get all {@link Player} of a {@link MargaretPlayer}.
   * @param margaretPlayers MargaretPlayers to Players.
   * @return List of players.
   */
  public static List<Player> getAsPlayer(MargaretPlayer... margaretPlayers) {
    List<Player> list = new ArrayList<>();
    for (MargaretPlayer margaretPlayer : margaretPlayers) {
      list.add(getAsPlayer(margaretPlayer));
    }
    return list;
  }

  /**
   * Get the name of {@link MargaretPlayer} colorized by their gender.
   * @param margaretPlayer MargaretPlayer.
   * @return Name of MargaretPlayer colorized.
   */
  public static String getNameColorized(MargaretPlayer margaretPlayer) {
    Gender gender = margaretPlayer.getGender();
    return gender.getChatColor() + margaretPlayer.getName();
  }

  /**
   * Get the pair of {@link MargaretPlayer} of a {@link Partner}.
   * @param partner Partner to get pair.
   * @return Pair of MargaretPlayer.
   */
  public static TypePair<MargaretPlayer> getMargaretPlayerAsPair(Partner partner) {
    return new MargaretPlayerTypePair(
        getAsMargaretPlayer(partner.getUuid1()),
        getAsMargaretPlayer(partner.getUuid2()));
  }

  /**
   * Get the pair of {@link Player} of a {@link Partner}.
   * @param partner Partner to get pair.
   * @return Pair of MargaretPlayer.
   */
  public static TypePair<Player> getPlayerAsPair(Partner partner) {
    return new TypePairImpl<>(
        Bukkit.getPlayer(partner.getUuid1()),
        Bukkit.getPlayer(partner.getUuid2()));
  }

  /**
   * Send proposal from MargaretPlayer1
   * to MargaretPlayer2.
   * @param margaretPlayer1 MargaretPlayer sender.
   * @param margaretPlayer2 MargaretPlayer receiver.
   * @param relation Relation of proposal.
   * @param withRelation Send proposal with relation in message
   */
  public static void sendProposal(MargaretPlayer margaretPlayer1,
                                  MargaretPlayer margaretPlayer2,
                                  String relation,
                                  boolean withRelation) {

    if (relation.isEmpty()) {
      relation = MargaretYamlStorage.getDefaultRelation();
    }
    Proposal proposal = new MargaretPlayerProposal(
        margaretPlayer1,
        margaretPlayer2,
        relation);
    if (BukkitUtils.callEventAndGetResult(
        new ProposalSendEvent(proposal))) {
      return;
    }

    margaretPlayer1.setCurrentProposal(proposal);
    margaretPlayer2.addProposal(proposal);

    MessageUtils.compose(margaretPlayer1,
        Messages.PROPOSAL_SENT,
        true,
        margaretPlayer2.getName());
    if (withRelation) {
      MessageUtils.compose(margaretPlayer2,
          Messages.YOU_RECEIVE_A_PROPOSAL_TO_BE,
          true,
          margaretPlayer1.getName(),
          relation);
    } else {
      MessageUtils.compose(margaretPlayer2,
          Messages.YOU_RECEIVE_A_PROPOSAL,
          true,
          margaretPlayer1.getName());
    }

    sendProposalInteractiveMessages(margaretPlayer1.getName(), margaretPlayer2);
  }

  /**
   * Send Proposal messages interactive to player 2.
   * @param playerName1 Name of sender of proposal used to run command accept or decline.
   * @param margaretPlayer2 Receiver of proposal.
   */
  private static void sendProposalInteractiveMessages(
      String playerName1,
      MargaretPlayer margaretPlayer2) {
    // Hardcoded commands
    TextComponent acceptComponent = MessageUtils
        .messageOf(Messages.PROPOSAL_ACCEPT, TextColor.GREEN,
            MessageUtils.hoverTextOf(
                Messages.PROPOSAL_ACCEPT_HOVER, playerName1),
            "/mr prop accept " + playerName1);
    TextComponent denyComponent = MessageUtils
        .messageOf(Messages.PROPOSAL_DENY, TextColor.RED,
            MessageUtils.hoverTextOf(
                Messages.PROPOSAL_DENY_HOVER, playerName1),
            "/mr prop decline " + playerName1);
    MessageUtils.composeInteractive(margaretPlayer2,
        Messages.PROPOSAL_ACTION_ENTRY,
        true,
        acceptComponent,
        denyComponent);
    SoundUtils.playSound(margaretPlayer2, MargaretSound.PROPOSAL);
  }

  /**
   * Delete all information of MargaretPlayer in database.
   * @param id UUID of MargaretPlayer.
   */
  public static void deleteMargaretPlayer(UUID id) {
    dbTasks.deletePlayerData(id);
    dbTasks.deletePlayerSettings(id);
  }

  /**
   * Toggle settings of MargaretPlayer
   * and update it in the database.
   * @param margaretPlayer Player.
   * @param setting Setting to toggle.
   * @return New value setting.
   */
  public static boolean toggleSetting(MargaretPlayer margaretPlayer,
                                      EnumSettings setting) {
    margaretPlayer.toggleSetting(setting);
    boolean settingValue = margaretPlayer
        .isSetting(setting);
    if (!(setting instanceof CacheSettings)) {
      dbTasks.updatePlayerSetting(setting.getName(),
          settingValue,
          margaretPlayer.getUniqueId());
    }
    return settingValue;
  }

  /**
   * Teleport players.
   * @param margaretPlayer1 Player destination
   * @param margaretPlayer2 Player to teleport
   */
  public static void teleport(MargaretPlayer margaretPlayer1,
                              MargaretPlayer margaretPlayer2) {
    Player player1 = getAsPlayer(margaretPlayer1);
    Player player2 = getAsPlayer(margaretPlayer2);
    player1.teleport(player2);
  }

  /**
   * Set gender of MargaretPlayer and update gender in database.
   * @param margaretPlayer Player.
   * @param gender Gender to set.
   */
  public static void setGender(MargaretPlayer margaretPlayer, Gender gender) {
    margaretPlayer.setGender(gender);
    dbTasks.updatePlayerData("gender",
        gender.getRealName(), margaretPlayer.getUniqueId());
  }

  /**
   * Call {@link MargaretPlayers#cleanUpProposals} for all MargaretPlayers.
   * @param margaretPlayers Players to clean proposals.
   */
  public static void cleanUpProposals(MargaretPlayer... margaretPlayers) {
    for (MargaretPlayer margaretPlayer : margaretPlayers) {
      cleanUpProposals(margaretPlayer);
    }
  }

  /**
   * Clean all the proposals and the actual proposal of MargaretPlayer.
   * @param margaretPlayer Player to clean proposals.
   */
  public static void cleanUpProposals(MargaretPlayer margaretPlayer) {
    margaretPlayer.clearCurrentProposal();
    margaretPlayer.removeAllProposals();
  }

  /**
   * Check if MargaretPlayer has all of the settings.
   * @param margaretPlayer MargaretPlayer to check
   * @param settings Settings to be checked
   * @return Has all settings or not.
   */
  public static boolean isSettings(MargaretPlayer margaretPlayer,
                                   EnumSettings... settings) {
    List<Integer> disabled = new ArrayList<>();
    for (EnumSettings setting : settings) {
      if (!margaretPlayer.isSetting(setting)) {
        disabled.add(1);
      }
    }
    return disabled.size() != settings.length;
  }

  public static void checkMargaretPlayer(@NotNull MargaretPlayer... margaretPlayers) {
    for (MargaretPlayer margaretPlayer : margaretPlayers) {
      checkMargaretPlayer(margaretPlayer);
    }
  }

  public static void checkMargaretPlayer(@NotNull MargaretPlayer margaretPlayer) {
    checkMargaretPlayer(margaretPlayer,
        "MargaretPlayer is invalid (Bad initialization?)");
  }

  public static void checkMargaretPlayer(@NotNull MargaretPlayer margaretPlayer,
                                         String errMessage) {
    if (margaretPlayer == EmptyObjects.getEmptyMargaretPlayer())
      throw new NullPointerException(errMessage);
  }
}
