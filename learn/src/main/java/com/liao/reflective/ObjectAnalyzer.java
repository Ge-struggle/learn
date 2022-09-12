package com.liao.reflective;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class ObjectAnalyzer {

    private ArrayList<Object> visited=new ArrayList<>();

    public String toString(Object obj){
        if(obj==null) return "null";
        if(visited.contains(obj)) return "...";//防止循环引用导致无线递归？？ 什么是循环引用？
        visited.add(obj);
        Class cl=obj.getClass();
        if(cl==String.class) return (String)obj;
        if(cl.isArray()){
            String r=cl.getComponentType()+"[]{";
            for(int i=0;i< Array.getLength(obj);i++){
                if(i>0) r+=",";
                Object val=Array.get(obj,i);
                if(cl.getComponentType().isPrimitive()) r+=val;
                else r+=toString(val);
            }
            return r+="}";
        }
        String r=cl.getName();

        do{
            r+="[";
            Field[] fields=cl.getDeclaredFields();
            AccessibleObject.setAccessible(fields,true);//可以访问私有
            for(Field f:fields){
                if(!Modifier.isStatic(f.getModifiers())){
                    if(!r.endsWith("[")) r+=",";
                    r+=f.getName()+"=";
                    try{
                        Class t=f.getType();
                        Object val=f.get(obj);
                        if(t.isPrimitive()) r+=val;
                        else r+=toString(val);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            r+="]";
            cl=cl.getSuperclass();
        }while (cl!=null);
        return r;
    }

    public static void main(String[] args) {
        ArrayList<Integer> squares=new ArrayList<>();
//        for(int i=0;i<3;i++){
//            squares.add(i*i);
//        }
        System.out.println(new ObjectAnalyzer().toString(squares));
    }
}
