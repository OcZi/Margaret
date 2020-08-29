package me.oczi.bukkit.internal.database.tasks;

import me.oczi.bukkit.objects.Home;
import me.oczi.bukkit.objects.partner.Partner;
import me.oczi.bukkit.objects.partner.PartnerProperties;
import me.oczi.bukkit.objects.player.PlayerData;

import java.util.List;
import java.util.UUID;

public interface DbTaskSetup {

  void setupPlayerData(PlayerData playerData, int days);

  void setupPlayerSettings(UUID id, List<Object> defaultValues);

  void setupPartnerData(Partner partner);

  void setupPartnerProperties(String id,
                              PartnerProperties properties);

  void setupPartnerHome(String partnerId, Home home);
}
