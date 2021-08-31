package com.hy.java.nio;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

/**
 * HySelector
 *
 * @author Jie.Hu
 * @date 8/31/21 5:11 PM
 */
public class HySelector {


    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8089));
        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            int cnt = selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                keyIterator.remove();
                if (key.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    SocketAddress remoteAddress = socketChannel.getRemoteAddress();
                    System.out.println("Remote " + remoteAddress.toString().substring(1));
                    Socket socket = socketChannel.socket();
                    InputStream inputStream = socket.getInputStream();
                    byte[] bytes = new byte[1024];
                    int index = 0;
                    while ((index = inputStream.read(bytes)) > 0) {
                        String s = new String(bytes, 0, index);
                        s = s.substring(0, s.length() - 1);
                        if (s.endsWith("\r")) {
                            s = s.substring(0, s.length() - 1);
                        }
                        System.out.println(s);
                    }
                }
            }
        }
    }

}
