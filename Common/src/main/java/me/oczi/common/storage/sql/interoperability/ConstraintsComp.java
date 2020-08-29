package me.oczi.common.storage.sql.interoperability;

import me.oczi.common.storage.sql.datasource.DataSourceType;
import me.oczi.common.utils.CommonsUtils;
import me.oczi.common.utils.Statements;

import java.lang.reflect.Field;

public enum ConstraintsComp {

  AUTO_INCREMENT("AUTO_INCREMENT",
      "",
      "",
      "AUTOINCREMENT",
      "SERIAL",
      ""),
  BOOLEAN("TINYINT(1)",
      "",
      "",
      "",
      "BOOL",
      ""),
  DOUBLE("DOUBLE",
      "",
      "",
      "",
      "DOUBLE PRECISION",
      "");

  private final String placeholder;
  private final String defaultConstraint;
  private final String hsqldb;
  private final String h2;
  private final String sqlite;
  private final String postgresql;
  private final String mysql;

  ConstraintsComp(String defaultConstraint,
                  String hsqldb,
                  String h2,
                  String sqlite,
                  String postgresql,
                  String mysql) {
    this.placeholder = Statements.asPlaceholder(toString());
    this.defaultConstraint = defaultConstraint;
    this.hsqldb = hsqldb;
    this.h2 = h2;
    this.sqlite = sqlite;
    this.postgresql = postgresql;
    this.mysql = mysql;
  }

  public String getDefaultConstraint() {
    return defaultConstraint;
  }

  public String getConstraintComp(DataSourceType dataSourceType) {
    try {
      for (Field field : getClass().getDeclaredFields()) {
        if (dataSourceType.toString()
            .equalsIgnoreCase(field.getName())) {
          String value = (String) field.get(this);
          return !CommonsUtils.isNullOrEmpty(value)
              ? value
              : defaultConstraint;
        }
      }
    }catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    return defaultConstraint;
  }

  public String getPlaceholder() {
    return placeholder;
  }
}
