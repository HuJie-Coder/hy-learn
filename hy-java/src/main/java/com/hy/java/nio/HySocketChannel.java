package com.hy.java.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * HySocketChannel
 *
 * @author Jie.Hu
 * @date 7/18/21 1:26 PM
 */
public class HySocketChannel {
    public static void main(String[] args) {
        try (ServerSocketChannel socketChannel = ServerSocketChannel.open()) {
            socketChannel.bind(new InetSocketAddress(8080));
            socketChannel.configureBlocking(false);

            Selector selector = Selector.open();
            // 注册 channel，并且指定感兴趣的事件是 Accept
            socketChannel.register(selector, SelectionKey.OP_ACCEPT);
            // 读缓存
            ByteBuffer readBuf = ByteBuffer.allocate(1024);
            // 写缓存
            ByteBuffer writeBuf = ByteBuffer.allocate(1024);

            writeBuf.put("received\n".getBytes());
            writeBuf.flip();

            while (true) {
                int readyCnt = selector.select();
                if(readyCnt == 0) {
                    continue;
                }

                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = keys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();
                    keyIterator.remove();

                    if (key.isAcceptable()) {
                        SocketChannel socket = socketChannel.accept();
                        socket.configureBlocking(false);
                        socket.register(selector, SelectionKey.OP_READ);
                        System.out.println("Connected: " + socket.getRemoteAddress() + "  " + socket.getLocalAddress());
                    } else if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        readBuf.clear();
                        int position = channel.read(readBuf);
                        System.out.println(channel.getRemoteAddress() + " Received: " + new String(readBuf.array(),0,position));
                        key.interestOps(SelectionKey.OP_WRITE);
                    } else if (key.isWritable()) {
                        writeBuf.rewind();
                        SocketChannel channel = (SocketChannel) key.channel();
                        channel.write(writeBuf);
                        key.interestOps(SelectionKey.OP_READ);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
