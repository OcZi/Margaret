package me.oczi.bukkit.internal.objectcycle.player;

import me.oczi.bukkit.MargaretMain;
import me.oczi.bukkit.internal.MemoryManager;
import me.oczi.bukkit.internal.objectcycle.AbstractObjectLoader;
import me.oczi.bukkit.objects.partner.Partner;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.objects.player.MargaretPlayerMeta;
import me.oczi.bukkit.objects.player.PlayerData;
import me.oczi.bukkit.storage.yaml.MargaretYamlStorage;
import me.oczi.bukkit.utils.MessageUtils;
import me.oczi.bukkit.utils.Partners;
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

    if (MargaretYamlStorage.isDebugMode()) {
      MessageUtils.info(uuid + " Profile:");
      MessageUtils.info("Data: ");
      for (Map.Entry<String, String> entry : playerData.toMap().entrySet()) {
        MessageUtils.info(" " + entry.getKey() + ": " + entry.getValue());
      }
      MessageUtils.info("Settings:");
      for (Map.Entry<String, Boolean> entry : metaSettings.asMap().entrySet()) {
        MessageUtils.info(" " + entry.getKey() + ": " + entry.getValue());
      }
    }

    MargaretPlayer margaretPlayer = builder.createMargaretPlayer(playerData, metaSettings);
    persistenceCache.putMargaretPlayer(uuid, margaretPlayer);
  }

  @Override
  public void close(Player player) {
    UUID uuid = player.getUniqueId();
    MargaretPlayer margaretPlayer1 = persistenceCache.getMargaretPlayer(uuid);
    if (!margaretPlayer1.isEmpty()) {
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
        MessageUtils.debug(margaretPlayer1.getName()
            + " swap from Persistence to Garbage");
      }

      persistenceCache.removeMargaretPlayer(uuid);
      MessageUtils.debug(uuid + " session ended");
    }
  }

  @Override
  public void swapObject(Player player) {
    UUID uuid = player.getUniqueId();
    MargaretPlayer margaretPlayer = garbageCache.getMargaretPlayer(uuid);

    persistenceCache.putMargaretPlayer(uuid, margaretPlayer);
    garbageCache.removeMargaretPlayer(uuid);
    MessageUtils.debug(margaretPlayer.getName()
        + " swap from Garbage to Persistence");
  }
}
