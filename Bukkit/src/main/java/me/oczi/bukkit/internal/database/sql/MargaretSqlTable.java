package me.oczi.bukkit.internal.database.sql;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import me.oczi.bukkit.MargaretMain;
import me.oczi.bukkit.internal.database.DatabaseManager;
import me.oczi.bukkit.storage.yaml.MargaretYamlStorage;
import me.oczi.bukkit.storage.yaml.impl.TableConfigImpl;
import me.oczi.bukkit.utils.settings.PlayerSettings;
import me.oczi.common.api.configuration.TableConfig;
import me.oczi.common.api.sql.SqlTable;
import me.oczi.common.storage.sql.interoperability.ConstraintsComp;
import me.oczi.common.storage.sql.interoperability.SqlConstraints;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public enum MargaretSqlTable implements SqlTable {

  SQL_PROPERTIES("margaret_sql_properties",
      "id VARCHAR(20) NOT NULL PRIMARY KEY",
      "value VARCHAR(16)"),

  PLAYER_DATA("margaret_player_data",
      "id VARCHAR(36) NOT NULL PRIMARY KEY",
      "name VARCHAR(16)",
      "partnerid VARCHAR(8)",
      "gender VARCHAR(16)",
      "last_date DATE"),
  PARTNERSHIP_DATA("margaret_partnership_data",
      "id VARCHAR(8) NOT NULL PRIMARY KEY",
      "player1 VARCHAR(36)",
      "player2 VARCHAR(36)",
      "relation VARCHAR(16)",
      "creation_date DATE"),

  @SqlConstraints(expression = ConstraintsComp.BOOLEAN)
  PLAYER_SETTINGS("margaret_player_settings",
      "id VARCHAR(36) NOT NULL PRIMARY KEY",
      "%s {BOOLEAN}") {
    private String[] subValues;

    @Override
    public String[] getColumns() {
      if (subValues == null) { subValues = createValues(); }
      return subValues;
    }

    public String[] createValues() {
      String[] values = super.values;
      Set<String> playerSettings =
          PlayerSettings.getDatabaseSettings().keySet();
      Set<String> formatted = new LinkedHashSet<>();
      formatted.add(values[0]);
      for (String playerSetting : playerSettings) {
        formatted.add(String.format(values[1],
            playerSetting));
      }
      return formatted.toArray(values);
    }
  },

  PARTNERSHIP_HOMES_LIST("margaret_partnership_homes_list",
      "id VARCHAR(8) PRIMARY KEY NOT NULL",
      " VARCHAR(12)")  // Only constraints to concat
      {
      private String[] subValues;
      private String constraint;

      @Override
      public String[] getColumns() {
        if (subValues == null) { subValues = createValues(); }
        return subValues;
      }

      @Override
      public String getConstraint() {
        if (Strings.isNullOrEmpty(constraint)) {
          constraint = super.values[1];
        }
        return constraint;
      }

      public String[] createValues() {
        String[] values = super.values;
        List<String> list = Lists.newArrayList(values[0]);
        DatabaseManager databaseManager =
            MargaretMain.getCore().getDatabaseManager();
        for (String s : databaseManager.getTableHomesId()) {
          String homeId = s + getConstraint();
          list.add(homeId);
        }
        return list.toArray(values);
      }
    },

  PARTNERSHIP_PROPERTIES("margaret_partnership_properties",
      "id VARCHAR(8) NOT NULL PRIMARY KEY",
      "maxhomes INTEGER",
      "bitpermissions INTEGER"),

  @SqlConstraints(expression = ConstraintsComp.DOUBLE)
  PARTNERSHIP_HOME("margaret_partnership_home",
      "id VARCHAR(12) PRIMARY KEY",
      "alias VARCHAR(20)",
      "partnerid VARCHAR(8)",
      "world VARCHAR(32)", // World's name have a limit?
      "x {DOUBLE}",
      "y {DOUBLE}",
      "z {DOUBLE}",
      "creation_date DATE");

  private final String defaultName;
  private final String[] values;

  MargaretSqlTable(String defaultName,
                   String... values) {
    this.defaultName = defaultName;
    this.values = values;
  }

  private final TableConfig tables = new TableConfigImpl(
      MargaretYamlStorage.getDatabaseConfig());

  @Override
  public String getName() {
    return tables.getOrDefault(
        name().toLowerCase(), defaultName);
  }

  public String getDefaultName() {
    return defaultName;
  }

  @Override
  public String[] getColumns() {
    return values;
  }
}
