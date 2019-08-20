package xie;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description:
 * @Author: songyang
 * @Date: $date$
 */
public class ThreadMainContro {
    //参数
    private static String txt = "C:\\Users\\wxygy\\Desktop\\error.txt";  //辅助
    private static String error = "C:\\Users\\wxygy\\Desktop\\log.txt";  //辅助
    private static String fileType = ".java";//生成的文件
    private static Charset CharType =  Charset.forName("UTF-8");//读编码
    private static int threadNum = 10;//线程池最大线程数量
    private static HashSet<String> record = new HashSet<>();
    //参数结束
    private static File txtFile = new File(txt);
    private static File errorFile = new File(error);
    //文档资料
    private static ArrayList<String> paths = new ArrayList<>();
    //线程池
    private static  ExecutorService cachedThreadPool = Executors.newScheduledThreadPool(threadNum);

    //执行文件
    public static void getFiles() {
       for(String file : paths){
           //遍历所有要被修改的文件
           File sourceFile = new File(file);
           ThreadCreadter threadCreadter = new ThreadCreadter();
           //读取文件和生成文件的路径一样
           threadCreadter.setFile(sourceFile);
           //读取文件和生成文件的路径一样
           threadCreadter.setTargetFile(sourceFile);
           threadCreadter.setFileType(fileType);
           threadCreadter.setCharType(CharType);
           threadCreadter.putRecord(record);
           cachedThreadPool.execute(threadCreadter);

        }
    }

    //读取文档
    public static void readTxt(File txtFile) {
        try{
            paths = (ArrayList<String>) Files.readAllLines(Paths.get(txtFile.getAbsolutePath()),CharType);//1.8可以省，默认utf-8
        }catch (Exception ex){
            System.out.println(Thread.currentThread().getName() + " 读取失败文件为" + txtFile.getPath()+"失败，原因为该文件需要用GBK读取");
        }

    }

    public static void main(String[] args) {
        //读取txt文件
        readTxt(txtFile);
        getFiles();
        //在getfiles执行完，也就是所有任务已经分配完毕后 关掉 线程池
        cachedThreadPool.shutdown();
        while (true){
            //在所有任务执行完成后统计数量
            if(cachedThreadPool.isTerminated()){
                cachedThreadPool.shutdown();
                WriteTool.write(errorFile,record);
                System.out.println("修改文件共" + CoutTool.getInstance().getCount());
                System.out.println("需手动处理文件为" + record.size());
                break;
            }
        }
    }
}


