package com.hy.java.nio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * HySocketChannel
 *
 * @author Jie.Hu
 * @date 7/18/21 10:57 AM
 */
public class HySocket {

    public static void handler(final Socket socket) {
        // 一个连接一个线程
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();
                    System.out.println(socket.getInetAddress().toString().substring(1) + ":" + socket.getPort() + " Connected");
                    byte[] buffer = new byte[4 * 1024 * 1024];
                    // 监听 tcp 的输入缓存，直到有数据进来
                    int position = inputStream.read(buffer);
                    StringBuilder builder = new StringBuilder();
                    while (position > 0) {
                        String message = new String(buffer, 0, position);
                        builder.append(message);
                        outputStream.write(("Socket:" + message).getBytes());
                        position = inputStream.read(buffer);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (socket != null) {
                        System.out.println(socket.getInetAddress().toString().substring(1) + ":" + socket.getPort() + " Exited");
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        };
        thread.start();
    }

    public static void socketServer() {
        try {
            // 监听本地 8080 端口，最多三个客户端连接进来
            ServerSocket serverSocket = new ServerSocket(8080, 3, InetAddress.getLocalHost());
            boolean isRun = true;
            while (isRun) {
                // 监听端口，直到有连接进来，否则一直阻塞
                Socket socket = serverSocket.accept();
                handler(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        socketServer();

    }

}
