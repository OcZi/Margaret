package me.oczi.common.storage.sql.function;

import me.oczi.common.storage.sql.datasource.DataSourceType;

import java.util.Collection;

public interface SqlFunctionBuilder extends SqlFunction{

  SqlFunctionBuilder addParams(Object object);

  SqlFunctionBuilder addParams(Collection<Object> objects);

  SqlFunctionBuilder addParams(Object... objects);

  SqlFunctionBuilder removeParams(Object object);

  SqlFunctionBuilder removeParams(Collection<Object> objects);

  SqlFunctionBuilder removeParams(Object... objects);

  SqlFunctionPattern getFunctionPattern();

  SqlFunctionBuilder setDataSourceType(DataSourceType type);
}
