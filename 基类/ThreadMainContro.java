import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description:
 * @Author: songyang
 * @Date: $date$
 */
public class ThreadMainContro {
    //参数
    private static String dicName = "C:\\Users\\wxygy\\Desktop\\source"; //目标文件
    private static String succName = "C:\\Users\\wxygy\\Desktop\\target";  //生成目录
    private static String findType = ".java";//需要寻找的文件
    private static String fileType = ".java";//生成的文件
    private static Charset CharType =  Charset.forName("UTF-8");//读编码
    private static int threadNum = 10;//线程池最大线程数量
    //参数结束
    private static File sourceFile = new File(dicName);
    private static File targetFile = new File(succName);
    //线程池
    private static  ExecutorService cachedThreadPool = Executors.newScheduledThreadPool(threadNum);

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
                threadCreadter.setTargetFile(targetFile);
                threadCreadter.setFileType(fileType);
                threadCreadter.setCharType(CharType);
                cachedThreadPool.execute(threadCreadter);
            }
        }
    }

    public static void main(String[] args) {
        getFiles(sourceFile);
        //在getfiles执行完，也就是所有任务已经分配完毕后 关掉 线程池
        cachedThreadPool.shutdown();
        while (true){
            //在所有任务执行完成后统计数量
            if(cachedThreadPool.isTerminated()){
                cachedThreadPool.shutdown();
                System.out.println("修改文件共" + CoutTool.getInstance().getCount());
                break;
            }
        }
    }
}

