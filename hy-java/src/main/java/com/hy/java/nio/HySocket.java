package com.hy.java.nio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;

/**
 * HySocketChannel
 *
 * @author Jie.Hu
 * @date 7/18/21 10:57 AM
 */
public class HySocket {


    public static void read(){
        try(ServerSocketChannel socketChannel = ServerSocketChannel.open()) {
            socketChannel.bind(new InetSocketAddress("127.0.0.1",8080));
            ServerSocket serverSocket1 = new ServerSocket();
            ServerSocket serverSocket = socketChannel.socket();
            Socket socket = serverSocket.accept();
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

    }

}
