package utils;

import java.util.Map;

/**
 * Description:
 * Date: 2018/10/2
 * User: Eylaine
 */
public class ResInfo {
    private int resCode;
    //    private Map<String, String> resHeader;
    private String resBody;
    private Map<String, String> cookies;

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public int getResCode() {
        return resCode;
    }

    public void setResCode(int resCode) {
        this.resCode = resCode;
    }

//    public Map<String, String> getResHeader() {
//        return resHeader;
//    }

//    public void setResHeader(Map<String, String> resHeader) {
//        this.resHeader = resHeader;
//    }

    public String getResBody() {
        return resBody;
    }

    public void setResBody(String resBody) {
        this.resBody = resBody;
    }
}
