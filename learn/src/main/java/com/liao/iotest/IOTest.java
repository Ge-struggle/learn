package com.liao.iotest;


import java.io.*;

class FileUtil{
    private File srcFile; //源文件
    private File desFile;//目标文件

    public FileUtil(File src,File dest){
        this.srcFile=src;
        this.desFile=dest;
    }
    public FileUtil(String src,String desc){
        this(new File(src),new File(desc));
    }


    public boolean copy() throws Exception {
        if(!this.srcFile.exists()){
            System.out.println("源文件不存在");
            return false;
        }
        if(!this.desFile.getParentFile().exists()){
            this.desFile.getParentFile().mkdirs();//创建父目录
        }

//        byte[] data=new byte[1024];//开辟拷贝缓冲区
//        InputStream in=null;
//        OutputStream out=null;
//        try{
//            in=new FileInputStream(this.srcFile);
//            out=new FileOutputStream(this.desFile);
//
//            //1
//            int len=0;
//            while(len !=-1){
//               len=in.read(data);
//               if(len !=-1){
//                   out.write(data,0,len);
//               }
//            }
//            //2
//            while((len=in.read(data))!=-1){
//                out.write(data,0,len);
//            }
//            //3
//            in.transferTo(out);
//
//
//            return true;
//
//        }catch (IOException e){
//            throw e;
//        }finally {
//            if(in !=null){
//                in.close();
//            }
//            if(out !=null){
//                out.close();
//            }
//        }
        return this.copyFileImpl(this.srcFile,this.desFile);
    }
    public boolean copyDir() throws Exception {
        copyImpl(this.srcFile);
        return false;
    }

    private void copyImpl(File file)  throws Exception{
        if(file.isDirectory()){//是目录
            File[] result=file.listFiles();
            if(result!=null){
                for(int x=0;x<result.length;x++){
                    copyImpl(result[x]);
                }
            }
        }else{//是文件
            //去掉目录后的新文件子路径
            String newFilePath=file.getPath().replace(this.srcFile.getPath()+File.separator,"");
            //新文件的全路径
            File newFile=new File(this.desFile,newFilePath);
            this.copyFileImpl(file,newFile);
        }
    }

    private boolean copyFileImpl(File src,File dest) throws Exception{
        if(!dest.getParentFile().exists()){
            dest.getParentFile().mkdirs();//创建父目录
        }
        InputStream in=null;
        OutputStream out=null;
        try{
            in=new FileInputStream(src);
            out=new FileOutputStream(dest);

            //3
            in.transferTo(out);
            return true;
        }catch (IOException e){
            throw e;
        }finally {
            if(in !=null){
                in.close();
            }
            if(out !=null){
                out.close();
            }
        }
    }
}




public class IOTest {

    public static void main(String[] args) throws Exception {
        if(args.length!=2){
            System.out.println("参数不正确");
            System.exit(1);
        }

        long start=System.currentTimeMillis();
        FileUtil fu=new FileUtil(args[0],args[1]);
        if(new File(args[0]).isFile()){
            //文件拷贝
            fu.copy();
        }else{
            //目录拷贝
            fu.copyDir();
        }
        System.out.println(fu.copy()? "拷贝成功！":" 拷贝失败！");
        long end=System.currentTimeMillis();


    }
}
