package com.hy.poc.flink.mock;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * OneServerSocket
 *
 * @author Jie.Hu
 * @date 8/9/21 3:48 PM
 */
public class MultiServerSocket {

    public static Integer startPort = 9500;

    public static Long minPauseMiles = 50L;

    public static void mock(int times) throws InterruptedException {
        for (int i = 0; i < times; i++) {
            Thread.sleep(1000);
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        ServerSocket serverSocket = new ServerSocket(startPort, 3);
                        System.out.println("start port " + startPort);
                        startPort++;
                        while (true) {
                            Socket socket = serverSocket.accept();
                            System.out.println(socket.getInetAddress().toString().substring(1) + ":" + socket.getPort() + " Connected");
                            OutputStream output = socket.getOutputStream();
                            int count = 0;
                            while (true) {
                                output.write(String.join(",", "number", String.valueOf(count++), "jayden\n").getBytes());
                                Thread.sleep(minPauseMiles);
                                if (count % 20 == 0) {
                                    count = 0;
                                }
                            }

                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            };
            thread.start();
            minPauseMiles += 100;
        }

    }

    public static void main(String[] args) throws InterruptedException {
        int times = 1;
        if (args.length > 0 && Integer.parseInt(args[0]) > 1) {
            times = Integer.parseInt(args[0]);
        }
        mock(times);
    }
}
