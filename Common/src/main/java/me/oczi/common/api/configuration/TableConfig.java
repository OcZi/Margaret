package me.oczi.common.api.configuration;

public interface TableConfig {

  String getOrDefault(String node, String defaultName);
}
