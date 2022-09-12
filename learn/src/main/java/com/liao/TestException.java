package com.liao;

import java.io.Serializable;

public class TestException extends Inning implements Strom{

    public TestException() throws RaineOut,BaseBallException{}
    public TestException(String s) throws Foul,BaseBallException{}

    public void walk(){}

//    public void event() throws RaineOut{}

    public void rainHard(){}
    public void event(){}

    public void atBat() throws PopFpoul{}

    public <T extends Comparable & Serializable,V extends Comparable & Serializable> String testsss(T t,V v){
        return "";
    }


}






class BaseBallException extends Exception{}

class Foul extends BaseBallException{}

class Strike extends BaseBallException{}

abstract class Inning{
    public Inning() throws BaseBallException{}
    public void event() throws BaseBallException{}

    public abstract void atBat() throws Strike,Foul;

    public void walk(){}
}

class StormException extends Exception{}
class RaineOut extends  StormException{}
class PopFpoul extends Foul{}

interface Strom{
    public void event() throws RaineOut;
    public void rainHard() throws RaineOut;
}