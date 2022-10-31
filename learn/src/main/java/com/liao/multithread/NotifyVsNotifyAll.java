package com.liao.multithread;


import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Blocker{

    synchronized void waitingCall(){
        try{
            while(!Thread.interrupted()){
                wait();
                System.out.println(Thread.currentThread()+" 当前线程 ");
            }
        }catch (InterruptedException e){
            //nothing
        }
    }

    synchronized void prod(){super.notify();}
    synchronized void prodAll(){super.notifyAll();}

}


class Task1 implements Runnable{
    static Blocker blocker=new Blocker();
    public void run(){
        blocker.waitingCall();
    }
}

class Task2 implements Runnable{
    static Blocker blocker=new Blocker();
    public void run(){
        blocker.waitingCall();
    }
}

public class NotifyVsNotifyAll {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec= Executors.newCachedThreadPool();
        for(int i=0;i<5;i++) exec.execute(new Task1());
        exec.execute(new Task2());
        Timer timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            boolean prod=true;
            @Override
            public void run() {
                if(prod){
                    System.out.println("notify");
                    Task1.blocker.prod();
                    prod=false;
                }else{
                    System.out.println("notifyAll");
                    Task1.blocker.prodAll();
                    prod=true;
                }
            }
        },400,400);

        TimeUnit.SECONDS.sleep(5);
        timer.cancel();
        System.out.println("Timer canceled");
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.println("Task2 notifyAll");
        Task2.blocker.prodAll();
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.println("shutting down");
        exec.shutdown();
    }
}
