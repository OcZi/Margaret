package me.oczi.bukkit.internal.database.tasks;

import me.oczi.bukkit.objects.Home;
import me.oczi.bukkit.objects.partnership.Partnership;
import me.oczi.bukkit.objects.partnership.PartnershipProperties;
import me.oczi.bukkit.objects.player.PlayerData;

import java.util.List;
import java.util.UUID;

public interface DbTaskSetup {

  void setupPlayerData(PlayerData playerData, int days);

  void setupPlayerSettings(UUID id, List<Object> defaultValues);

  void setupPartnershipData(Partnership partnership);

  void setupPartnershipProperties(String id,
                                  PartnershipProperties properties);

  void setupPartnershipHome(String partnerId, Home home);
}
