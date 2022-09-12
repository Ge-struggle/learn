package com.liao.reflective;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Scanner;

public class GenericReflectionTypeTest {
    public static void main(String[] args) {
        String name;
        if(args.length>0){
            name=args[0];
        }else{
            try(Scanner in =new Scanner(System.in)){
                System.out.println("输入一个类");
                name=in.next();
            }
        }

        try{
            Class<?> cl=Class.forName(name);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }



    public static void printClass(Class<?> cl){
        System.out.print(cl);

    }


    public static void printTypes(Type[] types,String pre,String sep,String suf,boolean isDefinition){
        if(pre.equals("extends") && Arrays.equals(types,new Type[]{Object.class})) return;
        if(types.length>0) System.out.print(pre);
        for(int i=0;i<types.length;i++){
            if(i>0) System.out.print(sep);
            printType(types[i],isDefinition);
        }
        if (types.length>0) System.out.print(suf);
    }

    public static void printType(Type type,boolean isDefinition){
        if(type instanceof Class){
            Class<?> t=(Class<?>)type;
            System.out.print(t.getName());
        }else if(type instanceof TypeVariable){
            TypeVariable<?> t=(TypeVariable<?>) type;
            System.out.print(t.getName());
            if(isDefinition){
                printTypes(t.getBounds()," extends ","&","",false);
            }
        }else if(type instanceof WildcardType){
            WildcardType t=(WildcardType) type;
            System.out.print("?");
            printTypes(t.getUpperBounds(),"extends","&","",false);
            printTypes(t.getLowerBounds(),"super","&","",false);
        }else if(type instanceof ParameterizedType){
            ParameterizedType t=(ParameterizedType) type;
            Type owner=t.getOwnerType();
            if(owner!=null){
                printType(owner,false);
                System.out.println(".");
            }
            printType(t.getRawType(),false);
            printTypes(t.getActualTypeArguments(),"<",",",">",false);
        }else if(type instanceof GenericArrayType){
            GenericArrayType t=(GenericArrayType) type;
            System.out.print("");
            printType(t.getGenericComponentType(),isDefinition);
            System.out.print("[]");
        }
    }






}
