package com.hy.java.nio;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * HyByteBuffer
 *
 * @author Jie.Hu
 * @date 8/31/21 3:38 PM
 */
public class HyByteBuffer {

    /**
     * ByteBuffer read and write
     */
    public static void byteBuffer() {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        putBuffer(buffer, "test,test");
        String str1 = readBuffer(buffer);
        putBuffer(buffer, "test1");
        String str2 = readBuffer(buffer);
    }

    /**
     * put string to ByteBuffer, it will clear ByteBuffer firstly
     *
     * @param buffer
     * @param str
     * @return
     */
    private static ByteBuffer putBuffer(ByteBuffer buffer, String str) {
        if (buffer.capacity() < str.length()) {
            throw new RuntimeException();
        }
        buffer.clear();
        buffer.put(str.getBytes(Charset.forName("UTF-8")));
        return buffer;
    }

    /**
     * read String from byteBuffer
     *
     * @param buffer
     * @return
     */
    private static String readBuffer(ByteBuffer buffer) {
        return readBuffer(buffer, 10);
    }

    /**
     * read String from ByteBuffer
     *
     * @param buffer
     * @param bufferSize temporary buffer size
     * @return
     */
    private static String readBuffer(ByteBuffer buffer, int bufferSize) {
        if (bufferSize < 1) {
            throw new RuntimeException();
        }
        buffer.flip();
        int remain = 0;
        int size = bufferSize;
        byte[] resultBytes = new byte[size];
        StringBuilder builder = new StringBuilder();
        while ((remain = buffer.limit() - buffer.position()) > 0) {
            int len = Math.min(remain, size);
            buffer.get(resultBytes, 0, len);
            String str = new String(resultBytes, 0, len);
            builder.append(str);
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        byteBuffer();
    }

}
