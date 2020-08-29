package me.oczi.common.storage.sql.dsl.result;

import com.google.common.collect.ForwardingMap;

import java.util.List;
import java.util.Map;

public abstract class ResultMap extends ForwardingMap<String, SqlObject> {

  public abstract SqlObject[] getAll(String key);

  public abstract Map<String, SqlObject> getRow(int i);

  public abstract Map<String, SqlObject> findFirstRow();

  public abstract Map<String, SqlObject> findRow(int i);

  public abstract List<Map<String, SqlObject>> getRows();
}
