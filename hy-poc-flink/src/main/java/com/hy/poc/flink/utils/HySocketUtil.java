package com.hy.poc.flink.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * HySocketUtil
 *
 * @author Jie.Hu
 * @date 8/8/21 12:46 PM
 */
public class HySocketUtil {

    private static final Integer DEFAULT_BIND_PORT = 8989;

    public static void startSocketServer() {
        startSocketServer(DEFAULT_BIND_PORT);
    }

    public static void startSocketServer(int port) {
        new Thread(() -> {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(port);
                while (true) {
                    Socket socket = serverSocket.accept();
                    System.out.println("accept new connection");
                    OutputStream outputStream = socket.getOutputStream();
                    int count = 0;
                    while (true) {
                        outputStream.write(String.join(",", String.valueOf(count++), String.valueOf(System.currentTimeMillis())).getBytes());
                        outputStream.write("\n".getBytes());
                        outputStream.flush();
                        Thread.sleep(100);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();
    }


}
