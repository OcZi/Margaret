package me.oczi.common.storage.sql.orm.result;

import me.oczi.common.utils.CommonsUtils;

import java.util.*;

public class QueryMap extends ResultMap {
  private final Map<String, SqlObject> resultMap;

  private final int rows;
  private final int columns;

  public QueryMap(Map<String, SqlObject> resultMap,
                  int columns,
                  int rows) {
    this.resultMap = Collections.unmodifiableMap(resultMap);
    this.columns = columns;
    this.rows = rows;
  }

  @Override
  protected Map<String, SqlObject> delegate() {
    return resultMap;
  }

  @Override
  public SqlObject[] getAll(String key) {
    List<SqlObject> list = new ArrayList<>();
    for (Entry<String, SqlObject> entry : resultMap.entrySet()) {
      String entryKey = entry.getKey();
      if (entryKey.startsWith(key.toLowerCase())) {
        list.add(entry.getValue());
      }
    }
    return list.toArray(new SqlObject[]{});
  }

  @Override
  public Map<String, SqlObject> getRow(int i) {
    return i == 0
        ? findFirstRow()
        : findRow(i);
  }

  @Override
  public Map<String, SqlObject> findFirstRow() {
    Map<String, SqlObject> map = new HashMap<>();
    for (Entry<String, SqlObject> entry : resultMap.entrySet()) {
      String key = entry.getKey();
      if (!CommonsUtils.findNumeric(key)) {
        map.put(key, entry.getValue());
      }
      if (map.size() == columns) {
        break;
      }
    }
    return map;
  }

  @Override
  public Map<String, SqlObject> findRow(int i) {
    Map<String, SqlObject> map = new HashMap<>();
    for (Entry<String, SqlObject> entry : resultMap.entrySet()) {
      String key = entry.getKey();
      if (CommonsUtils.findNumeric(key)) {
        key = key.replace("-" + i, "");
        map.put(key, entry.getValue());
      }
      if (map.size() == columns) {
        break;
      }
    }
    return map;
  }

  @Override
  public List<Map<String, SqlObject>> getRows() {
    List<Map<String, SqlObject>> mapRow = new ArrayList<>();
    for (int i = 0; i < rows; i++) {
      mapRow.add(getRow(i));
    }
    return mapRow;
  }
}
