package com.spring.boot.socket.tcp.channel.constants;

/***************************************
 * @auther: Radeon
 * @Date: 2019/3/18 19:47
 * @Description:
 ***************************************/
public class UDPConstants {
    // 公用头部
    public static byte[] HEADER = new byte[]{7, 7, 7, 7, 7, 7, 7, 7};

    // 服务器固化服务端口
    public static int PORT_SERVER = 30201;

    // 客户端回送端口
    public static int PORT_CLIENT_RESPONSE = 30202;
}
