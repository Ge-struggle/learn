package com.liao.multithread;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Horse implements  Runnable{
    private  static int counter=0;
    private final int id=counter++;
    private int strides=0;
    private static Random rand=new Random(47);
    private static CyclicBarrier barrier;

    public Horse(CyclicBarrier b){
        barrier=b;
    }

    public synchronized  int getStrides(){return strides;}

    public void run(){
        try{
            while(!Thread.interrupted()){
                synchronized (this){
                    strides+=rand.nextInt(3);
                }
                barrier.await();
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public String toString(){
        return "Hourse"+id+" ";
    }

    public String tracks(){
        StringBuilder s=new StringBuilder();
        for(int i=0;i<this.getStrides();i++) s.append("*");
        s.append(id);
        return  s.toString();
    }

}



public class HorseRace {
    static final int FINISH_LINE=20;
    private List<Horse> horses=new ArrayList<>();
    private ExecutorService exec=Executors.newCachedThreadPool();
    private CyclicBarrier barrier;

    public HorseRace(int nHorse,final int pause){
        barrier=new CyclicBarrier(nHorse, new Runnable() {
            @Override
            public void run() {
                StringBuilder s=new StringBuilder();
                for(int i=0;i<FINISH_LINE;i++) s.append("=");
                System.out.println(s);
                for(Horse horse:horses) System.out.println(horse.tracks());
                for(Horse horse:horses){
                    if(horse.getStrides()>FINISH_LINE){
                        System.out.println(horse+"Won!");
                        exec.shutdownNow();
                        return;
                    }
                    try{
                        TimeUnit.MILLISECONDS.sleep(pause);
                    }catch (Exception e){
                        System.out.println("sleep failed");
                    }
                }

            }

        });
        for(int i=0;i<nHorse;i++){
            Horse horse=new Horse(barrier);
            horses.add(horse);
            exec.execute(horse);
        }
    }

    public static void main(String[] args) {
        new HorseRace(7,100);
    }



}
