package me.oczi.common.storage.sql.dsl.expressions.update;

import java.util.List;

public interface UpdateStart {

  UpdateClauses set(String column, Object... parameters);

  UpdateClauses set(String column, List<?> parameters);
}
