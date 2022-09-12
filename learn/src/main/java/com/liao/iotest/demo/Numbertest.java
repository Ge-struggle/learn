package com.liao.iotest.demo;

import com.liao.iotest.factory.Factory;
import com.liao.iotest.iinterface.INumberService;

public class Numbertest {
    public static void main(String[] args) {
        INumberService numberService= Factory.getInstance();
        int[] result=numberService.stat(5);
        System.out.println(result[0]);
        System.out.println(result[1]);
    }
}
