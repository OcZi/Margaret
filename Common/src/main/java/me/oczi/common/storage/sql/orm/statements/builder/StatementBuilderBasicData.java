package me.oczi.common.storage.sql.orm.statements.builder;

import me.oczi.common.storage.sql.orm.statements.data.StatementBasicData;

import java.util.concurrent.atomic.AtomicInteger;

public interface StatementBuilderBasicData extends StatementBasicData {

  StringBuilder getPatternBuilder();

  AtomicInteger getNumColumn();

  int getNumParameter();
}
