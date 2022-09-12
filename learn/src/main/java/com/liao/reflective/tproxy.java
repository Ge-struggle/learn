package com.liao.reflective;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//先接口
interface IMessage{
    public void send();
}

//真实业务类
class MeaageReal implements IMessage{

    @Override
    public void send() {
        System.out.println("消息发送");
    }
}

//发送消息需要连接等其他操作
//代理类
class DynamicProxy implements InvocationHandler {
    //代理对象
    private Object target;

    //进行真实业务对象与代理业务对象之间的绑定
    //target 真实业务对象，返回的是Proxy 生产的代理业务对象
    public Object bind(Object target){
        this.target=target;
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),this);
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
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("执行方法**"+method);
        Object returnData=null;
        if(this.connect()){
            returnData=method.invoke(this.target,args);
            this.close();
        }
        return returnData;
    }
}


public class tproxy {
    public static void main(String[] args) {
        IMessage msg=(IMessage) new DynamicProxy().bind(new MeaageReal());
        msg.send();
    }
}
