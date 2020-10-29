package me.oczi.bukkit.events;

import me.oczi.bukkit.objects.player.MargaretPlayer;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PartnershipStartEvent extends Event
    implements Cancellable {
  private final MargaretPlayer player1;
  private final MargaretPlayer player2;
  private boolean cancel;

  public PartnershipStartEvent(MargaretPlayer player1,
                               MargaretPlayer player2){
    this.player1 = player1;
    this.player2 = player2;
  }

  private static final HandlerList handlers = new HandlerList();

  public static HandlerList getHandlerList() {
    return handlers;
  }

  @Override
  public boolean isCancelled() {
    return cancel;
  }

  @Override
  public void setCancelled(boolean cancel) {
    this.cancel = cancel;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public MargaretPlayer getMargaretPlayer1() {
    return player1;
  }

  public MargaretPlayer getMargaretPlayer2() {
    return player2;
  }
}
