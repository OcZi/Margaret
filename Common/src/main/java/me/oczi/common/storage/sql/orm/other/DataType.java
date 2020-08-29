package me.oczi.common.storage.sql.orm.other;

public enum DataType {
  TABLE("{table}"),
  COLUMN("{column-%s}"),
  PARAMETER(),
  PLAIN();

  private final String placeholder;


  DataType(String placeholder) {
    this.placeholder = placeholder;
  }

  DataType() {
    this.placeholder = "";
  }

  public String getPlaceholder() {
    return getPlaceholder(null);
  }

  public String getPlaceholder(Object format) {
    return format != null
        ? String.format(placeholder, format)
        : placeholder;
  }
}
