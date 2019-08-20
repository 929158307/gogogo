import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Description:
 * @Author: songyang
 * @Date: $date$
 */
public class WriteTool {

    public static void write(File file,String body){
        try{
            FileOutputStream fout = new FileOutputStream(file);
            FileChannel fc = fout.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate( 1024 );
            byte[] bytes = body.getBytes();
            for (int i = 0; i < bytes.length; i++) {
                if (buffer.remaining() == 0) {//当buffer剩余为0是 写入信息 然后清空，继续往缓存里面放
                    buffer.flip();
                    fc.write(buffer);//管道写入数据
                    buffer.clear();
                } else {
                    buffer.put(bytes[i]);
                }
            }
            buffer.flip();
            fc.write(buffer);
            fout.close();//关闭

        }catch (Exception ex){
            System.out.println(ex);
        }
    }

}
