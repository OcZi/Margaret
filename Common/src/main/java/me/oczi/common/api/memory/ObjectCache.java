package me.oczi.common.api.memory;

import java.util.Map;
import java.util.UUID;

public interface ObjectCache<K1, K2> {

  K1 getMargaretPlayer(UUID uuid);

  K2 getPartner(String id);

  void putMargaretPlayer(UUID uuid, K1 k1);

  void putPartner(String id, K2 k2);

  void removeMargaretPlayer(UUID uuid);

  void removePartner(String id);

  boolean containsMargaretPlayer(UUID uuid);

  boolean containsPartner(String id);

  Map<UUID, K1> playerCacheAsMap();

  Map<String, K2> partnerCacheAsMap();
}
