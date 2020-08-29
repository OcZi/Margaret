package me.oczi.common.executors;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

public abstract class TaskExecutor {
  protected final ListeningExecutorService executorService;

  protected TaskExecutor(ExecutorService executorService) {
    this.executorService = MoreExecutors.listeningDecorator(executorService);
  }

  protected TaskExecutor(ListeningExecutorService executorService) {
    this.executorService = executorService;
  }

  // Just for isolate exceptions.
  // Maybe will remove this in the future.
  protected  <T> T getFutureValue(ListenableFuture<T> future) {
    T t = null;
    try {
      t = future.get();
    }catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
    }

    return t;
  }

  public void close() {
    executorService.shutdown();
  }

  public ListeningExecutorService getExecutorService() {
    return executorService;
  }
}
