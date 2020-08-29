package me.oczi.bukkit.internal.objectcycle.player;

import me.oczi.bukkit.MargaretMain;
import me.oczi.bukkit.internal.MemoryManager;
import me.oczi.bukkit.internal.objectcycle.AbstractObjectLoader;
import me.oczi.bukkit.objects.partner.Partner;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.objects.player.MargaretPlayerMeta;
import me.oczi.bukkit.objects.player.PlayerData;
import me.oczi.bukkit.utils.*;

import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class PlayerObjectLoader extends AbstractObjectLoader<Player> {
  private final PlayerObjectBuilder builder;

  public PlayerObjectLoader(MemoryManager memoryManager) {
    super(memoryManager);
    this.builder = new PlayerObjectBuilder(MargaretMain.getCore());
  }

  @Override
  public void load(Player player) {
    UUID uuid = player.getUniqueId();
    if (cache.isGarbageCache() &&
        garbageCache.containsMargaretPlayer(uuid)) {
      swapObject(player);
      return;
    }

    PlayerData playerData = builder.initPlayerData(player);
    MargaretPlayerMeta metaSettings = builder.initPlayerSettings(uuid);

    //DEBUGGING
    MessageUtils.console(uuid + " Profile:", true);
    MessageUtils.console("Data: " + playerData.toString(), true);
    MessageUtils.console("Settings:", true);
    for (Map.Entry<String, Boolean> entry : metaSettings.asMap().entrySet()) {
      MessageUtils.console(entry.getKey() + ": " + entry.getValue(), true);
    }
    //DEBUGGING

    MargaretPlayer margaretPlayer = builder.createMargaretPlayer(playerData, metaSettings);
    persistenceCache.putMargaretPlayer(uuid, margaretPlayer);
  }

  @Override
  public void close(Player player) {
    UUID uuid = player.getUniqueId();
    MargaretPlayer margaretPlayer1 = persistenceCache.getMargaretPlayer(uuid);
    if (margaretPlayer1.havePartner()) {
      Partner partner = margaretPlayer1.getPartner();
      MargaretPlayer margaretPlayer2 =
          persistenceCache.getMargaretPlayer(
              Partners.foundUuidOfPartner(margaretPlayer1));
      if (margaretPlayer2.isEmpty()) {
        Partners.closePartner(partner);
      }
    }

    if (cache.isGarbageCache()) {
      garbageCache.putMargaretPlayer(uuid, margaretPlayer1);
      MessageUtils.console(margaretPlayer1.getName()
          + " swap from Persistence to Garbage", true);
    }

    persistenceCache.removeMargaretPlayer(uuid);
    System.out.println(uuid + " closed");
  }

  @Override
  public void swapObject(Player player) {
    UUID uuid = player.getUniqueId();
    MargaretPlayer margaretPlayer = garbageCache.getMargaretPlayer(uuid);

    persistenceCache.putMargaretPlayer(uuid, margaretPlayer);
    garbageCache.removeMargaretPlayer(uuid);
    MessageUtils.console(margaretPlayer.getName()
        + " swap from Garbage to Persistence", true);
  }
}
