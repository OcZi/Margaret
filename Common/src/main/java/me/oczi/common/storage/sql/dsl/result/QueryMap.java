package me.oczi.common.storage.sql.dsl.result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class QueryMap extends ResultMap {
  private final List<Map<String, SqlObject>> rowList;

  private final int rows;
  private final int columns;

  public QueryMap(List<Map<String, SqlObject>> rowList,
                  int columns,
                  int rows) {
    this.rowList = Collections.unmodifiableList(rowList);
    this.columns = columns;
    this.rows = rows;
  }

  @Override
  public SqlObject[] getAll(String key) {
    List<SqlObject> list = new ArrayList<>();
    for (Map<String, SqlObject> row : rowList) {
      for (Map.Entry<String, SqlObject> entry : row.entrySet()) {
        String entryKey = entry.getKey();
        if (entryKey.startsWith(key.toLowerCase())) {
          list.add(entry.getValue());
        }
      }
    }
    return list.toArray(new SqlObject[]{});
  }

  @Override
  public Map<String, SqlObject> getRow(int i) {
    return rowList.get(i);
  }

  @Override
  public int getColumnCount() {
    return columns;
  }

  @Override
  public int getRowCount() {
    return rows;
  }

  @Override
  public List<Map<String, SqlObject>> getRows() {
    return rowList;
  }

  @Override
  protected List<Map<String, SqlObject>> delegate() {
    return getRows();
  }
}
