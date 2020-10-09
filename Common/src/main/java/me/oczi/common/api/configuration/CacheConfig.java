package me.oczi.common.api.configuration;

public interface CacheConfig {

  boolean isGarbage();

  int getPlayerTimeOut();

  int getPartnerTimeOut();

  long getPartnerTopRefresh();

  int getPartnerTopMaxEntries();

  int getPartnerTopEntriesPerPage();
}
