package me.oczi.common.request;

import me.oczi.common.api.mojang.HistoryNameEntry;
import me.oczi.common.api.mojang.MojangAccount;
import me.oczi.common.api.mojang.MojangApi;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

/**
 * A {@link MojangApi} resolver data.
 */
public interface MojangResolver {

  static MojangResolver newResolver() {
    return new MojangResolverImpl();
  }

  static AsyncMojangResolver newAsyncResolver(ExecutorService executorService) {
    return new AsyncMojangResolverImpl(executorService);
  }

  /**
   * Get data of Mojang's account.
   * @param name Name of account.
   * @return Mojang account.
   * @throws IOException If occurs a exception
   * while trying to connect o get information.
   */
  MojangAccount resolveAccount(String name)
      throws IOException;

  /**
   * Get history name of account.
   * @param uuid UUID of account.
   * @return History name of account.
   * @throws IOException If occurs a exception
   * while trying to connect o get information.
   */
  HistoryNameEntry[] resolveHistoryName(UUID uuid)
          throws IOException;

  /**
   * Get history name of account.
   * @param uuid UUID of account.
   * @return History name of account.
   * @throws IOException If occurs a exception
   * while trying to connect o get information.
   */
  HistoryNameEntry[] resolveHistoryName(String uuid)
              throws IOException;
}
