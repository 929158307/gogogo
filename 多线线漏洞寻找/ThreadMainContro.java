package saomiao;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
    private static String dicName = "E:\\workspace\\lis7tww\\lis7"; //目标文件
    private static String succName = "C:\\Users\\wxygy\\Desktop\\log.txt";  //生成目录
    private static String findType = ".java";//需要寻找的文件
    private static Charset CharType =  Charset.forName("UTF-8");//读编码
    private static int threadNum = 10;//线程池最大线程数量
    private static HashSet<String> record = new HashSet<>();
    private static String SERVICE_MOUDLE ="service";
    private static String WEB_MOUDLE ="web";
    private static String BL_MOUDLE ="bl";
    //参数结束
    private static File sourceFile = new File(dicName);
    private static File targetFile = new File(succName);
    //线程池
    private static  ExecutorService cachedThreadPool = Executors.newScheduledThreadPool(threadNum);

    //仅扫描xx模块中的文件
    public static void scanner(File sourceFile) {
        if (sourceFile.isDirectory()) {
            File[] files = sourceFile.listFiles();
            for (File file : files) {
                if(file.getName().contains(BL_MOUDLE)){
                    getFiles(file);
                }
            }
        }
    }
    //执行文件
    public static void getFiles(File sourceFile) {
        if (sourceFile.isDirectory()) {
            File[] files = sourceFile.listFiles();
            for (File file : files) {
                getFiles(file);
            }
        } else {
            //如果是目标类型文件
            if (sourceFile.getName().contains(findType)) {
                ThreadCreadter threadCreadter = new ThreadCreadter();
                threadCreadter.setFile(sourceFile);
                threadCreadter.setCharType(CharType);
                threadCreadter.putRecord(record);
                cachedThreadPool.execute(threadCreadter);
            }
        }
    }

    public static void main(String[] args) {
        scanner(sourceFile);
        //在getfiles执行完，也就是所有任务已经分配完毕后 关掉 线程池
        cachedThreadPool.shutdown();
        while (true){
            //在所有任务执行完成后统计数量
            if(cachedThreadPool.isTerminated()){
                cachedThreadPool.shutdown();
                //关掉线程池后将错误信息写入文件中
                WriteTool.write(targetFile,record);
                System.out.println("发现文件" + CoutTool.getInstance().getCount());
                break;
            }
        }
    }
}

