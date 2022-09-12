package com.liao.iotest.demo;

import com.liao.iotest.factory.Factory;
import com.liao.iotest.iinterface.IFileService;

import java.io.File;

public class IOSaveFile {

    //程序启动时必须保证路径存在
    static{
        File file=new File(IFileService.SAVE_DIR);
        if(!file.exists()){ //目录不存在就创建
            file.mkdirs();
        }
    }

    public static void main(String[] args) {
        IFileService ifile= Factory.getFileService();
        ifile.save();
    }
}
