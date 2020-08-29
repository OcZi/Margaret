package me.oczi.bukkit.listeners;

import me.oczi.bukkit.objects.partner.Partner;
import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.bukkit.utils.MargaretPlayers;
import me.oczi.bukkit.utils.PartnerPermission;
import me.oczi.bukkit.utils.Partners;
import me.oczi.bukkit.utils.settings.PartnerSettings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerListener implements Listener {

  @EventHandler(priority = EventPriority.NORMAL)
  public void onPlayerJoin(PlayerJoinEvent e) {
    MargaretPlayers.loadMargaretPlayer(e.getPlayer());
  }

  @EventHandler(priority = EventPriority.NORMAL)
  public void onPlayerLeave(PlayerQuitEvent e) {
    MargaretPlayers.closeMargaretPlayer(e.getPlayer());
  }

  @EventHandler()
  public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
    if (!(e.getRightClicked() instanceof Player)) { return; }

    Player player1 = e.getPlayer();
    MargaretPlayer margaretPlayer1 = MargaretPlayers
        .getAsMargaretPlayer(player1);
    if (!margaretPlayer1.havePartner()) {
      return;
    }

    MargaretPlayer margaretPlayer2 = Partners
        .foundPartnerAsMargaretPlayer(margaretPlayer1);
    if (margaretPlayer2.isEmpty()) {
      return;
    }
    if (margaretPlayer2.isSetting(PartnerSettings.ALLOW_MOUNT)) {
      Player player2 = MargaretPlayers
          .getAsPlayer(margaretPlayer2);
      if (player1.getPassenger() == null) {
        player2.setPassenger(player1);
      }
    }
  }

  @EventHandler
  public void onPvp(EntityDamageByEntityEvent e) {
    if (!(e.getEntity() instanceof Player)) { return; }
    Player player1 = (Player) e.getEntity();
    MargaretPlayer margaretPlayer1 = MargaretPlayers
        .getAsMargaretPlayer(player1);
    if (!margaretPlayer1.havePartner()) { return; }

    Partner partner = margaretPlayer1.getPartner();
    UUID partnerUuid = Partners.foundUuidOfPartner(margaretPlayer1);
    MargaretPlayer margaretPlayer2 = MargaretPlayers
        .getAsMargaretPlayer(partnerUuid);
    if (margaretPlayer2.isEmpty()) { return; }
    if (partner.hasPermission(PartnerPermission.PVP) &&
        !margaretPlayer2.isSetting(PartnerSettings.ALLOW_PVP)) {
      e.setCancelled(true);
    }
  }
}
