package me.oczi.bukkit.internal.database.tasks;

import me.oczi.bukkit.objects.partner.Partner;

import java.util.UUID;

public interface DbTaskDelete {

  void deletePlayerData(UUID uuid);

  void deletePlayerSettings(UUID uuid);

  void deletePartnerData(Partner partner);

  void deletePartnerData(String id);

  void deletePartnerProperties(Partner partner);

  void deletePartnerProperties(String id);

  void deletePartnerHomeList(Partner partner);

  void deletePartnerHomeList(String id);

  void deletePartnerHome(String id);
}
