package me.oczi.bukkit.objects.player;

import me.oczi.bukkit.objects.Gender;
import me.oczi.bukkit.objects.Proposal;
import me.oczi.bukkit.objects.collections.CacheSet;
import me.oczi.bukkit.objects.partner.Partner;
import me.oczi.bukkit.utils.EmptyObjects;
import me.oczi.bukkit.utils.settings.EnumSettings;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MargaretPlayerImpl implements MargaretPlayer {
  private final CacheSet<Proposal> proposals;

  private final UUID uuid;
  private final String name;
  private Gender gender;
  private Partner partner;
  private final MargaretPlayerMeta settingsPlayer;

  private Proposal currentProposal = EmptyObjects.getEmptyProposal();

  public MargaretPlayerImpl(PlayerData data,
                            CacheSet<Proposal> cacheSet) {
    this(data, new MargaretPlayerMeta(), cacheSet);
  }

  public MargaretPlayerImpl(PlayerData data,
                            MargaretPlayerMeta metaSettings,
                            @Nullable CacheSet<Proposal> cacheSet) {
    this.uuid = data.getUniqueId();
    this.name = data.getName();
    this.gender = data.getGender();
    this.partner = data.getPartner();
    this.settingsPlayer = metaSettings;
    this.proposals = cacheSet;
  }

  @Override
  public void addProposal(Proposal proposal) {
    proposals.add(proposal);
  }

  @Override
  public void removeProposal(Proposal proposal) {
    proposals.remove(proposal);
  }

  @Override
  public void removeAllProposals() {
    proposals.removeAll();
  }

  @Override
  public boolean havePartner() {
    return !partner.isEmpty();
  }

  @Override
  public void clearPartner() {
    setPartner(EmptyObjects.getEmptyPartner());
  }

  @Override
  public boolean containsProposalOf(MargaretPlayer margaretPlayer) {
    for (Proposal proposal : proposals) {
      String receiverName = proposal.getSender().getName();
      if (proposal.isEmpty() ||
          receiverName.equalsIgnoreCase(
              margaretPlayer.getName())) {
        return true;
      }
    }

    return false;
  }

  @Override
  public void setPartner(Partner partner) {
    this.partner = partner;
  }

  @Override
  public void setGender(Gender gender) {
    this.gender = gender;
  }

  @Override
  public void setCurrentProposal(Proposal proposal) {
    this.currentProposal = proposal;
  }

  @Override
  public void clearCurrentProposal() {
      this.currentProposal = EmptyObjects.getEmptyProposal();
  }

  @Override
  public void toggleSetting(EnumSettings setting) {
    toggleSetting(setting.getName());
  }

  @Override
  public void toggleSetting(String setting) {
    settingsPlayer.toggleMeta(setting);
  }

  @Override
  public boolean isSetting(EnumSettings setting) {
    return isSetting(setting.getName());
  }

  @Override
  public boolean isSetting(String setting) {
    return settingsPlayer.isMeta(setting);
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public CacheSet<Proposal> getProposals() {
    if (!proposals.isEmpty()) {
      proposals.clear();
    }
    return proposals;
  }

  @Override
  public Gender getGender() {
    return gender;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public UUID getUniqueId() {
    return uuid;
  }

  @Override
  public Partner getPartner() {
    return partner;
  }

  @Override
  public MargaretPlayerMeta getSettings() {
    return settingsPlayer;
  }

  @Override
  public Proposal getCurrentProposal() {
    if (!currentProposal.isEmpty()) {
      currentProposal.getReceiver().getProposals();
    }
    return currentProposal;
  }

  @Override
  public String toString() {
    return "MargaretPlayerImpl{" +
        "proposals=" + proposals +
        ", uuid=" + uuid +
        ", name='" + name + '\'' +
        ", gender=" + gender +
        ", partner=" + partner +
        ", settingsPlayer=" + settingsPlayer +
        ", currentProposal=" + currentProposal +
        '}';
  }
}
