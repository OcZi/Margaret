package me.oczi.common.storage.sql.dsl.result;

import me.oczi.common.exceptions.SQLCastException;
import me.oczi.common.utils.CommonsUtils;
import org.jetbrains.annotations.Nullable;

import java.sql.*;

public class SqlObjectImpl implements SqlObject{
  private Object object;

  private SqlObjectMetaData objectMetaData;

  public SqlObjectImpl() {}

  public SqlObjectImpl(@Nullable ResultSet rs)
      throws SQLException {
    this(null, rs);
  }

  public SqlObjectImpl(@Nullable Integer i,
                       @Nullable ResultSet rs)
      throws SQLException {
    if (rs != null) {
      if (i == null) { i = 1; }
      this.object = rs.getObject(i);
      if (!isEmpty()) {
        ResultSetMetaData resultMetaData = rs.getMetaData();
        this.objectMetaData = new SqlObjectMetaDataImpl(
            resultMetaData.getColumnName(i),
            resultMetaData.getColumnTypeName(i),
            resultMetaData.getColumnClassName(i),
            resultMetaData.getTableName(i),
            resultMetaData.getCatalogName(i));
      }
    }
  }

  @Override
  public Object getObject() {
    // Accepts null
    return object;
  }

  @Override
  public String getString() throws SQLCastException {
    checkObject(object, "Cannot cast to string. Object is null.");
    return object instanceof String
        ? (String) object
        : String.valueOf(object);
  }

  @Override
  public String getStringOrDefault(String str) {
    try {
      return getString();
    } catch (SQLCastException e) {
      return str;
    }
  }

  @Override
  public Boolean getBoolean() throws SQLCastException {
    checkObject(object, "Cannot cast to boolean. Object is null.");
    if (object instanceof Boolean) {
      return (Boolean) object;
    } else if (object instanceof Number) {
      int value = ((Number) object).intValue();
      return CommonsUtils.parseBoolean(value);
    }

    return Boolean.parseBoolean(object.toString());
  }

  @Override
  public Boolean getBooleanOrDefault(boolean bool) {
    try {
      return getBoolean();
    } catch (SQLCastException e) {
      return bool;
    }
  }

  @Override
  public Integer getInteger() throws SQLCastException {
    checkObject(object, "Cannot cast to integer. Object is null.");
    if (object instanceof Integer) {
      return (Integer) object;
    } else if (object instanceof Number) {
      Number number = (Number) object;
      return number.intValue();
    } else {
      String stringInt = object.toString();
      if ((CommonsUtils.isNumeric(stringInt))) {
        return Integer.parseInt(stringInt);
      }
    }

    throw new SQLCastException(Integer.class);
  }

  @Override
  public Integer getIntegerOrDefault(int i) {
    try {
      return getInteger();
    } catch (SQLCastException e) {
      return i;
    }
  }

  @Override
  public Short getShort() throws SQLCastException {
    checkObject(object, "Cannot cast to short. Object is null.");
    if (object instanceof Short) {
      return (Short) object;
    } else if (object instanceof Number) {
      Number number = (Number) object;
      return number.shortValue();
    } else {
      String stringShort = object.toString();
      if ((CommonsUtils.isNumeric(stringShort))) {
        return Short.parseShort(stringShort);
      }
    }

    throw new SQLCastException(Short.class);
  }

  @Override
  public Short getShortOrDefault(short num) {
    try {
      return getShort();
    } catch (SQLCastException e) {
      return num;
    }
  }

  @Override
  public Long getLong() throws SQLCastException {
    checkObject(object, "Cannot cast to long. Object is null.");
    if (object instanceof Long) {
      return (Long) object;
    } else if (object instanceof Number) {
      Number number = (Number) object;
      return number.longValue();
    } else {
      String stringLong = object.toString();
      if ((CommonsUtils.isNumeric(stringLong))) {
        return Long.parseLong(stringLong);
      }
    }

    throw new SQLCastException(Long.class);
  }

  @Override
  public Long getLongOrDefault(long num) {
    try {
      return getLong();
    } catch (SQLCastException e) {
      return num;
    }
  }

