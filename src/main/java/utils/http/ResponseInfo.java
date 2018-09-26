package utils.http;

import java.util.Map;

/**
 * Description:
 * Date: 2018/8/30
 * User: Eylaine
 * Name: ResponseInfo.java
 */
public class ResponseInfo {

    private String httpUrl;
    private String resBodyInfo;
    private Map<String, String> resHeaderInfo;
    private String cookies;
    private String errMsgInfo;

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getResBodyInfo() {
        return resBodyInfo;
    }

    public void setResBodyInfo(String resBodyInfo) {
        this.resBodyInfo = resBodyInfo;
    }

    public Map<String, String> getResHeaderInfo() {
        return resHeaderInfo;
    }

    public void setResHeaderInfo(Map<String, String> resHeaderInfo) {
        this.resHeaderInfo = resHeaderInfo;
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public String getErrMsgInfo() {
        return errMsgInfo;
    }

    public void setErrMsgInfo(String errMsgInfo) {
        this.errMsgInfo = errMsgInfo;
    }
}
