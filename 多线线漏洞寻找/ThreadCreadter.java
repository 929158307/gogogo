package saomiao;

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
    private Charset CharType = null;//读写取文件编码
    private HashSet<String> record = null;

    //修改文件数量
    public CoutTool coutTool = CoutTool.getInstance();

    @Override
    public void run() {
        try {
            readAndWrite(file);
            synchronized (this){
                coutTool.countNum();
            }
            System.out.println(Thread.currentThread().getName() + "扫描 ： "+file.getName() +"成功");
        } catch (Exception ex) {
            System.out.println(Thread.currentThread().getName() + "扫描 ： "+file.getName() +"失败");
        }
    }

    //读取文件，写入在对应的新建目录
    private void readAndWrite(File file){
        Charset charset = CharType;
        //读取这个文件的所有行
        ArrayList<String> lines = null;
        try{
            lines = (ArrayList<String>) Files.readAllLines(Paths.get(file.getAbsolutePath()),charset);//1.8可以省，默认utf-8
        }catch (Exception ex){
            System.out.println(Thread.currentThread().getName() + " 读取失败文件为" + file.getPath()+"失败，原因为该文件需要用GBK读取");
        }
        //取得每一行数据
        for (String line : lines) {
            //业务操作模块！
                synchronized (this){
                    record.add(file.getPath());
                    System.out.println("发现漏洞，文件为：" + file.getPath());
                }
             }
        }

    //获得文件
    public void setFile(File file) {
        this.file = file;
    }


    //读写类型
    public void setCharType(Charset type) {
        this.CharType = type;
    }

    //记录有问题文件
    public void putRecord(HashSet<String> record) {
        this.record = record;
    }


}
