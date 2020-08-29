package me.oczi.bukkit.events;

import me.oczi.bukkit.objects.partner.Partner;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PartnerEndEvent extends Event
    implements Cancellable {
  private final Partner partner;
  private boolean cancel;

  public PartnerEndEvent(Partner partner) {
    this.partner = partner;
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

  public Partner getPartner() {
    return partner;
  }
}
