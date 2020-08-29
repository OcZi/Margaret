package me.oczi.common.storage.sql.orm.dsl.clause.jump;

import java.util.List;

public interface WhereBasicJumpClause<C> {

  C where(String column, Object... parameters);

  C where(String column, List<?> parameters);

  C whereNot(String column, Object... parameters);

  C whereNot(String column, List<?> parameters);

  /*
  C whereInverted(Object value, String... columns);

  C whereNotInverted(Object value, String... columns);
  */
}
