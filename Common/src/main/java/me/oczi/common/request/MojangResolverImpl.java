package me.oczi.common.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import me.oczi.common.api.mojang.HistoryNameEntry;
import me.oczi.common.api.mojang.MojangAccount;
import me.oczi.common.api.mojang.MojangApi;
import me.oczi.common.utils.CommonsUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.UUID;

/**
 * Implementation of {@link MojangResolver}.
 */
public class MojangResolverImpl implements MojangResolver {
  private final Gson gson = new GsonBuilder()
      .serializeNulls()
      .create();

  /**
   * Get data of Mojang's account.
   * @param name Name of account.
   * @return Mojang account.
   * @throws IOException If occurs a exception
   * while trying to connect o get information.
   */
  @Override
  public MojangAccount resolveAccount(String name)
      throws IOException {
    MojangAccount result = requestData(
        MojangApi.UUID.of(name),
        MojangAccount.class);
    result.setId(
        CommonsUtils
            .formatUuid(
                result.getId())
            .toString());
    return result;
  }

  /**
   * Get history name of account.
   * @param uuid UUID of account.
   * @return History name of account.
   * @throws IOException If occurs a exception
   * while trying to connect o get information.
   */
  @Override
  public HistoryNameEntry[] resolveHistoryName(UUID uuid)
      throws IOException {
    return resolveHistoryName(
        uuid.toString());
  }

  /**
   * Get history name of account.
   * @param uuid UUID of account.
   * @return History name of account.
   * @throws IOException If occurs a exception
   * while trying to connect o get information.
   */
  @Override
  public HistoryNameEntry[] resolveHistoryName(String uuid)
      throws IOException {
    return requestData(
        MojangApi.NAME.of(
            uuid.replace("-", "")),
        HistoryNameEntry[].class);
  }

  private <T> T requestData(String urlName, Class<T> clazz)
      throws IOException {
    HttpUrlConnectionBuilder connection =
        HttpUrlConnectionBuilder
            .newBuilder(urlName)
            .setMethod("GET")
            .setConnectionTimeout(8000)
            .setReadTimeout(8000);
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
