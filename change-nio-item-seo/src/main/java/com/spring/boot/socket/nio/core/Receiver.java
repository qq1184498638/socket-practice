package com.spring.boot.socket.nio.core;

import java.io.Closeable;
import java.io.IOException;

/***************************************
 * @auther: Radeon
 * @Date: 2019/3/19 15:50
 * @Description:
 ***************************************/
public interface Receiver extends Closeable{
    boolean receiveAsync(IoArgs.IoArgsEventListener listener) throws IOException;
}
