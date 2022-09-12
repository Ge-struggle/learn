package com.liao.iotest.demo.vo;

public class Student implements Comparable<Student> {
    private String name;
    private int age;

    public String getName() {
        return name;
    }



    public Student (String name,int age){
        this.name=name;
        this.age=age;
    }

    @Override
    public String toString(){
        return "姓名："+this.name+"  年龄："+this.age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int compareTo(Student o) {
        return o.age-this.age;
    }
}
