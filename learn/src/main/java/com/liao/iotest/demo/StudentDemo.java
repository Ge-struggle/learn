package com.liao.iotest.demo;

import com.liao.iotest.factory.Factory;

import java.util.Arrays;

public class StudentDemo {
    public static void main(String[] args) {
        System.out.println(Arrays.toString(Factory.getStudenService().getData()));
    }
}
