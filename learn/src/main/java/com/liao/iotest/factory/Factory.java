package com.liao.iotest.factory;

import com.liao.iotest.iinterface.*;
import com.liao.iotest.service.*;
import com.liao.iotest.util.InputUtil;

public class Factory {
    private Factory(){};

    public static INumberService getInstance(){
        return new INumberimpl();
    }

    public static IFileService getFileService(){
        return new FileServiceImpl();
    }

    public static IStringService getStringService(){
        return new StringServiceImpl();
    }

    public static IStudentService getStudenService(){
        return new StudentService();
    }

    public static IVoteService getVoteService(){
        return new VoteServiceImpl();
    }
}
