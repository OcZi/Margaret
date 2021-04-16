package me.oczi.bukkit.objects.player;

import me.oczi.bukkit.objects.Gender;
import me.oczi.bukkit.objects.Proposal;
import me.oczi.bukkit.objects.collections.CacheSet;
import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.utils.EmptyObjects;
import me.oczi.bukkit.utils.settings.EnumSetting;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MargaretPlayerImpl implements MargaretPlayer {
  private final CacheSet<Proposal> proposals;

  private final UUID uuid;
  private final String name;
  private Gender gender;
  private Partnership partnership;
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
    this.partnership = data.getPartnership();
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
    return !partnership.isEmpty();
  }

  @Override
  public void clearPartner() {
    setPartnership(EmptyObjects.getEmptyPartner());
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
  public void setPartnership(Partnership partnership) {
    this.partnership = partnership;
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
  public void toggleSetting(EnumSetting setting) {
    toggleSetting(setting.getName());
  }

  @Override
  public void toggleSetting(String setting) {
    settingsPlayer.toggleMeta(setting);
  }

  @Override
  public boolean isSetting(EnumSetting setting) {
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
  public Partnership getPartnership() {
    return partnership;
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
        ", partner=" + partnership +
        ", settingsPlayer=" + settingsPlayer +
        ", currentProposal=" + currentProposal +
        '}';
  }
}
