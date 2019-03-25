package com.spring.boot.udp.item.server;

import com.spring.boot.udp.item.constants.TCPConstants;

import java.io.IOException;

/***************************************
 * @auther: Radeon
 * @Date: 2019/3/18 20:38
 * @Description:
 ***************************************/
public class Server {
    public static void main(String[] args) {
        ServerProvider.start(TCPConstants.PORT_SERVER);
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ServerProvider.stop();
    }
}
