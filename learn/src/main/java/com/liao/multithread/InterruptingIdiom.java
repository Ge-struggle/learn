package com.liao.multithread;


import java.sql.SQLOutput;
import java.util.concurrent.TimeUnit;

class NeedsCleanup{
    private final int id;
    public NeedsCleanup(int ident){
        this.id=ident;
        System.out.println("NeedCleanup"+id);
    }
    public void cleanUp(){
        System.out.println("CleanUp"+id);
    }
}

class Blocked3 implements Runnable{
    private volatile double d=0.0;
    public void run(){
        try{
            while(!Thread.interrupted()){
                NeedsCleanup n1=new NeedsCleanup(1);
                try{
                    System.out.println("Sleeping");
                    TimeUnit.SECONDS.sleep(1);
                    NeedsCleanup n2=new NeedsCleanup(2);
                    try{
                        System.out.println("Calculating");
                        for(int i=0;i<250000;i++){
                            d=d+(Math.E+Math.PI)/d;
                        }
                        System.out.println("Calculated");
                    }finally {
                        n2.cleanUp();
                    }
                }finally {
                    n1.cleanUp();
                }
            }
            System.out.println("exit while");
        }catch (InterruptedException e){
            System.out.println("interruptedException");
        }
    }

}

public class InterruptingIdiom {
    public static void main(String[] args) throws InterruptedException {
        Thread t=new Thread(new Blocked3());
        t.start();
        TimeUnit.SECONDS.sleep(3);
        t.interrupt();
    }



}
