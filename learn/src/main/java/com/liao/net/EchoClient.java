package com.liao.net;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {
    private static final BufferedReader KEYBOARD_INPUT=new BufferedReader(new InputStreamReader(System.in));
    public static void main(String[] args) throws Exception {
        Socket client=new Socket("127.0.0.1",9999);//定义服务端链接信息
        //客户端也要有输出输出操作
        Scanner scan=new Scanner(client.getInputStream());//接受服务器端的输入信息
        scan.useDelimiter("\n");
        PrintStream out=new PrintStream(client.getOutputStream());
        boolean flag=true;
        while(flag){
            String input=getString("请输入要发送的内容").trim();
            out.println(input);//加换行
            if(scan.hasNext()){//服务器端有响应
                System.out.println(scan.next());
            }
            if("ByeByeBye".equalsIgnoreCase(input)){
                flag=false;
            }
        }
        scan.close();
        out.close();
        client.close();

    }

    public static String getString(String prompt) throws Exception{
        System.out.println(prompt);
        String str=KEYBOARD_INPUT.readLine();
        return  str;
    }
}
