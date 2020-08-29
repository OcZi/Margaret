package me.oczi.common.utils;

import me.oczi.common.api.sql.StatementPattern;
import me.oczi.common.storage.sql.datasource.DataSourceType;
import me.oczi.common.storage.sql.interoperability.ConstraintsComp;
import me.oczi.common.storage.sql.dsl.result.SqlObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static me.oczi.common.storage.sql.dsl.other.LogicalOperatorPattern.EQUALS;
import static me.oczi.common.storage.sql.dsl.other.LogicalOperatorPattern.IN;

/**
 * "SQL" utilities.
 */
public interface Statements {

  static void checkObjects(String errMessage, SqlObject... sqlObjects) {
    for (SqlObject sqlObject : sqlObjects) {
      if (sqlObject.isEmpty()) {
        throw new NullPointerException(errMessage);
      }
    }
  }

  static boolean containsLegalCharacters(String tableName) {
    for (char c : tableName.toCharArray()) {
      if (!Character.isLetterOrDigit(c) && '_' != c ||
          Character.isSpaceChar(c)) {
        return false;
      }
    }
    return true;
  }

  static String asPlaceholder(String string) {
    return CommonsUtils.middleSplit("{}", string);
  }

  static String generateEquals(int i) {
    String placeholder = CommonsUtils.joinRepeatedString(
        "?", ", ", i);
    return generateEquals(i,
        placeholder,
        IN.getPattern(),
        EQUALS.getPattern());
  }

  static String generateEquals(int i,
                               String placeholder,
                               String plural,
                               String mono) {
    return i > 1
        ? String.format(plural, placeholder)
        : String.format(mono, placeholder);
  }

  static String formatConstraintCompatibility(String toFormat,
                                              DataSourceType dataSourceType) {
    for (ConstraintsComp constraintsComp : ConstraintsComp.values()) {
      toFormat = toFormat.replace(constraintsComp.getPlaceholder(),
          constraintsComp.getConstraintComp(dataSourceType));
    }
    return toFormat;
  }

  static String formatPatterns(Collection<StatementPattern> patterns) {
    List<String> patternList = new ArrayList<>();
    for (StatementPattern pattern : patterns) {
      patternList.add(pattern.getPattern());
    }
    return CommonsUtils.cyclicFormat(patternList, true);
  }

  static String formatPatterns(StatementPattern... patterns) {
    List<String> patternList = new ArrayList<>();
    for (StatementPattern pattern : patterns) {
      patternList.add(pattern.getPattern());
    }
    return CommonsUtils.cyclicFormat(patternList, true);
  }

  static String getColumnName(String id) {
    return id.substring(0, id.indexOf(" "));
  }
}
