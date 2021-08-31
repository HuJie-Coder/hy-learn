package com.hy.java.nio;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;

/**
 * HyChannel
 *
 * @author Jie.Hu
 * @date 8/31/21 2:55 PM
 */
public class HyChannel {


    public static void fileChannel(){

        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile("hy-java/out/nio_random.txt","rw");
            FileChannel channel = randomAccessFile.getChannel();
            boolean isOpen = channel.isOpen();
            long size = channel.size();
            FileLock lock = channel.lock();
            lock.release();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, size);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        fileChannel();
    }
}
