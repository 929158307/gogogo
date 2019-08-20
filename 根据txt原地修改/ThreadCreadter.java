package xiexie;

import com.sun.deploy.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * @Description: 利用nio及多线程快速读取数据
 * @Author: songyang
 */
public class ThreadCreadter implements Runnable{

    private File file = null;//需要读入的文件
    private File targetFile = null;//目标路径
    private String txt = null;//母目录名字
    private String fileType = "";//文件类型
    private Charset CharType = null;//读写取文件编码
    private HashSet<String> record = null;


    //修改文件数量
    public CoutTool coutTool = CoutTool.getInstance();



    @Override
    public void run() {
        try {
            readAndWrite(file);
            System.out.println(Thread.currentThread().getName() + "执行成功");
        } catch (Exception ex) {
            record.add(file.getPath());
            System.out.println("需要手动更改，已记录" + file.getPath() +"错误原因为" + ex);
        }
    }


    //读取文件，写入在对应的新建目录
    private void readAndWrite(File file){
        boolean flag = false;
        boolean logbody = false;
        boolean funFlag = false;
        Charset charset = CharType;
        //存生成文本的string
        String content = "";
        //需要生成文件的名字
        //生成文件路径
        File newDic =  mkdirs(file);
        String goalFile = newDic.getPath() + "\\" + file.getName().split("\\.")[0] + fileType;
        //开始读入操作

        //读取这个文件的所有行
        ArrayList<String> lines = null;
        try{
            lines = (ArrayList<String>) Files.readAllLines(Paths.get(file.getAbsolutePath()),charset);//1.8可以省，默认utf-8
        }catch (Exception ex){
            System.out.println(Thread.currentThread().getName() + " 读取失败文件为" + file.getPath()+"失败，原因为该文件需要用GBK读取");
        }
//        普通
        for (String line : lines){
            if(line.contains("import")&&!funFlag){
                content +="import com.sinosoft.lis.pubfun.PubFun;" +"\r\n";
                funFlag = true;
            }
            if(flag){
                if(!line.contains(";")){
                    content += line + "\r\n";
                }else{
                    content += line.substring(0,line.length()-1) +");"+"\r\n";
                    flag = false;
                }
            }else{
                if(line.contains("logger.debug")&&line.contains(";")&&line.contains("+")&&!line.contains("//")){
                    int local = line.indexOf("(");
                    String head = line.substring(0,local);
                    String body = line.substring(local+1,line.length()).trim();
                    String newbody = body.substring(0,body.length()-1).trim();
                    content+= head + "(PubFun.logFilter(" + newbody.replace(",","+")  +");"   +"\r\n";

                }else if(line.contains("logger.debug")&&!line.contains(";")&&!line.contains("//")){
                    flag = true;
                    int local = line.indexOf("(");
                    String head = line.substring(0,local);
                    String body = line.substring(local+1,line.length()).trim();
                    String newbody = body.substring(0,body.length()).trim();
                    content+= head + "(PubFun.logFilter(" + newbody.replace(",","+")+"\r\n";
                }else{
                    content += line  +"\r\n";
                }
            }
        }
        //error
        //结束读入操作

        //开始写入操作
        try {
            //封装的写入类
            if(content.toLowerCase().contains("debug")&&content.toLowerCase().contains("+")){
                synchronized (this){
                    coutTool.countNum();
                }
                WriteTool.writeIO(new File(goalFile),content);
            }
        }catch (Exception ex){
            System.out.println("异常文件为" + new File(goalFile).getName());
        }

    }
    //生成文件夹 传入文件读的文件
    private File mkdirs(File file){
        String sourcePtah = file.getParentFile().getPath();
        File dicFile = new File(sourcePtah);
        if(!dicFile.exists()){
            dicFile.mkdirs();
        }
        return dicFile;
    }

    //获得文件
    public void setFile(File file) {
        this.file = file;
    }

    //获得文档路径
    public void setTxt(String string) {
        this.txt = string;
    }

    //获得生成文件路径
    public void setTargetFile(File file) {
        this.targetFile = file;
    }

    //文件类型
    public void setFileType(String type) {
        this.fileType = type;
    }

    //读写类型
    public void setCharType(Charset type) {
        this.CharType = type;
    }


    public void putRecord(HashSet<String> record) {
        this.record = record;
    }


}
