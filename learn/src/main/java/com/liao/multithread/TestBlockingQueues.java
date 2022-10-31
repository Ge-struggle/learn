package com.liao.multithread;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.*;

class LiftOffRunner implements  Runnable{
    private BlockingQueue<LiftOff> rockets;
    public LiftOffRunner(BlockingQueue<LiftOff> queue){
        this.rockets=queue;
    }

    public void add(LiftOff lo){
        try{
            rockets.put(lo);
        }catch (InterruptedException e){
            System.out.println("Interrquted");
        }
    }

    public void run(){
        try {
            while(Thread.interrupted()){
                LiftOff rocket=rockets.take();
                rocket.run();
            }
        }catch (InterruptedException e){
            System.out.println("waking from take()");
        }
        System.out.println("Exiting LiftOffRunner");
    }
}

public class TestBlockingQueues {
    static void getKey(){
        try {
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    static void getKey(String msg){
        System.out.println(msg);
        getKey();
    }


    static void test(String msg,BlockingQueue<LiftOff> queue){
        System.out.println(msg);
        LiftOffRunner runner=new LiftOffRunner(queue);
        Thread t=new Thread(runner);
        t.start();
        for(int i=0;i<5;i++){
            runner.add(new LiftOff(5));
        }

        getKey("Press 'Enter'("+msg+")");
        t.interrupt();
        System.out.println("Finishd"+msg);

    }


    public static void main(String[] args) {
        test("LinkedBlockingQueue",new LinkedBlockingDeque<LiftOff>());
//        test("ArrayBlockingQueue",new ArrayBlockingQueue<LiftOff>(3));
//        test("SynchronousQueue",new SynchronousQueue<>());
    }

}
