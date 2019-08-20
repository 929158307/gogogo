import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * @Description: 利用nio及多线程快速读取数据
 * @Author: songyang
 */
public class ThreadCreadter implements Runnable{

    private File file = null;//需要读入的文件
    private File targetFile = null;//目标路径
    private String fileType = "";//文件类型
    private Charset CharType = null;//读写取文件编码


    //修改文件数量
    public CoutTool coutTool = CoutTool.getInstance();



    @Override
    public void run() {
        try {
            readAndWrite(file);
            synchronized (this){
                coutTool.countNum();
            }
            System.out.println(Thread.currentThread().getName() + "执行成功");
        } catch (Exception ex) {

        }
    }


    //读取文件，写入在对应的新建目录
    private void readAndWrite(File file){
        Charset charset = CharType;
        //存生成文本的string
        String content = "";
        //生成目标目录文件
        if (!targetFile.exists()) {
            targetFile.mkdirs();//创建文件夹target
        }
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
        //取得每一行数据
        for (String line : lines) {
            //业务操作模块！
            content += line + "\r\n";








//


        }
        //结束读入操作

        //开始写入操作
        try {
            //封装的写入类
            WriteTool.write(new File(goalFile),content);
//            FileOutputStream fout = new FileOutputStream(goalFile);
//            FileChannel fc = fout.getChannel();//获得管道
//            ByteBuffer buffer = ByteBuffer.allocate(1024);//建立缓冲
//            byte[] bytes = content.getBytes();
//            for (int i = 0; i < bytes.length; i++) {
//                if (buffer.remaining() == 0) {//当buffer剩余为0是 写入信息 然后清空，继续往缓存里面放
//                    buffer.flip();
//                    fc.write(buffer);//管道写入数据
//                    buffer.clear();
//                } else {
//                    buffer.put(bytes[i]);
//                }
//            }
//            buffer.flip();
//            fc.write(buffer);
//            fout.close();//关闭
        }catch (Exception ex){
            System.out.println("异常文件为" + new File(goalFile).getName());
        }

    }
    //生成文件夹 传入文件读的文件
    private File mkdirs(File file){
        String sourcePtah = file.getParentFile().getPath();
        File dicFile = new File(targetFile.getPath() + "\\" + sourcePtah.split("source")[1]);
        if(!dicFile.exists()){
            dicFile.mkdirs();
        }
        return dicFile;
    }

    //获得文件
    public void setFile(File file) {
        this.file = file;
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


}
