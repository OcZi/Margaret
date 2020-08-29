package me.oczi.bukkit.objects.player;

import com.google.common.collect.Maps;
import me.oczi.bukkit.objects.collections.MetaSettings;
import me.oczi.bukkit.utils.settings.EnumSettings;
import me.oczi.bukkit.utils.settings.PlayerSettings;

import java.util.Map;

public class MargaretPlayerMeta extends MetaSettings<String> {

  public MargaretPlayerMeta() {
    int length = PlayerSettings.getAllSettings().size();
    this.metaMap = Maps.newHashMapWithExpectedSize(length);
    for (EnumSettings value : PlayerSettings.getAllSettings().values()) {
      metaMap.put(value.toString().toLowerCase(), value.getDefaultValue());
    }
  }

  public MargaretPlayerMeta(Map<String, Boolean> settings) {
    this.metaMap = settings;
  }
}