package com.liao.multithread;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

class SleepBlocked implements Runnable{
    public void run(){
        try{
            TimeUnit.SECONDS.sleep(100);
        }catch (InterruptedException e){
            System.out.println("InterruptedException");
        }
        System.out.println("Exiting SleepBlock.run()");
    }
}

class IOBlocked implements Runnable{
    private InputStream in ;
    public IOBlocked(InputStream in){this.in=in;}
    public void run(){
        try{
            System.out.println("waiting for read();");
            in.read();
        }catch (Exception e){
            if(Thread.currentThread().isInterrupted()){
                System.out.println("Interrupted from blocked I/O");
            }else {
                throw new RuntimeException();
            }
        }
        System.out.println("exiting IOBlocked.run()");
    }
}

class SynchronizedBlocked implements Runnable{
    public synchronized  void f(){
        while(true){
            Thread.yield();
        }
    }
    public SynchronizedBlocked(){
        new Thread(){
            public void run(){
                f();
            }
        }.start();
    }

    public void run(){
        System.out.println("Trying to call f()");
        f();
        System.out.println("Exitng SynchronizedBlocked.run()");
    }
}

public class Interrupting {
    private static ExecutorService exec= Executors.newCachedThreadPool();
    static void test(Runnable r) throws InterruptedException{
        Future<?> f=exec.submit(r);
        TimeUnit.SECONDS.sleep(3);
        System.out.println("开始终止");
        f.cancel(true);
        System.out.println("终止结束");
    }

    public static void main(String[] args) throws InterruptedException {
        test(new SleepBlocked());
        test(new IOBlocked(System.in));
        test(new SynchronizedBlocked());
        TimeUnit.SECONDS.sleep(3);
        System.out.println("系统终止ing");
        System.exit(0);
    }



}
