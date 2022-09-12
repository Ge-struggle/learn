package com.liao.reflective;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class reflectiveDemo {

    public static void main(String[] args) {
        String value="ename:liaozk|job:worker|hireDate:2021-10-11|salary:6000";
        Emp obj=ClassInstanceFactory.create(Emp.class,value);
        System.out.println("name:"+obj.getEname()+",job "+obj.getJob()+",入职日期 "+obj.getHireDate()+",薪水 "+obj.getSalary());
    }
}


class ClassInstanceFactory{
    private ClassInstanceFactory(){};

    public static <T> T create(Class<?> clazz,String value)  {
        try {
            Object obj=clazz.getDeclaredConstructor().newInstance();
            BeanUtils.setValue(obj,value);
            return (T)obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}


class Emp{
    private String ename;
    private String job;
    private Date hireDate;
    private Double salary;


    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }
}

class BeanUtils{
    public static void setValue(Object obj,String  value)  {
        String[] results=value.split("\\|");
        for(int x=0;x<results.length;x++){
            String[] attval=results[x].split(":");
            try {
                if(attval[0].contains(".")){//属于多级
                    String[] temp=attval[0].split("\\.");
                    Object currentObj=obj;
                    //判断是否实例化
                    for(int i=0;i<temp.length-1;i++){
                        Method getMethod=currentObj.getClass().getDeclaredMethod("get"+StringUtils.initcap(temp[i]));
                        Object tempObj=getMethod.invoke(currentObj);
                        if(tempObj==null){
                            Field field=currentObj.getClass().getDeclaredField(temp[i]);
                            Method setMethod=currentObj.getClass().getDeclaredMethod("set"+StringUtils.initcap(temp[i]),field.getType());
                            Object newObj=field.getType().getDeclaredConstructor().newInstance();
                            setMethod.invoke(currentObj,newObj);
                            currentObj=newObj;
                        }else{
                            currentObj=tempObj;
                        }
                    }
                    //级联对象的属性设置
                    Field field = currentObj.getClass().getDeclaredField(temp[temp.length-1]);
                    Method method=currentObj.getClass().getDeclaredMethod("set"+StringUtils.initcap(temp[temp.length-1]),field.getType());
                    Object val=BeanUtils.getAttributeValue(field.getType().getName(),attval[1]);
                    method.invoke(obj,val);

                }else{
                    Field field = obj.getClass().getDeclaredField(attval[0]);
                    Method method=obj.getClass().getDeclaredMethod("set"+StringUtils.initcap(attval[0]),field.getType());
                    Object val=BeanUtils.getAttributeValue(field.getType().getName(),attval[1]);
                    method.invoke(obj,val);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static Object getAttributeValue(String type,String value){
        if("double".equals(type)||"java.lang.Double".equals(type)){
            return Double.parseDouble(value);
        }else if("java.util.Date".equals(type)){
            SimpleDateFormat sdf=null;
            if(value.matches("\\d{4}-\\d{2}-\\d{2}")){
                sdf=new SimpleDateFormat("yyyy-MM-dd");
                try {
                    return sdf.parse(value);
                } catch (ParseException e) {
                    return new Date();
                }
            }else{
                return new Date();
            }
        }else{
            return value;
        }

    }

}

class StringUtils{
    public static String initcap(String str){
        if(str==null || "".equals(str)){
            return str;
        }
        if(str.length()==1){
            return str.toUpperCase();
        }else{
            return str.substring(0,1).toUpperCase()+str.substring(1);
        }
    }
}