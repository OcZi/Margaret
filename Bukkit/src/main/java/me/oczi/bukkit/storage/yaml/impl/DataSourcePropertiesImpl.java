package me.oczi.bukkit.storage.yaml.impl;

import me.oczi.common.api.configuration.DataSourceProperties;
import me.oczi.common.storage.sql.datasource.DataSourceType;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Properties;
import java.util.Set;

public class DataSourcePropertiesImpl implements DataSourceProperties {
  private final Properties properties = new Properties();

  public DataSourcePropertiesImpl(FileConfiguration config,
                                  DataSourceType databaseType) {
    String root = "properties." + databaseType
        .toString().toLowerCase();
    if (config.contains(root)) {
      Set<String> nodes = config
          .getConfigurationSection(root)
          .getKeys(false);
      for (String node : nodes) {
        String route = root + "." + node;
        properties.setProperty(node,
            String.valueOf(config.get(route)));
      }
    }
  }

  @Override
  public Properties getProperties() {
    return properties;
  }

  @Override
  public boolean isEmpty() {
    return properties.isEmpty();
  }
}
