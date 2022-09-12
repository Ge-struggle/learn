package com.liao.iotest.demo.vo;

public class MonitorCandidate implements Comparable<MonitorCandidate>{
    private String name;
    private int vote;
    private int mid;

    public MonitorCandidate(int mid,String name,int vote){
        this.mid=mid;
        this.name=name;
        this.vote=vote;
    }


    public String toString(){
        return this.mid+"姓名："+this.name+" 票数："+this.vote;
    }




    public int compareTo(MonitorCandidate stu){
        return stu.vote-this.vote;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }
}
