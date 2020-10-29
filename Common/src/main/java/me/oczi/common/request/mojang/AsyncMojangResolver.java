package me.oczi.common.request.mojang;

import me.oczi.common.api.mojang.HistoryNameEntry;
import me.oczi.common.api.mojang.MojangAccount;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * A {@link MojangResolver} async.
 */
public interface AsyncMojangResolver {

  /**
   * Get data of Mojang's account.
   * @param name Name of account.
   * @return Mojang Account.
   * @throws InterruptedException If occurs a error in the call of the method.
   * @throws ExecutionException If occurs a error in the call of the method.
   * @throws TimeoutException If method get not return the value after the milliseconds defined.
   */
  MojangAccount resolveAccount(String name)
      throws InterruptedException,
      ExecutionException,
      TimeoutException;

  /**
   * Get history name of account.
   * @param uuid UUID of account.
   * @return History name of account.
   * @throws InterruptedException If occurs a error in the call of the method.
   * @throws ExecutionException If occurs a error in the call of the method.
   * @throws TimeoutException If method get not return the value after the milliseconds defined.
   */
  HistoryNameEntry[] resolveHistoryName(UUID uuid)
      throws InterruptedException,
      ExecutionException,
      TimeoutException;

  /**
   * Get history name of account.
   * @param uuid UUID of account.
   * @return History name of account.
   * @throws InterruptedException If occurs a error in the call of the method.
   * @throws ExecutionException If occurs a error in the call of the method.
   * @throws TimeoutException If method get not return the value after the milliseconds defined.
   */
  HistoryNameEntry[] resolveHistoryName(String uuid)
      throws InterruptedException,
      ExecutionException,
      TimeoutException;

  void shutdown();
}
