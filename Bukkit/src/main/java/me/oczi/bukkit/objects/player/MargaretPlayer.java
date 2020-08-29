package me.oczi.bukkit.objects.player;

import me.oczi.bukkit.objects.Gender;
import me.oczi.bukkit.objects.Proposal;
import me.oczi.bukkit.objects.collections.CacheSet;
import me.oczi.bukkit.objects.partner.Partner;
import me.oczi.bukkit.utils.settings.EnumSettings;
import me.oczi.common.api.Emptyble;

import java.util.UUID;

/**
 * A player interface of the plugin.
 */
public interface MargaretPlayer extends Emptyble {

  /***
   * Add proposal to Proposal's list.
   * @param proposal Proposal to add.
   */
  void addProposal(Proposal proposal);

  /***
   * Remove proposal from Proposal's list.
   * @param proposal Proposal to remove.
   */
  void removeProposal(Proposal proposal);

  /**
   * Remove all proposals of list.
   */
  void removeAllProposals();

  /**
   * Check if MargaretPlayer have a partner.
   * @return Have a partner.
   */
  boolean havePartner();

  /**
   * Clear partner of MargaretPlayer.
   */
  void clearPartner();

  /**
   * Check if a MargaretPlayer is present in Proposal's list.
   * @param margaretPlayer MargaretPlayer to check.
   * @return result of list query.
   */
  boolean containsProposalOf(MargaretPlayer margaretPlayer);

  /**
   * Set partner of MargaretPlayer.
   * @param partner Partner to set.
   */
  void setPartner(Partner partner);

  /**
   * Set gender of MargaretPlayer.
   * @param gender Gender to set.
   */
  void setGender(Gender gender);

  /**
   * Set current proposal of MargaretPlayer.
   * @param proposal Proposal to set.
   */
  void setCurrentProposal(Proposal proposal);

  /**
   * Clear current proposal of MargaretPlayer.
   */
  void clearCurrentProposal();

  /**
   * Toggle a setting of MargaretPlayer.
   * @param setting Setting to toggle.
   */
  void toggleSetting(EnumSettings setting);

  /**
   * Toggle a setting of MargaretPlayer.
   * @param setting Setting to toggle.
   */
  void toggleSetting(String setting);

  /**
   * Check the state of Setting.
   * @param setting Setting to check.
   */
  boolean isSetting(EnumSettings setting);

  /**
   * Check the state of Setting.
   * @param setting Setting to check.
   */
  boolean isSetting(String setting);

  /**
   * Get all of proposals of MargaretPlayer.
   * @return A CacheSet of proposals.
   */
  CacheSet<Proposal> getProposals();

  /**
   * Get gender of MargaretPlayer.
   * @return Gender.
   */
  Gender getGender();

  /**
   * Get the name of MargaretPlayer.
   * @return Name.
   */
  String getName();

  /**
   * Get the UUID of MargaretPlayer.
   * @return UUID.
   */
  UUID getUniqueId();

  /**
   * Get Partner of MargaretPlayer.
   * @return Their partner, or a EmptyPartner if not exist.
   */
  Partner getPartner();

  /**
   * Get settings of MargaretPlayer.
   * @return Setting meta.
   */
  MargaretPlayerMeta getSettings();

  /**
   * Get the current proposal of MargaretPlayer.
   * @return Their proposal, or a EmptyProposal if not exist.
   */
  Proposal getCurrentProposal();
}
