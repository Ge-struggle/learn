package com.liao.iotest.service;

import com.liao.iotest.demo.vo.Student;
import com.liao.iotest.iinterface.IStudentService;
import com.liao.iotest.util.FileUtil;

import java.io.File;
import java.lang.reflect.Array;
import java.util.Arrays;

public class StudentService implements IStudentService {
    private String content;
    private Student[] students;
    private static final File SAVE_FILE=new File("F:"+File.separator+"liaozk"+File.separator+"liazk.text");

    public StudentService(){
        this.content=FileUtil.load(SAVE_FILE);
        this.handle();
    }

    private void handle(){//进行字符串处理
        if(this.content ==null || "".equals(this.content)){
            return;
        }
        String[] result=this.content.split("\\|");
        this.students=new Student[result.length];
        for(int x=0;x< result.length;x++){
            String[] temp=result[x].split(":");
            this.students[x]=new Student(temp[0],Integer.parseInt(temp[1]));

        }
    }


    @Override
    public Student[] getData() {
        Arrays.sort(this.students);
        return this.students;
    }

    @Override
    public void append(String str) {
        if(str.startsWith("|")){
            str=str.substring(1);
        }
        if(!str.endsWith("|")){
            str=str+"|";
        }
        FileUtil.append(SAVE_FILE,str);
    }
}
