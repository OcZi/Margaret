package me.oczi.bukkit.objects;

import me.oczi.bukkit.objects.player.MargaretPlayer;
import me.oczi.common.api.Emptyble;

/**
 * A Proposal to be Partner.
 */
public interface Proposal extends Emptyble {

  /**
   * Get the player that send the proposal.
   * @return Sender.
   */
  MargaretPlayer getSender();


  /**
   * Get the player that receive the proposal.
   * @return Sender.
   */
  MargaretPlayer getReceiver();

  /**
   * Get the relation that proposed.
   * @return Relation.
   */
  String getRelation();
}
