package me.oczi.common.request.mojang;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import me.oczi.common.api.mojang.HistoryNameEntry;
import me.oczi.common.api.mojang.MojangAccount;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Implementation of ({@link AsyncMojangResolver}.
 */
public class AsyncMojangResolverImpl implements AsyncMojangResolver {
  private final ListeningExecutorService executorService;
  private final MojangResolver delegate;

  public AsyncMojangResolverImpl(ExecutorService executorService) {
    this(executorService, MojangResolver.newResolver());
  }

  public AsyncMojangResolverImpl(ExecutorService executorService,
                                 MojangResolver delegate) {
    this.executorService = MoreExecutors
        .listeningDecorator(executorService);
    this.delegate = delegate;
  }

  @Override
  public MojangAccount resolveAccount(String name)
      throws InterruptedException,
      ExecutionException,
      TimeoutException {
    ListenableFuture<MojangAccount> future =
        executorService.submit(
            () -> delegate.resolveAccount(name));
    // Hardcoded Milliseconds match with HttpURLConnection timeout.
    return future.get(8000, TimeUnit.MILLISECONDS);
  }

  @Override
  public HistoryNameEntry[] resolveHistoryName(UUID uuid)
      throws InterruptedException,
      ExecutionException,
      TimeoutException {
    ListenableFuture<HistoryNameEntry[]> future =
        executorService.submit(
            () -> delegate.resolveHistoryName(uuid));
    return future.get(8000, TimeUnit.MILLISECONDS);
  }

  @Override
  public HistoryNameEntry[] resolveHistoryName(String uuid)
      throws InterruptedException,
      ExecutionException,
      TimeoutException {
    ListenableFuture<HistoryNameEntry[]> future =
        executorService.submit(
            () -> delegate.resolveHistoryName(uuid));
    return future.get(8000, TimeUnit.MILLISECONDS);
  }

  @Override
  public void shutdown() {
    executorService.shutdown();
  }
}
