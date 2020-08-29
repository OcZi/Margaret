package me.oczi.bukkit.objects.player;

import me.oczi.bukkit.objects.Proposal;
import me.oczi.bukkit.objects.player.MargaretPlayer;

public class MargaretPlayerProposal implements Proposal {
  private final MargaretPlayer sender;
  private final MargaretPlayer receiver;

  private final String relation;

  public MargaretPlayerProposal(MargaretPlayer sender,
                                MargaretPlayer receiver,
                                String relation) {
    this.sender = sender;
    this.receiver = receiver;
    this.relation = relation;
  }

  @Override
  public MargaretPlayer getSender() {
    return sender;
  }

  @Override
  public MargaretPlayer getReceiver() {
    return receiver;
  }

  @Override
  public String getRelation() {
    return relation;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public String toString() {
    return "MargaretPlayerProposal{" +
        "sender=" + sender.getName() +
        ", receiver=" + receiver.getName() +
        ", relation='" + relation + '\'' +
        '}';
  }
}
