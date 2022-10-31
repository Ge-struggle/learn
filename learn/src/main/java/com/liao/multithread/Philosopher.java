package com.liao.multithread;


import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Philosopher implements Runnable {
    private Chopstick left;
    private Chopstick right;

    private final int id;
    private final int ponderFactor;

    private Random rand=new Random(47);

    private void pause() throws InterruptedException{
        if(ponderFactor==0) return;
        TimeUnit.MILLISECONDS.sleep(rand.nextInt(ponderFactor*250));
    }

    public Philosopher(Chopstick left,Chopstick right,int ident,int ponder){
        this.left=left;
        this.right=right;
        this.id=ident;
        this.ponderFactor=ponder;
    }

    public void run(){
        try{
            while(!Thread.interrupted()){
                System.out.println(this+"  thinking");
                this.pause();
                System.out.println(this+"  grabbing rigth");
                this.right.take();
                System.out.println(this+"  grabbing left");
                this.left.take();
                System.out.println(this+"  eating");
                this.pause();
                right.drop();
                left.drop();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String toString() {return "Philosopher  "+this.id;}
}
