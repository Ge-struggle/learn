package com.liao.reflective;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Annotation {
    public static void main(String[] args)   {
        try{
            new MessageService().send("HELLO");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

@Retention(RetentionPolicy.RUNTIME)
@interface UseMessage{
    public Class<?> clazz();
}

@UseMessage(clazz = MessageImpl.class)
class MessageService{
    private IMessageA message;
    public MessageService() throws Exception {
        UseMessage use=MessageService.class.getAnnotation(UseMessage.class);
        this.message=(IMessageA)Factoory.getInstance(use.clazz());
    }
    public void send(String msg){
        this.message.send(msg);
    }
}







interface IMessageA{
    public void send(String msg);
}

class MessageImpl implements IMessageA{

    public MessageImpl(){};

    @Override
    public void send(String msg) {
        System.out.println("消息发送"+msg);
    }

}


//中介类
class MessageProxy implements InvocationHandler {
    private Object target;
    public Object bind(Object target){
        this.target=target;
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),this);
    }

    public boolean connection(){
        System.out.println("建立链接");
        return true;
    }
    public void close(){
        System.out.println("链接关闭");
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args)  {
        Object re=null;
        try{
            if(this.connection()){
                re= method.invoke(this.target,args);
            }else{
                throw new Exception("  ");
            }
        }catch (Exception e){

        }finally {
            this.close();
        }
        return re;

    }
}

class Factoory{
    private  Factoory(){};

    public static <T> T getInstance(Class<T> clazz) throws Exception {
        return (T)new MessageProxy().bind(clazz.getDeclaredConstructor().newInstance());
    }
}