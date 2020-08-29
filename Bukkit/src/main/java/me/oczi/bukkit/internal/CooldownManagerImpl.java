package me.oczi.bukkit.internal;

import me.oczi.bukkit.objects.CooldownPlayer;
import me.oczi.bukkit.objects.CooldownPlayerImpl;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CooldownManagerImpl implements CooldownManager {
  private final CooldownPlayer commandCooldown;
  private final CooldownPlayer proposalCooldown;

  public CooldownManagerImpl(int commandCooldown,
                             int proposalCooldown) {
    this.commandCooldown = new CooldownPlayerImpl(commandCooldown);
    this.proposalCooldown = new CooldownPlayerImpl(proposalCooldown);
  }

  @Override
  public void setProposalCooldown(Player player) {
    proposalCooldown.setCooldown(player);
  }

  @Override
  public void setProposalCooldown(UUID uuid) {
    proposalCooldown.setCooldown(uuid);
  }

  @Override
  public void setCommandCooldown(Player player) {
    commandCooldown.setCooldown(player);
  }

  @Override
  public void setCommandCooldown(UUID uuid) {
    commandCooldown.setCooldown(uuid);
  }

  @Override
  public CooldownPlayer getProposalCooldown() {
    return proposalCooldown;
  }

  @Override
  public CooldownPlayer getCommandCooldown() {
    return commandCooldown;
  }
}
