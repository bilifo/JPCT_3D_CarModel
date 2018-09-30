import com.setproject.bilifo.a3dtest.utils.ModelUtils;

public class TestCase {
    public void test1(){
        String file="123/asd/vvv.txt";
        String txt=ModelUtils.getFileSuffiyStr(file);

    }

    /**
     * 获得文件后缀
     * @param filePath
     * @return
     */
    public static String getFileSuffiyStr(String filePath){
        String tempStr=filePath.trim();
        int lastIndex=tempStr.lastIndexOf(".");
        String suffiy=null;
        try {
            suffiy = tempStr.substring(lastIndex, tempStr.length());
        }catch (Exception e){
            new Throwable("没有后缀");
        }
        tempStr=null;
        return suffiy;
    }
}
