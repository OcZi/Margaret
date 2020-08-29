package me.oczi.bukkit.storage.yaml.impl;

import me.oczi.common.api.configuration.TableConfig;
import me.oczi.common.utils.CommonsUtils;
import me.oczi.common.utils.Statements;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TableConfigImpl implements TableConfig {
  private final Map<String, String> section;

  public TableConfigImpl(FileConfiguration config) {
    Set<String> sets = config
        .getConfigurationSection("table")
        .getKeys(false);
    this.section = new HashMap<>(sets.size());
    for (String entry : sets) {
      String tableName = config.getString("table." + entry);
      checkTableName(tableName, entry);
      section.put(entry, tableName);
    }
  }

  private void checkTableName(String tableName, String entry) {
    if (CommonsUtils.isNullOrEmpty(tableName)) {
      throw new NullPointerException("Table in " + entry + " is null or empty.");
    } else if (!Statements.containsLegalCharacters(tableName)) {
      throw new IllegalArgumentException(
          "Table in " + entry + " contains illegal characters " +
          "(table: '" + tableName +"')");
    }
  }

  @Override
  public String getOrDefault(String node, String defaultName) {
    return section.getOrDefault(node, defaultName);
  }
}
