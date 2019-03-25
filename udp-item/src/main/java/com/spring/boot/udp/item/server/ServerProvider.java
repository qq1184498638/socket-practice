package com.spring.boot.udp.item.server;

import com.spring.boot.udp.item.constants.UDPConstants;
import com.spring.boot.udp.item.utils.ByteUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.UUID;


/***************************************
 * @auther: Radeon
 * @Date: 2019/3/18 19:36
 * @Description:
 ***************************************/
public class ServerProvider {
    private static Provider PROVIDER_INSTANCE;


    static void start(int port) {
        stop();
        String sn = UUID.randomUUID().toString();
        Provider provider = new Provider(sn, port);
        provider.start();
        PROVIDER_INSTANCE = provider;
    }


    static void stop() {
        if (PROVIDER_INSTANCE != null) {
            PROVIDER_INSTANCE.exit();
            PROVIDER_INSTANCE = null;
        }
    }

    private static class Provider extends Thread {
        private final byte[] sn;

        private final int port;

        private boolean done;

        private DatagramSocket  ds = null;

        //存储消息的buffer
        final byte[] buffer = new byte[128];


        private Provider(String sn, int port) {
            super();
            this.sn = sn.getBytes();
            this.port = port;
        }

        @Override
        public void run() {
            super.run();
            System.out.println("UDPProvider Starter.");

            try {
                // 监听20000 端口
                ds = new DatagramSocket(UDPConstants.PORT_SERVER);

                // 接受消息的packet
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

                while (!done) {
                    // 接受
                    ds.receive(receivePacket);

                    // 打印接受的信息与发送者
                    // 发送者的IP地址
                    String clientIp = receivePacket.getAddress().getHostAddress();
                    int clientPort = receivePacket.getPort();
                    int clientDataLen = receivePacket.getLength();
                    byte[] clientData = receivePacket.getData();
                    boolean isValid = clientDataLen >= (UDPConstants.HEADER.length + 2 + 4)
                            && ByteUtils.startsWith(clientData, UDPConstants.HEADER);

                    System.out.printf("ServerProvider receive from ip: %s, port: %d, dataValid: %s \n", clientIp, clientPort, isValid);

                    if (!isValid) {
                        continue;
                    }

                    // 解析命令与回送端口
                    int index = UDPConstants.HEADER.length;
                    short cmd = (short) ((clientData[index++] << 8 | (clientData[index++] & 0xff)));

                    int responsePort = (((clientData[index++]) << 24) |
                            ((clientData[index++] & 0xff) << 16) |
                            ((clientData[index++] & 0xff) << 8) |
                            ((clientData[index] & 0xff)));

                    // 判断合法性
                    if (cmd == 1 && responsePort > 0) {
                        // 构建一份回送数据
                        ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
                        byteBuffer.put(UDPConstants.HEADER);
                        byteBuffer.putShort((short) 2);
                        byteBuffer.putInt(port);
                        byteBuffer.put(sn);
                        int len = byteBuffer.position();
                        // 直接根据发送者构建一个回送
                        DatagramPacket responsePacket = new DatagramPacket(buffer, len, receivePacket.getAddress(), responsePort);
                        ds.send(responsePacket);
                        System.out.printf("serverProvider response to : %s, port: %d, dataLen: %d", clientIp, responsePort, len);
                    }else{
                        System.out.printf("serverProvider receive cmd nonsupport; cmd: $d, port: %d", cmd, responsePort);
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                close();
            }
        }
        private void close() {
            if (ds != null) {
                ds.close();
                ds = null;
            }
        }

        // 提供退出
        private void exit() {
            done = true;
            close();
        }
    }


}
