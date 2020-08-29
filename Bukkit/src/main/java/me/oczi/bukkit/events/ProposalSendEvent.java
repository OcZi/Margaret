package me.oczi.bukkit.events;

import me.oczi.bukkit.objects.Proposal;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ProposalSendEvent extends Event implements Cancellable {
  private final Proposal proposal;
  private boolean cancel;

  public ProposalSendEvent(Proposal proposal){
    this.proposal = proposal;
  }

  private static final HandlerList handlers = new HandlerList();

  public static HandlerList getHandlerList() {
    return handlers;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public Proposal getProposal() {
    return proposal;
  }

  @Override
  public boolean isCancelled() {
    return this.cancel;
  }

  @Override
  public void setCancelled(boolean cancel) {
    this.cancel = cancel;
  }
}
