package me.oczi.bukkit.internal.database.tasks;

import me.oczi.bukkit.objects.partnership.Partnership;

import java.util.UUID;

public interface DbTaskDelete {

  void deletePlayerData(UUID uuid);

  void deletePlayerSettings(UUID uuid);

  void deletePartnershipData(Partnership partnership);

  void deletePartnershipData(String id);

  void deletePartnershipProperties(Partnership partnership);

  void deletePartnershipProperties(String id);

  void deletePartnershipHomeList(Partnership partnership);

  void deletePartnershipHomeList(String id);

  void deletePartnershipHome(String id);
}
