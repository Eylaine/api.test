package utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Description: Http请求工具类
 * Date: 2018/9/30
 * User: Eylaine
 */
public class HttpUtil {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private HttpClient httpClient;
    private HttpGet httpGet;
    private HttpPost httpPost;
    private HttpResponse httpResponse;

    public HttpUtil() {
        httpClient = new DefaultHttpClient();
    }

    public ResInfo get(String url, Map<String, String> headers) {
        ResInfo resInfo = new ResInfo();
        httpGet = new HttpGet(url);

        if (null != headers && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.addHeader(entry.getKey(), entry.getValue());
            }
        }

        return get(httpGet);
    }

    /**
     * 不带header，只有url的get请求
     * @param url
     * @return
     */
    public ResInfo get(String url) {
        return get(url, null);
    }

    private ResInfo get(HttpGet httpGet) {
        ResInfo resInfo = new ResInfo();
        try {
            httpResponse = httpClient.execute(httpGet);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        resInfo.setResCode(httpResponse.getStatusLine().getStatusCode());
        resInfo.setResBody(getBody(httpResponse));
        resInfo.setResHeader(getHeader(httpResponse));
        return resInfo;
    }

    private String getBody(HttpResponse response) {
        String strResult = "";

        if (null == response) return strResult;
        return null;
    }

    private Map<String, String> getHeader(HttpResponse response) {
        return null;
    }
}
