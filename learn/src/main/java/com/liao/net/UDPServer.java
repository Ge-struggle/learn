package com.liao.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer {
    public static void main(String[] args) throws Exception {
        DatagramSocket server=new DatagramSocket(9000);//连接到9999端口
        String str="laiozk";//要发送的消息
        DatagramPacket packet=new DatagramPacket(str.getBytes(),0,str.length(), InetAddress.getByName("LocalHost"),9999);
        server.send(packet);//发送消息
        System.out.println("消息发送完毕");

    }
}
