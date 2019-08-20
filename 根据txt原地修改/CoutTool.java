package xiexie;

/**
 * @Description:
 * @Author: songyang
 * @Date: $date$
 */
public class CoutTool {
    private final static CoutTool instance = new CoutTool();
    private int count = 0;

    public static CoutTool getInstance(){
        return instance;
    }
    public void countNum(){
        instance.count ++;
    }
    public int getCount(){
        return count;
    }

}
