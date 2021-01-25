package me.oczi.bukkit.listeners;

import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.utils.MargaretPlayers;
import me.oczi.bukkit.utils.PartnershipPermission;
import me.oczi.bukkit.utils.Partnerships;
import me.oczi.bukkit.utils.settings.PartnershipSetting;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerListener implements Listener {

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e) {
    MargaretPlayers.loadMargaretPlayer(e.getPlayer());
  }

  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent e) {
    MargaretPlayers.closeMargaretPlayer(e.getPlayer());
  }

  @EventHandler
  public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
    Entity entity = e.getRightClicked();
    if (!(entity instanceof Player)) {
      return;
    }

    Player player1 = e.getPlayer();
    MargaretPlayer margaretPlayer1 = MargaretPlayers
        .getAsMargaretPlayer(player1);
    if (!margaretPlayer1.havePartner()) {
      return;
    }
    if (player1.isSneaking()) {
      return;
    }

    Player player2 = (Player) entity;
    UUID partnerUuid = Partnerships.foundUuidOfPartner(margaretPlayer1);
    UUID uuid = player2.getUniqueId();
    if (!uuid.equals(partnerUuid)) {
      return;
    }
    MargaretPlayer margaretPlayer2 =
        MargaretPlayers.getAsMargaretPlayer(uuid);
    if (margaretPlayer2.isSetting(PartnershipSetting.ALLOW_MOUNT)) {
      if (player1.getPassenger() == null) {
        player2.setPassenger(player1);
      }
    }
  }

  @EventHandler
  public void onPvp(EntityDamageByEntityEvent e) {
    if (!(e.getEntity() instanceof Player)) {
      return;
    }
    Player player1 = (Player) e.getEntity();
    MargaretPlayer margaretPlayer1 = MargaretPlayers
        .getAsMargaretPlayer(player1);
    if (!margaretPlayer1.havePartner()) {
      return;
    }

    Partnership partnership = margaretPlayer1.getPartnership();
    UUID partnerUuid = Partnerships.foundUuidOfPartner(margaretPlayer1);
    MargaretPlayer margaretPlayer2 = MargaretPlayers
        .getAsMargaretPlayer(partnerUuid);
    if (margaretPlayer2.isEmpty()) {
      return;
    }
    if (partnership.hasPermission(PartnershipPermission.PVP) &&
        !margaretPlayer2.isSetting(PartnershipSetting.ALLOW_PVP)) {
      e.setCancelled(true);
    }
  }
}
