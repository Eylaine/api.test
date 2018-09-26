package utils.http;

import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpResponse;

import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import utils.LogUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Date: 2018/8/30
 * User: Eylaine
 * Name: HttpUtil.java
 */
public class HttpUtil {

    private LogUtil logger = LogUtil.getLogger(this.getClass());

    private HttpClient httpclient;
    private HttpGet httpGet;
    private HttpPost httpPost;
    private HttpResponse httpResponse;

    /**
     * 默认构造方法
     */
    public HttpUtil() {
        PoolingClientConnectionManager pccm = new PoolingClientConnectionManager();
        pccm.setMaxTotal(100);
        httpclient = new DefaultHttpClient(pccm);
        httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        httpclient.getConnectionManager().closeIdleConnections(30, TimeUnit.SECONDS);
    }

    /**
     *对外接口，带header的get请求
     * @param url
     * @param headers
     * @return
     */
    public ResponseInfo get(String url, Map<String, String> headers) {
        ResponseInfo responseInfo = new ResponseInfo();
        if (null == url || url.length() < 1) {
            logger.error("请求url为空");
            responseInfo.setResBodyInfo("请求url为空");
            return responseInfo;
        }

        httpGet = new HttpGet(url);
        httpGet = setGetHeader(httpGet, headers);
        return get(httpGet);
    }

    /**
     * 对外接口，不带header的get请求
     * @param url
     * @return
     */
    public ResponseInfo get(String url) {
        return get(url, null);
    }

    /**
     * 对内方法，执行get请求
     * @param httpGet
     * @return
     */
    private ResponseInfo get(HttpGet httpGet) {
        ResponseInfo responseInfo = new ResponseInfo();
        try {
            httpResponse = httpclient.execute(httpGet);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        responseInfo.setResBodyInfo(getResBody(httpResponse));
        responseInfo.setCookies(getResCookies());
        responseInfo.setResHeaderInfo(getResHeaders(httpResponse));
        return responseInfo;
    }

    /**
     * 设置请求头，会覆盖默认的header
     * @param httpGet
     * @param headers
     * @return
     */
    private HttpGet setGetHeader(HttpGet httpGet, Map<String, String> headers) {
        Map<String, String> temp = new HashMap<String, String>();
    }

    /**
     * 将返回的消息体转换成string
     * @param httpResponse HttpResponse
     * @return
     */
    private String getResBody(HttpResponse httpResponse) {

    }

    /**
     * 将返回的cookies转换成string
     * @return string
     */
    private String getResCookies() {

    }

    /**
     * 将返回的消息头转换成Map
     * @param httpResponse HttpResponse
     * @return
     */
    private Map<String, String> getResHeaders(HttpResponse httpResponse) {

    }
}
