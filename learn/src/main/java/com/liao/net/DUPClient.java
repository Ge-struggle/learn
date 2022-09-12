package com.liao.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class DUPClient {
    public static void main(String[] args) throws Exception {
        DatagramSocket client=new DatagramSocket(9999);//连接到9999端口
        byte[] data=new byte[1025];//接收消息
        DatagramPacket packet=new DatagramPacket(data,data.length);
        System.out.println("客户端等待接收消息....");

        client.receive(packet);
        System.out.println("接收的内容为：" +new String(data,0, packet.getLength()));
        client.close();
    }
}
