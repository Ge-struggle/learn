package com.liao.iotest.service;

import com.liao.iotest.iinterface.INumberService;
import com.liao.iotest.util.InputUtil;

public class INumberimpl implements INumberService {


    @Override
    public int[] stat(int count) {
        int[] result=new int[2];
        int[] data =new int[count];
        for (int i=0;i<data.length;i++){
            data[i]= InputUtil.getIntnew("请输入第“"+(i+1)+"“个数据");
        }
        result[0]=data[0];//最大值
        result[1]=data[1];//最小值
        for(int i=0;i<data.length;i++){
            if(data[i]>result[0]){
                result[0]=data[i];
            }
            if(data[i]<result[1]){
                result[1]=data[i];
            }
        }
        return result;
    }
}
