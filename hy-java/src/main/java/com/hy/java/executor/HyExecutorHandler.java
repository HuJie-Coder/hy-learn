package com.hy.java.executor;

import java.net.Socket;

/**
 * HyExecutorHandler
 *
 * @author Jie.Hu
 * @date 8/18/21 11:29 AM
 */
public class HyExecutorHandler implements Runnable {

    private final Socket socket;

    public HyExecutorHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // service
    }
}
