package com.liao.reflective;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Scanner;

public class ReflectionTest {

    public static void main(String[] args) {
        Scanner in=new Scanner(System.in);
        System.out.println("请输入一个类的全限定名称");
        String name=in.next();

        try{
            //获取类的Class对象
            Class cl=Class.forName(name);
            //获取父类
            Class supercl=cl.getSuperclass();
            //获取类的修饰符
            String modifier=Modifier.toString(cl.getModifiers());
            if(modifier.length()>0) System.out.println(modifier+" ");
            System.out.println("class"+name);
            if(supercl!=null && supercl!=Object.class) System.out.println(" extends "+supercl.getName());
            System.out.println("\n{\n");
            //输出构造器
            printConstructor(cl);
            System.out.println();
            //输出方法
            printMethod(cl);
            System.out.println();
            //输出字段
            printFields(cl);
            System.out.println();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void printConstructor(Class cl){
        Constructor[] constructors=cl.getDeclaredConstructors();
        for(Constructor c:constructors){
            String name=c.getName();
            System.out.println("  ");
            String modifiers=Modifier.toString(c.getModifiers());
            if(modifiers.length()>0) System.out.print(modifiers+" ");
            System.out.println(name+"(");
            Class[] paramTypes=c.getParameterTypes();
            for(int j=0;j<paramTypes.length;j++){
                if(j>0) System.out.println(" , ");
                System.out.println(paramTypes[j].getName());
            }
            System.out.println(");");
        }
    }

    public static void printMethod(Class cl){
        Method[] methods=cl.getDeclaredMethods();
        for(Method m:methods){
            Class returnType=m.getReturnType();
            String name=m.getName();
            System.out.println("  ");
            String modifiers=Modifier.toString(m.getModifiers());
            if(modifiers.length()>0) System.out.print(modifiers+" ");
            System.out.println(returnType.getName()+" "+name+"(");
            Class[] paramTypes=m.getParameterTypes();
            for(int j=0;j<paramTypes.length;j++){
                if(j>0) System.out.println(" , ");
                System.out.println(paramTypes[j].getName());
            }
            System.out.println(");");
        }
    }

    public static void printFields(Class cl){
        Field[] fields=cl.getDeclaredFields();
        for(Field f:fields){
            Class type=f.getType();
            String name=f.getName();
            System.out.print("  ");
            String modifiers=Modifier.toString(f.getModifiers());
            if(modifiers.length()>0) System.out.print(modifiers+" ");
            System.out.println(type.getName()+" "+f.getName()+";");
        }
    }


}
