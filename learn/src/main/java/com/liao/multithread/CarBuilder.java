package com.liao.multithread;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

//定义一个汽车实体类
class Car{
    private final int id;
    private boolean
        engine=false,driveTrain=false,wheels=false;

    public Car(int idn){
        this.id=idn;
    }
    public Car(){
        this.id=-1;
    }

    public synchronized int getId(){return this.id;}
    public synchronized void addEngine(){this.engine=true;}
    public synchronized void addDriveTrain(){this.driveTrain=true;}
    public synchronized void addWheels(){this.wheels=true;}
    public synchronized String toString(){
        return "Car"+this.id+"{engine:"+this.engine+"driveTrain:"+this.driveTrain+"wheels:"+this.wheels+"}";
    }
}

class CarQueue extends LinkedBlockingQueue<Car>{}

//底盘构造
class ChassisBuilder implements Runnable{
    private CarQueue carQueue;
    private int counter=0;

    public ChassisBuilder(CarQueue cq){
        this.carQueue=cq;
    }

    public void run(){
        try{
            while(!Thread.interrupted()){
                //模拟底盘制造
                TimeUnit.MILLISECONDS.sleep(500);
                Car c=new Car(counter++);
                //放进汽车队列
                carQueue.put(c);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("ChassisBuild End");
    }
}

//组装者
class Assembler implements Runnable{
    private CarQueue chassisQueue,finishingQueue;
    private Car car;
    private CyclicBarrier barrier=new CyclicBarrier(4);
    private RobotPool robotPool;
    public Assembler(CarQueue ca,CarQueue fq,RobotPool rp ){
        this.chassisQueue=ca;
        this.finishingQueue=fq;
        this.robotPool=rp;
    }

    public Car car(){return this.car;}

    public CyclicBarrier barrier(){return this.barrier;}

    public void run(){
        try{
            while(!Thread.interrupted()){
                car=chassisQueue.take();
                robotPool.hire(EngineRobot.class,this);
                robotPool.hire(DriveTrainRobot.class,this);
                robotPool.hire(WheelRobot.class,this);
                barrier.await();
                finishingQueue.put(car);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Assembler off");
    }
}

class Reporter implements Runnable{
    private CarQueue carQueue;
    public Reporter(CarQueue cq){
        this.carQueue=cq;
    }
    public void run(){
        try{
            while(!Thread.interrupted()){
                System.out.println(carQueue.take());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Reporter off");
    }
}

//开始定义机器人
abstract class Robot implements Runnable{
    private RobotPool pool;

    public Robot (RobotPool rp){this.pool=rp;}

    protected Assembler assembler;

    public Robot assignAssembler(Assembler assembler){
        this.assembler=assembler;
        return this;
    }

    private boolean engage=false;

    public synchronized void engage(){
        this.engage=true;
        notifyAll();
    }

    abstract  protected void performService();

    private synchronized void powerDown() throws Exception{
        this.engage=false;
        assembler=null;
        pool.release(this);
        while(this.engage==false){
            wait();
        }
    }

    //模板方法
    public void run(){
        try{
            this.powerDown();
            while(!Thread.interrupted()){
                performService();
                assembler.barrier().await();
                this.powerDown();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(this+"Off");
    }

    public String toString(){
        return getClass().getName();
    }
}

class EngineRobot extends Robot{
    public EngineRobot(RobotPool pool){
        super(pool);
    }

    protected void performService(){
        System.out.println(this+"installing engine");
        assembler.car().addEngine();
    }

}

class DriveTrainRobot extends Robot{
    public DriveTrainRobot(RobotPool pool){
        super(pool);
    }

    protected void performService(){
        System.out.println(this+"installing DriveTrain");
        assembler.car().addDriveTrain();
    }

}

class WheelRobot extends Robot{
    public WheelRobot(RobotPool pool){
        super(pool);
    }

    protected void performService(){
        System.out.println(this+"installing Wheel");
        assembler.car().addWheels();
    }

}

class RobotPool{
    private Set<Robot> pool=new HashSet<>();
    public synchronized void add(Robot r){
        pool.add(r);
        notifyAll();
    }

    public synchronized void hire(Class<? extends Robot> robotType,Assembler d) throws InterruptedException{
        for(Robot r:pool){
            if(r.getClass().equals(robotType)){
                pool.remove(r);
                r.assignAssembler(d);
                r.engage();
                return;
            }
            wait();
        }
        hire(robotType,d);
    }
    public synchronized void release(Robot r){
        this.add(r);
    }


}

public class CarBuilder {

    public static void main(String[] args) throws Exception{
        CarQueue chassisQueue=new CarQueue(),
                finishingQueue=new CarQueue();

        ExecutorService exec= Executors.newCachedThreadPool();
        RobotPool robotPool=new RobotPool();
        exec.execute(new EngineRobot(robotPool));
        exec.execute(new DriveTrainRobot(robotPool));
        exec.execute(new WheelRobot(robotPool));
        exec.execute(new Assembler(chassisQueue,finishingQueue,robotPool));
        exec.execute(new Reporter(finishingQueue));
        exec.execute(new ChassisBuilder(chassisQueue));
        TimeUnit.SECONDS.sleep(7);
        exec.shutdownNow();
    }

}
