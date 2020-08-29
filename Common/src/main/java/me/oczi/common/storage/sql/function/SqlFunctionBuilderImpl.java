package me.oczi.common.storage.sql.function;

import me.oczi.common.storage.sql.datasource.DataSourceType;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SqlFunctionBuilderImpl implements SqlFunctionBuilder {
  private final SqlFunctionPattern pattern;
  private final List<Object> params;

  private DataSourceType type;

  public SqlFunctionBuilderImpl(SqlFunctionPattern pattern) {
    this.pattern = pattern;
    this.params = Collections.emptyList();
  }

  public SqlFunctionBuilderImpl(SqlFunctionPattern pattern,
                                Object... params) {
    this.pattern = pattern;
    this.params = Arrays.asList(params);
  }

  @Override
  public SqlFunctionPattern getFunctionPattern() {
    return pattern;
  }

  @Override
  public SqlFunctionBuilder addParams(Object object) {
    params.add(object);
    return this;
  }

  @Override
  public SqlFunctionBuilder addParams(Object... objects) {
    return addParams(Arrays.asList(objects));
  }

  @Override
  public SqlFunctionBuilder addParams(Collection<Object> objects) {
    params.addAll(objects);
    return this;
  }

  @Override
  public SqlFunctionBuilder removeParams(Object object) {
    params.remove(object);
    return this;
  }

  @Override
  public SqlFunctionBuilder removeParams(Object... objects) {
    return removeParams(Arrays.asList(objects));
  }

  @Override
  public SqlFunctionBuilder removeParams(Collection<Object> objects) {
    params.removeAll(objects);
    return this;
  }

  @Override
  public List<Object> getParams() {
    return params;
  }

  @Override
  public SqlFunctionBuilder setDataSourceType(DataSourceType type) {
    this.type = type;
    return this;
  }

  @Override
  public String getFunction() {
    return pattern.getByDatabase(type);
  }
}
