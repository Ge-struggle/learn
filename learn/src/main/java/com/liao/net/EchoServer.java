package com.liao.net;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoServer {
    private static class  ClientThread implements Runnable{
        private Socket client=null;//描述每一个不同的客户端
        private Scanner scan=null;
        private PrintStream out=null;
        private boolean flag=true;

        public ClientThread(Socket client) throws Exception{
           this.client=client;
           this.scan=new Scanner(client.getInputStream());
           this.scan.useDelimiter("\n");
           this.out=new PrintStream(client.getOutputStream());
        }
        @Override
        public void run() {
            while(flag){
                if(scan.hasNext()){
                    String val= scan.next().trim();
                    if("ByeByeBye".equalsIgnoreCase(val)){
                        out.println("ByeByeBye.....");
                        this.flag=false;
                    }else{
                        out.println("【Echo】"+val);
                    }
                }
            }
            try{
                scan.close();
                out.close();
                client.close();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) throws Exception {
        ServerSocket server=new ServerSocket(9999);//设置服务器端口
        System.out.println("----------等待客户端链接---------");

        boolean flag=true;
        while(flag){//循环接受客户端
            Socket client= server.accept(); //有客户端链接,就启动一个线程
            new Thread(new ClientThread(client)).start();
        }
        server.close();
    }
}
