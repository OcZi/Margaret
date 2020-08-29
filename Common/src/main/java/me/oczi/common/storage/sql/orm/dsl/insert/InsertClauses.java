package me.oczi.common.storage.sql.orm.dsl.insert;

public interface InsertClauses
    extends InsertIntoClause {

  InsertIntoClause orReplace();
}
