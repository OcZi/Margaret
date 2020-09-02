package me.oczi.common.api.memory;

import java.util.Map;
import java.util.UUID;

public interface ObjectCache<T1, T2> {

  T1 getMargaretPlayer(UUID uuid);

  T2 getPartner(String id);

  void putMargaretPlayer(UUID uuid, T1 t1);

  void putPartner(String id, T2 t2);

  void removeMargaretPlayer(UUID uuid);

  void removePartner(String id);

  boolean containsMargaretPlayer(UUID uuid);

  boolean containsPartner(String id);

  Map<UUID, T1> playerCacheAsMap();

  Map<String, T2> partnerCacheAsMap();
}
