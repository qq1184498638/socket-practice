package com.spring.boot.udp.item.client;

import com.spring.boot.udp.item.domain.ServerInfo;

import java.io.IOException;

/***************************************
 * @auther: Radeon
 * @Date: 2019/3/18 21:19
 * @Description:
 ***************************************/
public class Client {
    public static void main(String[] args) {
        ServerInfo info = ClientSearcher.searchServer(10000);
        System.out.println("Server:" + info);
    }
}