  @Override
  public Double getDouble() throws SQLCastException {
    checkObject(object, "Cannot cast to double. Object is null.");
    if (object instanceof Double) {
      return (Double) object;
    } else if (object instanceof Number) {
      Number number = (Number) object;
      return number.doubleValue();
    } else {
      String stringDouble = object.toString();
      if ((CommonsUtils.isNumeric(stringDouble))) {
        return Double.parseDouble(stringDouble);
      }
    }

    throw new SQLCastException(Double.class);
  }

  @Override
  public Double getDoubleOrDefault(double num) {
    try {
      return getDouble();
    } catch (SQLCastException e) {
      return num;
    }
  }

  @Override
  public Float getFloat() throws SQLCastException {
    checkObject(object, "Cannot cast to float. Object is null.");
    if (object instanceof Float) {
      return (Float) object;
    } else if (object instanceof Number) {
      Number number = (Number) object;
      return number.floatValue();
    } else {
      String stringFloat = object.toString();
      if ((CommonsUtils.isNumeric(stringFloat))) {
        return Float.parseFloat(stringFloat);
      }
    }

    throw new SQLCastException(Float.class);
  }

  @Override
  public Float getFloatOrDefault(float num) {
    try {
      return getFloat();
    } catch (SQLCastException e) {
      return num;
    }
  }

  @Override
  public Date getSqlDate() throws SQLCastException {
    checkObject(object, "Cannot cast to sql date. Object is null.");
    if (object instanceof Date) {
      return (Date) object;
    } else if (object instanceof Number) {
      long millis = ((Number) object).longValue();
      return new Date(millis);
    } else if (object instanceof String) {
      return Date.valueOf((String) object);
    }

    throw new SQLCastException("sql ", Date.class);
  }

  @Override
  public Date getSqlDateOrDefault(Date date) {
    try {
      return getSqlDate();
    } catch (SQLCastException e) {
      return date;
    }
  }

  @Override
  public java.util.Date getUtilDate() throws SQLCastException {
    return getSqlDate();
  }

  @Override
  public java.util.Date getUtilDateOrDefault(java.util.Date date) {
    try {
      return getSqlDate();
    } catch (SQLCastException e) {
      return date;
    }
  }

  @Override
  public Time getTime() throws SQLCastException {
    checkObject(object, "Cannot cast to time. Object is null.");
    if (object instanceof Time) {
      return (Time) object;
    } else if (object instanceof Number) {
      long millis = ((Number) object).longValue();
      return new Time(millis);
    } else {
      String stringObject = object.toString();
      if (CommonsUtils.isNumeric(stringObject)) {
        long millis = Long.parseLong(stringObject);
        return new Time(millis);
      } else {
        try {
          return Time.valueOf(stringObject);
        } catch (IllegalArgumentException e) {
          throw new SQLCastException(Time.class, e);
        }
      }
    }
  }

  @Override
  public Time getTimeOrDefault(Time time) {
    try {
      return getTime();
    } catch (SQLCastException e) {
      return time;
    }
  }

  @Override
  public Timestamp getTimestamp() throws SQLCastException {
    checkObject(object, "Cannot cast to timestamp. Object is null.");
    if (object instanceof Timestamp) {
      return (Timestamp) object;
    } else if (object instanceof Number) {
      long millis = ((Number) object).longValue();
      return new Timestamp(millis);
    }

    throw new SQLCastException(Timestamp.class);
  }

  private void checkObject(Object object, String errMessage) throws SQLCastException {
    if (object == null) {
      throw new SQLCastException(errMessage);
    }
  }

  @Override
  public Timestamp getTimestampOrDefault(Timestamp timestamp) {
    try {
      return getTimestamp();
    } catch (SQLCastException e) {
      return timestamp;
    }
  }

  @Override
  public boolean isEmpty() {
    return object == null;
  }

  @Override
  public SqlObjectMetaData getMetadata() {
    return objectMetaData;
  }

  @Override
  public String toString() {
    return "SqlObjectImpl{" +
        "object=" + object +
        ", objectMetaData=" + objectMetaData.toString() +
        '}';
  }
}
