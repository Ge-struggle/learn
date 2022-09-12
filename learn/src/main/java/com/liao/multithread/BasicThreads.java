package com.liao.multithread;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class BasicThreads extends Thread {
//    private ReentrantLock lock=new ReentrantLock();
//    public void untimed(){
//        boolean captured=lock.tryLock();
//        try{
//            System.out.println("tryLocked():"+captured);
//        }finally {
//            if(captured){
//                lock.unlock();
//            }
//        }
//    }
//
//    public void timed(){
//        boolean captured=false;
//        try{
//            captured=lock.tryLock(2,TimeUnit.SECONDS);
//        }catch (InterruptedException e){
//            throw new RuntimeException(e);
//        }
//        try{
//            System.out.println("tryLock(2,TimeUnit.SECONDS):"+captured);
//        }finally {
//            if(captured) lock.unlock();
//        }
//    }
//
//    public static void main(String[] args) {
//        final BasicThreads al=new BasicThreads();
//        al.untimed();
//        al.timed();
//
//        new Thread(){
//            {
//                setDaemon(true);
//            }
//            public void run(){
//                al.lock.lock();
//                System.out.println("acquired");
//            }
//        }.start();
//        Thread.yield();
//        al.untimed();
//        al.timed();
//    }


    private static final int SIZE=10;
    private static CircularSet serials=new CircularSet(1000);
    private static ExecutorService exec=Executors.newCachedThreadPool();

    static class SerialChecker implements Runnable{

        @Override
        public void run() {
            while(true){
                int serial=SerialNumberGenerator.nextSerialNumber();
                if(serials.contains(serial)){
                    System.out.println("Duplicate:" +serial);
                    System.exit(0);
                }
                serials.add(serial);
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        for(int i=0;i<SIZE; i++){
            exec.execute(new SerialChecker());
            if(args.length>0){
                TimeUnit.SECONDS.sleep( new Integer(args[0]));
                System.out.println(" not detected duplicated");
                System.exit(0);
            }
        }
    }




}


class LiftOff implements Runnable{
    protected int countDown=10;
    private static int taskCount=0;
    private final int id=taskCount++;


    public LiftOff(){}

    public LiftOff(int countDown){
        this.countDown=countDown;
    }

    public String status(){
        return "#"+id+"("+ (countDown>0? countDown:"LiftOff!")+")";
    }

    @Override
    public void run() {
        while(countDown-->0){
            System.out.println(this.status());
            try {
                int x=(int)Math.random()*10;
                TimeUnit.SECONDS.sleep(x);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Practice1 implements Runnable{

    Practice1(){
        System.out.println("构造器启动");
    }

    @Override
    public void run() {
        for(int i=0;i<3;i++){
            System.out.println("文明其精神，野蛮其体魄");
            Thread.yield();
        }
        System.out.println( "run结束了");

    }
}

class Ex2FibonacciA implements Runnable {
    private int n = 0;
    public Ex2FibonacciA(int n) {
        this.n = n;
    }
    private int fib(int x) {
        if(x < 2) return 1;
        return fib(x - 2) + fib(x - 1);
    }
    public void run() {
        for(int i = 0; i < n; i++)
            System.out.print(fib(i) + " ");
        System.out.println();
    }
}


class CallableDemo implements Callable<Integer>{

    private int n;

    CallableDemo(int i){
        this.n=i;
    }

    @Override
    public Integer call() throws Exception {
        for(int i = 0; i < n; i++){
            System.out.println(fib(n));
        }
        int x=(int)Math.random()*10;
        TimeUnit.SECONDS.sleep(x);
        return x;
    }

    private int fib(int x) {
        if(x < 2) return 1;
        return fib(x - 2) + fib(x - 1);
    }
}


class DaemonThreadFactory implements ThreadFactory{

    @Override
    public Thread newThread(Runnable r) {
        Thread t=new Thread(r);
        t.setDaemon(true);
        return t;
    }
}


class Daemon implements  Runnable{
    private Thread[] t=new Thread[10];
    @Override
    public void run() {
        for(int i=0;i<t.length;i++){
            t[i]=new Thread(new DaemonSpawn());
            t[i].start();
            System.out.println("DaemonSpawn"+i+"started");
        }

        for(int i=0;i<t.length;i++){
            System.out.println("t"+i+"isDaemon="+t[i].isDaemon());
        }
    }
}

class DaemonSpawn implements Runnable{

    @Override
    public void run() {
        while(true){
            Thread.yield();
        }
    }
}



class ExceptionThread2 implements Runnable{

    @Override
    public void run() {
        Thread t=Thread.currentThread();
        System.out.println("run by"+t);
        throw  new RuntimeException();
    }
}

class ThreadExceptionHandler implements Thread.UncaughtExceptionHandler{

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("catch Thread Exception "+e);
    }
}

class HandlerExceptionFactory implements ThreadFactory{

    @Override
    public Thread newThread(Runnable r) {
        Thread t=new Thread(r);
        t.setUncaughtExceptionHandler(new ThreadExceptionHandler());
        return t;
    }
}


class CircularSet{
    private int[] array;
    private int len;
    private int index=0;

    public CircularSet(int size){
        array=new int[size];
        len=size;
        for(int i=0;i<size;i++){
            array[i]=-1;
        }
    }

    public synchronized  void add(int i){
        array[index]=i;
        index=++index % len; // 为了循环
    }

    public synchronized  boolean contains(int val){
        for(int i=0;i<len;i++){
            if(array[i]==val) return true;
        }
        return false;
    }

}

class SerialNumberGenerator{
    private static volatile  int serialNumber=0;
    public static synchronized int nextSerialNumber(){
        return serialNumber++;
    }
}



