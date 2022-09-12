package com.liao.reflective;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class CGLIBProxy {
    //此种创建代理类比较麻烦
    public static void main(String[] args) {
        Message realObject=new Message();
        Enhancer enhancer=new Enhancer();//负责代理操作的程序类
        enhancer.setSuperclass(realObject.getClass());//假定父类
        enhancer.setCallback(new CGProxy(realObject));
        Message proxyObject=(Message)enhancer.create();
        proxyObject.send();
    }
}


class CGProxy implements MethodInterceptor {
    //真实对象
    private Object target;

    public CGProxy(Object target){
        this.target=target;
    }

    //代理操作-链接
    public boolean connect(){
        System.out.println("建立链接 ");
        return true;
    }

    //代理操作-关闭
    public void close(){
        System.out.println("链接关闭");
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object returnData=null;
        if(this.connect()){
            method.invoke(this.target,objects);
            this.close();
        }

        return returnData;
    }
}


class Message{
    public void send(){
        System.out.println("发送消息");
    }
}
