package me.oczi.common.storage.sql.dsl.expressions.clause.jump;

import me.oczi.common.api.sql.FinishedState;

import java.util.List;

public interface ValuesJumpClause {

  FinishedState values(Object... values);

  FinishedState values(List<?> values);
}
