package me.oczi.bukkit.internal;

import me.oczi.bukkit.objects.CooldownPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface CooldownManager {

  void setProposalCooldown(Player player);

  void setProposalCooldown(UUID uuid);

  void setCommandCooldown(Player player);

  void setCommandCooldown(UUID uuid);

  CooldownPlayer getProposalCooldown();

  CooldownPlayer getCommandCooldown();
}