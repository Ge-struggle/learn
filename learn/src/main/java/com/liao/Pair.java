package com.liao;

public class Pair<T> {
    private T frist;
    private T second;

    public Pair(){
        frist=null;
        second=null;
    }

    public Pair(T frist,T second){
        this.frist =frist;
        this.second=second;
    }

    public T getFrist() {
        return frist;
    }

    public void setFrist(T frist) {
        this.frist = frist;
    }

    public T getSecond() {
        return second;
    }

    public void setSecond(T second) {
        this.second = second;
    }
}
