package me.oczi.bukkit.events;

import me.oczi.bukkit.objects.partnership.Partnership;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PartnershipEndEvent extends Event
    implements Cancellable {
  private final Partnership partnership;
  private boolean cancel;

  public PartnershipEndEvent(Partnership partnership) {
    this.partnership = partnership;
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

  public Partnership getPartnership() {
    return partnership;
  }
}
