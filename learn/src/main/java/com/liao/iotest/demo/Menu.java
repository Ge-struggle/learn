package com.liao.iotest.demo;

import com.liao.iotest.factory.Factory;
import com.liao.iotest.iinterface.IStringService;
import com.liao.iotest.util.InputUtil;

import java.util.Arrays;

public class Menu {

    private IStringService stringService;
    public Menu(){
        this.stringService= Factory.getStringService();
        this.choose();
    };

    public void choose(){
        this.show();
        String choose= InputUtil.getString("请选择");
        switch (choose){
            case "1":{
                String str=InputUtil.getString("输入字符串");
                this.stringService.append(str);
                choose();//重复出现
            }
            case "2":{
                String[] result=this.stringService.recerse();
                System.out.println(Arrays.toString(result));
                choose();//重复出现
            }
            case "0":{
                System.out.println("bye");
                System.exit(1);
            }
            default:{
                System.out.println("请输入。。。");
                choose();
            }
        }
    }

    public void show(){
        System.out.println("1追加字符串数据！\n");
        System.out.println("2逆序显示字符串！\n");
        System.out.println("0结束执行！\n");
        System.out.println("\n");
    }


    public static void main(String[] args) {
        new Menu();
    }
}
