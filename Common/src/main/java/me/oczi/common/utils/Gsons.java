package me.oczi.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.oczi.common.request.HttpUrlConnectionBuilder;

import java.io.BufferedReader;
import java.io.IOException;

public interface Gsons {
   Gson gson = new GsonBuilder()
      .serializeNulls()
      .create();

  static <T> T parseConnection(HttpUrlConnectionBuilder connection, Class<T> clazz)
      throws IOException {
    StringBuilder builder = new StringBuilder();
    try (final BufferedReader request = connection.request()) {
      String line;
      while ((line = request.readLine()) != null) {
        builder.append(line.trim());
      }
    }
    return gson.fromJson(
        builder.toString(), clazz);
  }
}
