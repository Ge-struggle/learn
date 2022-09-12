package com.liao.iotest.iinterface;

import com.liao.iotest.demo.vo.Student;

public interface IStudentService {
    public Student[] getData();//获取排序后的数据

    public void append(String str);//追加新的对象
}
