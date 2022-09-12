package com.liao.iotest.service;

import com.liao.iotest.demo.vo.MonitorCandidate;
import com.liao.iotest.iinterface.IVoteService;

import java.util.Arrays;

public class VoteServiceImpl implements IVoteService {
    private MonitorCandidate[] cnadidates=new MonitorCandidate[]{
            new MonitorCandidate(1,"张三",0),
            new MonitorCandidate(2,"李四",0),
            new MonitorCandidate(3,"王五",0),
            new MonitorCandidate(4,"赵六",0),
    };


    @Override
    public boolean inc(int sid) {
        for(int x=0;x<cnadidates.length;x++){
            if(this.cnadidates[x].getMid()==sid){
                this.cnadidates[x].setVote(this.cnadidates[x].getVote()+1);
                return true;
            }
        }
        return false;
    }

    @Override
    public MonitorCandidate[] result() {
        Arrays.sort(this.cnadidates);
        return this.cnadidates;
    }

    @Override
    public MonitorCandidate[] getData() {
        return this.cnadidates;
    }


}
