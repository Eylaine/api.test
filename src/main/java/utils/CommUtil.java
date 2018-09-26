package utils;

/**
 * Description:
 * Date: 2018/8/30
 * User: Eylaine
 * Name: CommUtil.java
 */
public class CommUtil {

    public static String getRootPath() {
        String temp = System.getProperty("user.dir");
        temp = temp.replace("\\", "/");
        return temp + "/";
    }

}
