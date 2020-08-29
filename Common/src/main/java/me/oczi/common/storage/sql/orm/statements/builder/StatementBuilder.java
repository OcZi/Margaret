package me.oczi.common.storage.sql.orm.statements.builder;

import me.oczi.common.api.sql.SqlTable;
import me.oczi.common.api.sql.StatementPattern;
import me.oczi.common.api.state.FinishedState;
import me.oczi.common.storage.sql.orm.statements.Statement;

import java.util.Collection;
import java.util.List;

public interface StatementBuilder
    extends Statement,
    StatementBuilderBasicData,
    FinishedState {

  StatementBuilder append(String... strings);

  StatementBuilder append(Collection<String> strings);

  StatementBuilder appendSpace(String... strings);

  StatementBuilder appendSpace(Collection<String> strings);

  StatementBuilder appendStatementBuilder(StatementBuilder otherBuilder);

  StatementBuilder appendColumn(StatementPattern pattern,
                                String column);

  StatementBuilder appendColumn(List<StatementPattern> patterns,
                                String column);

  StatementBuilder appendColumn(StatementPattern pattern,
                                String... column);

  StatementBuilder appendColumn(String pattern,
                                String... column);

  StatementBuilder appendColumn(String pattern,
                                List<?> columns);

  StatementBuilder appendColumn(StatementPattern pattern,
                                String column,
                                List<?> parameters);

  StatementBuilder appendColumn(String pattern,
                                String column);

  StatementBuilder appendColumn(List<StatementPattern> patterns,
                                String column,
                                List<?> parameter);

  StatementBuilder appendColumn(String pattern,
                                String column,
                                List<?> param);

  StatementBuilder appendPlain(StatementPattern pattern,
                               Object plainText);

  StatementBuilder appendPlain(StatementPattern pattern,
                               List<?> plainText);

  StatementBuilder appendPlain(String pattern,
                               List<?> plainText);

  StatementBuilder appendPlain(StatementPattern pattern,
                               Object... plainText);

  StatementBuilder appendPlain(String string,
                               Object... plainText);

  StatementBuilder appendAndSetTable(List<StatementPattern> patterns,
                                     SqlTable table);

  StatementBuilder appendAndSetTable(StatementPattern pattern,
                                     SqlTable table);

  StatementBuilder appendAndSetTable(String pattern,
                                     SqlTable table);

  StatementBuilder appendFormatBase(String base,
                                    String... formatList);

  StatementBuilder appendFormatBase(String base,
                                    List<String> formatList);

  StatementBuilder appendFormatBase(String base, String format);

  StatementBuilder formatLastClause(Object... objects);

  StatementBuilder clearStatement();

  StatementBuilder clearAnything();

  StatementBuilder setIntPlaceholders(int numColumn);

  StatementBuilder addIntPlaceholders(int numColumn);

  StatementBuilderBasicData getModifiableData();
}
