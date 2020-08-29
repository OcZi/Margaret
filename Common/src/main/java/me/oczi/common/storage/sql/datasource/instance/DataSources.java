package me.oczi.common.storage.sql.datasource.instance;

import me.oczi.common.storage.sql.datasource.DataSourceTypePackage;

public interface DataSources {

  static DataSourceCPBuilder newDataSourceCP(DataSourceTypePackage dataSourcePackage,
                                             String jdbcUrl) {
    return new DataSourceCPBuilderImpl(dataSourcePackage, jdbcUrl);
  }

  static JdbcUrlBuilder newJdbcUrlBuilder(DataSourceTypePackage dataSourceTypePackage) {
    return new JdbcUrlBuilderImpl(dataSourceTypePackage);
  }
}