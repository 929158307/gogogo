package saomiao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashSet;

/**
 * @Description:
 * @Author: songyang
 * @Date: $date$
 */
public class WriteTool {

    public synchronized static void write(File file, HashSet<String> record) {
        String name = "";
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            for (String str : record) {
                name += str +"\r\n";
            }
            bw.write(name);
            bw.close();

        }catch (Exception ex){
            System.out.println("写入异常");
        }
    }

}
