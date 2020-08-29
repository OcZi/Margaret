package me.oczi.bukkit.internal.database.tasks;

import me.oczi.bukkit.objects.id.ID;

import java.util.UUID;

public interface DbTaskOthers {

  boolean playerDataExist(UUID id);

  boolean playerSettingsExist(UUID id);

  boolean partnerDataExist(String id);

  boolean partnerPropertiesExist(String id);

  boolean partnerHomeExist(String id);

  boolean partnerHomeListExist(String id);

  String foundUnusedPartnerId();

  String foundUnusedId(ID id);
}
