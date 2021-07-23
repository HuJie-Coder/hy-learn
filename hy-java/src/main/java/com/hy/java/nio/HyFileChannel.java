package com.hy.java.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Objects;

/**
 * HyFileChannel
 *
 * @author Jie.Hu
 * @date 7/18/21 6:31 AM
 */
public class HyFileChannel {

    /**
     * write
     */
    public static void write() {
        try (FileOutputStream outputStream = new FileOutputStream("hy-java/out/nio_out.txt")) {
            FileChannel channel = outputStream.getChannel();
            ByteBuffer buffer = ByteBuffer.wrap("Jayden".getBytes());
            channel.write(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * read
     */
    public static void read() {
        try (FileInputStream inputStream = new FileInputStream("hy-java/out/nio_input.txt")) {
            FileChannel channel = inputStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while (channel.read(buffer) > 0) {
                byte[] array = buffer.array();
                String str = new String(array, 0, buffer.position());
                System.out.println(str);
                buffer.clear();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * read and write
     */
    public static void randomAccess() {

        FileChannel channel = null;
        try {
            RandomAccessFile accessFile = new RandomAccessFile("hy-java/out/nio_random.txt", "rw");
            channel = accessFile.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(7);
            channel.read(buffer);
            byte[] array = buffer.array();
            String str = new String(array, 0, buffer.position());
            System.out.println(str);
            for (int i = 0; i < array.length; i++) {
                array[i] = (byte) (array[i] + 10);
            }
            buffer.flip();
            channel.write(buffer);
            buffer.clear();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(channel) && channel.isOpen()) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * map mirror
     * FileChannel.MapMode.READ_ONLY 只能读，如果写会抛出异常
     * FileChannel.MapMode.READ_WRITE 可读可写
     * FileChannel.MapMode.PRIVATE 可读可写，不会修改文件内容，Copy-On-Write
     */
    public static void channelMap() {

        FileChannel channel = null;
        try (RandomAccessFile accessFile = new RandomAccessFile("hy-java/out/nio_map.txt", "rw");) {
            channel = accessFile.getChannel();
            int size = 7;
            MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, size);
            for(int i = 0;i < mappedByteBuffer.capacity();i++) {
                int index = mappedByteBuffer.capacity() - size + i;
                byte byt = mappedByteBuffer.get(index);
                mappedByteBuffer.put(index,(byte) (byt + 1));
            }
            mappedByteBuffer.force();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (Objects.nonNull(channel) && channel.isOpen()) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
//        write();
        read();
//        randomAccess();
//        channelMap();
    }
}
