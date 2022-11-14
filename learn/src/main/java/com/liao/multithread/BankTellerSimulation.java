package com.liao.multithread;


import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Customer{
    private final int serviceTime;
    public Customer (int tm){
        serviceTime=tm;
    }
    public int getServiceTime(){
        return serviceTime;
    }
    public String toString(){
        return "["+serviceTime+"]";
    }
}

class CustomerLine extends ArrayBlockingQueue<Customer>{
    public CustomerLine(int maxLineSize){
        super(maxLineSize);
    }
    public String toString(){
        if(this.size()==0){
            return "["+"Empty"+"]";
        }
        StringBuilder result=new StringBuilder();
        for(Customer customer:this ){
            result.append(customer);
        }
        return result.toString();
    }
}

class CustomerGenerator implements Runnable{
    private CustomerLine customers;
    private static Random rand=new Random(47);
    public CustomerGenerator(CustomerLine cq){
        this.customers=cq;
    }

    public void run(){
        try {
            while(!Thread.interrupted()){
                TimeUnit.MICROSECONDS.sleep(rand.nextInt(300));
                customers.put(new Customer(rand.nextInt(1000)));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("CustomerGenerator terminating");
    }
}

class Teller implements  Runnable, Comparable<Teller>{
    private static int counter=0;
    private final int id=counter++;
    private int customersServerd=0;
    private CustomerLine customers;
    //此处的开关，指的是？
    private boolean servingCustomerLine=true;

    public Teller(CustomerLine cq){
        this.customers=cq;
    }

    public void run(){
        try{
            while(!Thread.interrupted()){
                Customer customer=customers.take();
                TimeUnit.MICROSECONDS.sleep(customer.getServiceTime());
                synchronized (this){
                    customersServerd++;
                    while(!servingCustomerLine){
                        wait();
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(this+"terminating");
    }

    public synchronized void doSomeThingOrElse(){
        this.customersServerd=0;
        this.servingCustomerLine=false;
    }

    public synchronized void serveCustomerLine(){
        //此处的断言 是指？
        assert !servingCustomerLine:"already serving: "+this;
        servingCustomerLine =true;
        notifyAll();
    }

    public String toString(){
        return "Teller"+this.id+" ";
    }
    public String shortString(){
        return "T"+id;
    }


    public synchronized  int compareTo(Teller other){
        return this.customersServerd< other.customersServerd?-1:(this.customersServerd== other.customersServerd?0:1);
    }

}

class TellerManger implements  Runnable{
    private ExecutorService exec;
    private CustomerLine customers;
    private PriorityQueue<Teller> workingTellers=new PriorityQueue<>();
    private Queue<Teller> tellersDongOtherThing=new LinkedList<>();

    private int adjustmentPeriod;
    private static Random rand=new Random(47);

    public TellerManger(ExecutorService e,CustomerLine customers,int adjustmentPeriod){
        this.exec=e;
        this.adjustmentPeriod=adjustmentPeriod;
        this.customers=customers;
        Teller teller=new Teller(customers);
        exec.execute(teller);
        workingTellers.add(teller);
    }

    //排队办理业务的人太长，需要做调整
    public void adjustTellerNumber(){
        if(customers.size()/ workingTellers.size()>2){
            //优先把摸鱼，或者做其他事情的人调过来
            if(tellersDongOtherThing.size()>0){
                Teller teller=tellersDongOtherThing.remove();
                teller.serveCustomerLine();
                workingTellers.offer(teller);
                return;
            }
            Teller teller=new Teller(customers);
            exec.execute(teller);
            workingTellers.add(teller);
            return;
        }
        //排队的人比较少
        if(workingTellers.size()>1 && customers.size()/ workingTellers.size()<2){
            this.reassignOneTeller();
        }
        //没人的时候只需要留一个值班即可
        if(customers.size()==0){
            while(workingTellers.size()>1){
                reassignOneTeller();
            }
        }
    }

    private void reassignOneTeller(){
        Teller teller=workingTellers.remove();
        teller.doSomeThingOrElse();
        tellersDongOtherThing.offer(teller);
    }

    public void run(){
        try {
            while(!Thread.interrupted()){
                TimeUnit.MILLISECONDS.sleep(adjustmentPeriod);
                adjustTellerNumber();
                System.out.println(customers+"{");
                for(Teller teller:workingTellers){
                    System.out.println(teller.shortString()+" ");
                }
                System.out.println("}");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(this+"terminating");
    }

    public String toString(){
        return "TellerManager";
    }



}






public class BankTellerSimulation {
    static final int MAX_LINE_SIZE=50;
    static final int ADJUSTMENT_PERIOD=1000;

    public static void main(String[] args) throws Exception{
        ExecutorService exec=Executors.newCachedThreadPool();
        CustomerLine customers=new CustomerLine(MAX_LINE_SIZE);
        exec.execute(new CustomerGenerator(customers));
        exec.execute(new TellerManger(exec,customers,ADJUSTMENT_PERIOD));
        System.out.println("Enter to exit");
        System.in.read();
        exec.shutdown();
    }
}
