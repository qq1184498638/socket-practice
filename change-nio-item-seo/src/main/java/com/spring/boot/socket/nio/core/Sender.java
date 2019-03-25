package com.spring.boot.socket.nio.core;

import java.io.Closeable;
import java.io.IOException;

/***************************************
 * @auther: Radeon
 * @Date: 2019/3/19 15:40
 * @Description:
 ***************************************/
public interface Sender extends Closeable {
    boolean sendAsync(IoArgs args, IoArgs.IoArgsEventListener listener) throws IOException;
}
