package com.liao.iotest.demo;

import com.liao.iotest.demo.vo.MonitorCandidate;
import com.liao.iotest.factory.Factory;
import com.liao.iotest.iinterface.IVoteService;
import com.liao.iotest.util.InputUtil;

public class MenuOfVote {
    private IVoteService voteService;


    public MenuOfVote(){
        this.voteService= Factory.getVoteService();
        this.vote();
    }

    public void vote(){
        MonitorCandidate[] candidates=this.voteService.getData();
        for(MonitorCandidate temp:candidates){
            System.out.println(temp.getMid()+" 【"+temp.getName()+"】 "+ temp.getVote()+"票");
        }
        int num=1;
        while(num!=0){
            num= InputUtil.getIntnew("请输入候选人代号，0退出");
            if(num!=0){
                if(!this.voteService.inc(num)){
                    System.out.println("此选票无效");
                }
            }
        }
        System.out.println("最终结果");
        candidates=this.voteService.result();
        System.out.println(candidates[0].getName() +"当选！");
    }


    public static void main(String[] args) {
        new MenuOfVote();
    }
}
