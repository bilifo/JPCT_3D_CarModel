import org.junit.Test;

public class TestCaseTest {

    @Test
    public void test1() {
        String file="123/asd/vvv.txt";
        String txt= getFileSuffiyStr(file);
        System.out.print(txt);
    }

    public String getFileSuffiyStr(String filePath){
        String tempStr=filePath.trim();
        int lastIndex=tempStr.lastIndexOf("/");
        String suffiy=null;
        try {
            suffiy = tempStr.substring(lastIndex+1, tempStr.length());
        }catch (Exception e){
            new Throwable("没有后缀");
        }
        tempStr=null;
        return suffiy;
    }
}