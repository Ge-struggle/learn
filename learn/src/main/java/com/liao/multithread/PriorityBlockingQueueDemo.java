package com.liao.multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

class PrioritizedTask implements Runnable,Comparable<PrioritizedTask>{
    private Random rand=new Random(47);
    private static int counter=0;
    private final int id=counter++;
    private final int priority;
    protected static List<PrioritizedTask> sequence=new ArrayList<>();

    public PrioritizedTask(int priority){
        this.priority=priority;
        sequence.add(this);
    }

    public int compareTo(PrioritizedTask other){
        return priority< other.priority ? 1:(priority>other.priority?-1:0);
    }
    public void run(){
        try{
            TimeUnit.MILLISECONDS.sleep(rand.nextInt(250));
        }catch (InterruptedException e){

        }
        System.out.println(this);
    }

    public String toString(){
        return String.format("[%1$-3d]",priority)+"Task"+id;
    }

    public String summary(){
        return "("+id+":"+priority+")";
    }

    public static class EndSentinel extends PrioritizedTask{
        private ExecutorService exec;
        public EndSentinel(ExecutorService e){
            super(-1);
            this.exec=e;
        }
        public void run(){
            int count=0;
            for(PrioritizedTask task:sequence){

                System.out.println(task.summary());
                if(++count%5==0) System.out.println();
            }
            System.out.println();
            System.out.println(this+"shutDowning");
            exec.shutdownNow();

        }
    }
}

class PrioritizedTaskProducer implements Runnable{
    private Random rand=new Random(47);
    private Queue<Runnable> queue;
    private ExecutorService exec;

    public PrioritizedTaskProducer(Queue<Runnable> q,ExecutorService e){
        this.queue=q;
        this.exec=e;
    }

    public void run(){
        for(int i=0;i<20;i++){
            queue.add(new PrioritizedTask(rand.nextInt(10)));
            Thread.yield();
        }
        try{
            for(int i=0;i<10;i++){
                TimeUnit.MILLISECONDS.sleep(250);
                queue.add(new PrioritizedTask(10));
            }
            for(int i=0;i<10;i++){
                TimeUnit.MILLISECONDS.sleep(250);
                queue.add(new PrioritizedTask(i));
            }
            queue.add(new PrioritizedTask.EndSentinel(exec));
        }catch (InterruptedException e){

        }
        System.out.println("Produce finished");
    }
}

class PrioritizedTaskConsumer implements Runnable{
    private PriorityBlockingQueue<Runnable> q;

    public PrioritizedTaskConsumer(PriorityBlockingQueue<Runnable> q){
        this.q=q;
    }


    public void run(){
        try{
            while(!Thread.interrupted()){
                q.take().run();
            }
        }catch (InterruptedException e){

        }
        System.out.println("Consumer Finished");
    }

}


public class PriorityBlockingQueueDemo {
    public static void main(String[] args) {
        Random ran=new Random(47);
        ExecutorService exec= Executors.newCachedThreadPool();
        PriorityBlockingQueue queue=new PriorityBlockingQueue();
        exec.execute(new PrioritizedTaskProducer(queue,exec));
        exec.execute(new PrioritizedTaskConsumer(queue));
    }
}
