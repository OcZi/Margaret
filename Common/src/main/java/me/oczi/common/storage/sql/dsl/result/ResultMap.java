package me.oczi.common.storage.sql.dsl.result;

import com.google.common.collect.ForwardingList;

import java.util.List;
import java.util.Map;

public abstract class ResultMap extends ForwardingList<Map<String, SqlObject>> {

  public abstract SqlObject[] getAll(String key);

  public abstract Map<String, SqlObject> getRow(int i);

  public abstract int getColumnCount();

  public abstract int getRowCount();

  public abstract List<Map<String, SqlObject>> getRows();
}
