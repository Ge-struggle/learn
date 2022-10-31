package com.liao.multithread;

public class Chopstick {
    private boolean taken=false;

    public synchronized void take() throws Exception{
        while (taken){
            System.out.println("进来了！");
            wait();
        }
        this.taken=true;
    }

    public synchronized void drop(){
        taken=false;
        notifyAll();
    }
}
