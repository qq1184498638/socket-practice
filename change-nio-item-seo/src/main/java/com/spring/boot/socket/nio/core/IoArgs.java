package com.spring.boot.socket.nio.core;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/***************************************
 * @auther: Radeon
 * @Date: 2019/3/19 15:42
 * @Description:
 ***************************************/
public class IoArgs {
    private byte[] byteBuffer = new byte[256];
    private ByteBuffer buffer = ByteBuffer.wrap(byteBuffer);

    public int read(SocketChannel socketChannel) throws IOException {
        buffer.clear();
        return socketChannel.read(buffer);
    }

    public int write(SocketChannel socketChannel) throws IOException {
        buffer.clear();
        return socketChannel.write(buffer);
    }

    public String bufferString() {
        // remove buffer in "\n"
        return new String(buffer.array(), 0, buffer.position() - 1);
    }

    public interface IoArgsEventListener {
        void onStarted(IoArgs args);

        void onCompleted(IoArgs args);
    }
}
