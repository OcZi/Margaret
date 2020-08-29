package me.oczi.common.storage.sql.orm.result;

import me.oczi.common.api.Emptyble;
import me.oczi.common.exceptions.SQLCastException;

import java.sql.Time;
import java.sql.Timestamp;

public interface SqlObject extends Emptyble {

  Object getObject();

  String getString() throws SQLCastException;

  String getStringOrDefault(String str);

  Boolean getBoolean() throws SQLCastException;

  Boolean getBooleanOrDefault(boolean bool);

  Integer getInteger() throws SQLCastException;

  Integer getIntegerOrDefault(int i);

  Short getShort() throws SQLCastException;

  Short getShortOrDefault(short num);

  Long getLong() throws SQLCastException;

  Long getLongOrDefault(long num);

  Double getDouble() throws SQLCastException;

  Double getDoubleOrDefault(double num);

  Float getFloat() throws SQLCastException;

  Float getFloatOrDefault(float num);

  java.sql.Date getSqlDate() throws SQLCastException;

  java.sql.Date getSqlDateOrDefault(java.sql.Date date);

  java.util.Date getUtilDate() throws SQLCastException;

  java.util.Date getUtilDateOrDefault(java.util.Date date);

  Time getTime() throws SQLCastException;

  Time getTimeOrDefault(Time time);

  Timestamp getTimestamp() throws SQLCastException;

  Timestamp getTimestampOrDefault(Timestamp timestamp);

  SqlObjectMetaData getMetadata();
}
