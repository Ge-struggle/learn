package com.liao.iotest.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class InputUtil {
    private InputUtil(){};

//    private static final Scanner INPUT=new Scanner(System.in);

    public static int getInt(String prompt){
        int num=0;
        boolean flag=true;

        while(flag){
            Scanner input=new Scanner(System.in);
            System.out.println(prompt);
            if(input.hasNext("\\d+")){
                num=Integer.parseInt(input.next("\\d+")) ;
                flag=false;
            }else {
                System.out.println("请输入数字");
            }
            input.close();
        }
        return num;
    }

    public static int getIntnew(String prompt)  {
        int num=0;
        boolean flag=true;
        BufferedReader buf=new BufferedReader(new InputStreamReader(System.in));
        while(flag){
            try{
                System.out.println(prompt);
                String str=buf.readLine();
                if(str.matches("\\d+")){
                    num=Integer.parseInt(str) ;
                    flag=false;
                }else {
                    System.out.println("请输入数字");
                }
            }catch (Exception e){

            }finally {

            }

        }

        return num;
    }


    public static String getString(String prompt){
        boolean flag=true;
        String str="";
        while(flag){
            System.out.println(prompt);
            Scanner in=new Scanner(System.in);
            if(in.hasNext()){
                str=in.next().trim();
                if(!str.equals("")){
//                    str=in.next();  //为何不注释这里，就会要求一直输入
                    flag=false;
                }else {
                    System.out.println("请输入非空字符！");
                }
            }else{
                System.out.println("请输入非空字符！");
            }
        }
        return str;
    }

}
