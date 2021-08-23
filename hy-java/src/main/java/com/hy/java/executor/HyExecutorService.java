package com.hy.java.executor;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * HyExecutorService
 *
 * @author Jie.Hu
 * @date 8/18/21 11:24 AM
 */
public class HyExecutorService implements Executor {

    private final ServerSocket serverSocket;

    private final ExecutorService pool;

    private final static int DEFAULT_POOL_SIZE = 10;

    public HyExecutorService(int port) throws IOException {
        this(port, DEFAULT_POOL_SIZE);
    }

    public HyExecutorService(int port, int poolSize) throws IOException {
        serverSocket = new ServerSocket(port);
        pool = Executors.newFixedThreadPool(poolSize);
    }

    public void execute() {
        try {
            for (; ; ) {
                execute(new HyExecutorHandler(serverSocket.accept()));
            }
        } catch (IOException e) {
            e.printStackTrace();
            pool.shutdown();
        }
    }

    @Override
    public void execute(Runnable task) {
        pool.execute(task);
    }
}
