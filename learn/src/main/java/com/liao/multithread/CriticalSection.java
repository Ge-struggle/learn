package com.liao.multithread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CriticalSection {

    static void testApproaches(PairManager pman1,PairManager pman2){
        ExecutorService exec= Executors.newCachedThreadPool();

        PairManipulator
                pm1=new PairManipulator(pman1),
                pm2=new PairManipulator(pman2);

        PairChecker
                pc1=new PairChecker(pman1),
                pc2=new PairChecker(pman2);

        exec.execute(pm1);
        exec.execute(pm2);
        exec.execute(pc1);
        exec.execute(pc2);

        try{
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            System.out.println("sleep interrupted");
        }

        System.out.println("Pm1:"+pm1+"\nPm2:"+pm2);
        System.exit(0);

    }

    public static void main(String[] args) {
        PairManager
                pman1=new PairManage1(),
                pman2=new Pairmanage2();
        testApproaches(pman1,pman2);
    }


}

class Pair{
    private int x,y;
    public Pair(int x,int y){
        this.x=x;
        this.y=y;
    }
    public Pair(){
        this(0,0);
    }

    public int getX(){return this.x;}
    public int getY(){return this.y;}

    public void incrementX(){x++;}
    public void incermentY(){y++;}

    public String toString(){
        return "x="+this.x+";y="+this.y;
    }

    public class PairValuesNotEqualException extends RuntimeException{
        public PairValuesNotEqualException(){
            super("Pair values not equals: "+Pair.this); //指向外部类的对象？
        }
    }

    public void checkState(){
        if(x!=y){
            throw new PairValuesNotEqualException();
        }
    }

}

abstract class PairManager{
    AtomicInteger checkCounter=new AtomicInteger(0);
    protected Pair p=new Pair();
    private List<Pair> storage= Collections.synchronizedList(new ArrayList<>());

    public synchronized Pair getPair(){
        return new Pair(p.getX(),p.getY());
    }
    protected void store(Pair p){
        storage.add(p);
        try{
            TimeUnit.MILLISECONDS.sleep(50);
        }catch (InterruptedException e){

        }
    }

    public abstract void increment();
}

class PairManage1 extends PairManager{
    public synchronized void increment(){
        p.incrementX();
        p.incermentY();
        store(getPair());
    }
}

class Pairmanage2 extends PairManager{
    public void increment(){
        Pair temp;
        synchronized (this){
            p.incrementX();
            p.incermentY();
            temp=getPair();
        }
        store(temp);
    }
}


class PairManipulator  implements  Runnable{
    private PairManager pm;
    public PairManipulator(PairManager pm){
        this.pm=pm;
    }

    public void run(){
        while(true){
            pm.increment();
        }
    }

    public String toString(){
        return "Pair: "+pm.getPair()+"checkCounter:"+pm.checkCounter.get();
    }
}

class PairChecker implements Runnable{
    private PairManager pm;
    public PairChecker(PairManager pm){
        this.pm=pm;
    }

    public void run(){
        while(true){
            pm.checkCounter.incrementAndGet();
            pm.getPair().checkState();
        }
    }
}
