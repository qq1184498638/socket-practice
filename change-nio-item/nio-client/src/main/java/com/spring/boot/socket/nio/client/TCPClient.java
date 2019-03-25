package com.spring.boot.socket.nio.client;

import com.spring.boot.socket.nio.common.domain.ServerInfo;
import com.spring.boot.socket.nio.common.utils.CloseUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

/***************************************
 * @auther: Radeon
 * @Date: 2019/3/19 15:08
 * @Description:
 ***************************************/
@Slf4j
public class TCPClient {
    private final Socket socket;
    private final ReadHandler readHandler;
    private final PrintStream printStream;

    public TCPClient(Socket socket, ReadHandler readHandler) throws IOException {
        this.socket = socket;
        this.readHandler = readHandler;
        this.printStream = new PrintStream(socket.getOutputStream());
    }
    public void exit() {
        readHandler.exit();
        CloseUtils.close(printStream);
        CloseUtils.close(socket);
    }

    public void send(String msg) {
        printStream.println(msg);
    }

    public static TCPClient startWith(ServerInfo info) throws IOException {
        Socket socket = new Socket();
        // 超时时间
        socket.setSoTimeout(3000);

        // 连接本地，端口2000；超时时间3000ms
        socket.connect(new InetSocketAddress(Inet4Address.getByName(info.getAddress()), info.getPort()), 3000);

        System.out.println("已发起服务器连接，并进入后续流程～");
        System.out.println("客户端信息：" + socket.getLocalAddress() + " P:" + socket.getLocalPort());
        System.out.println("服务器信息：" + socket.getInetAddress() + " P:" + socket.getPort());

        try {
            ReadHandler readHandler = new ReadHandler(socket.getInputStream());
            readHandler.start();
            return new TCPClient(socket, readHandler);
        } catch (Exception e) {
            System.out.println("连接异常");
            CloseUtils.close(socket);
        }

        return null;
    }
    static class ReadHandler extends Thread {
        private boolean done = false;
        private final InputStream inputStream;


        ReadHandler(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @Override
        public void run() {
            try {
                // get inputStream, use receive data
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                do {
                    String msg = null;
                    try {
                        msg = bufferedReader.readLine();
                    } catch (SocketTimeoutException e) {
                        continue;
                    }

                    if (msg == null) {
                        log.info("connect state is close, not read data");
                        break;
                    }

                    log.info("core send data : {}", msg);

                } while (!done);
            } catch (Exception e) {
                if (!done) {
                    log.error("connect exception close: {}", e.getMessage());
                }
            } finally {
                CloseUtils.close(inputStream);
            }

        }

        void exit() {
            done = true;
            CloseUtils.close(inputStream);
        }
    }
}
