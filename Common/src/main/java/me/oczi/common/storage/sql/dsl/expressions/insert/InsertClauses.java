package me.oczi.common.storage.sql.dsl.expressions.insert;

public interface InsertClauses
    extends InsertIntoClause {

  InsertIntoClause orReplace();
}
