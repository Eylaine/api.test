package utils.httputil;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: Http请求工具类
 * 单例模式，使用同一个HttpClient。目的：保持会话
 * Date: 2018/9/30
 * User: Eylaine
 */
public class HttpUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private CloseableHttpClient httpClient;
    private CloseableHttpResponse httpResponse;
    private BasicCookieStore cookieStore = new BasicCookieStore();

    public HttpUtil() {
        PoolingHttpClientConnectionManager pccm = new PoolingHttpClientConnectionManager();
        pccm.setMaxTotal(100);
        httpClient = HttpClients.custom().setConnectionManager(pccm).setDefaultCookieStore(cookieStore).build();
    }

    /**
     * 带header的get请求
     *
     * @param url
     * @param headers
     * @return
     */
    public ResInfo get(String url, Map<String, String> headers) {

        HttpGet httpGet = new HttpGet(url);

        if (null != headers && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpGet.addHeader(entry.getKey(), entry.getValue());
            }
        }

        return get(httpGet);
    }

    /**
     * 不带header的get请求
     *
     * @param url
     * @return
     */
    public ResInfo get(String url) {
        return get(url, null);
    }

    /**
     * 对内get请求，真正发送请求
     *
     * @param httpGet
     * @return
     */
    private ResInfo get(HttpGet httpGet) {
        ResInfo resInfo = new ResInfo();
        try {
            logger.info("开始发送get请求：" + httpGet.getURI());
            httpResponse = httpClient.execute(httpGet);
        } catch (Exception e) {
            logger.error("get请求执行失败：");
            logger.error(e.getMessage());
        }
        resInfo.setResCode(httpResponse.getStatusLine().getStatusCode());
        resInfo.setResBody(getBody(httpResponse));
        resInfo.setResHeader(getHeader(httpResponse));
        resInfo.setCookies(getCookies());
        return resInfo;
    }

    /**
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public ResInfo post(String url, Map<String, String> headers, Map<String, String> params) {

        HttpPost httpPost = new HttpPost(url);

        if (null != headers && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }
        }

        if (null != params && params.size() > 0) {
            List<BasicNameValuePair> temp = new ArrayList<>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                temp.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                try {
                    httpPost.setEntity(new UrlEncodedFormEntity(temp, "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    logger.error("请求参数设置异常：");
                    logger.error(e.getMessage());
                }
            }
        }

        return post(httpPost);
    }

    /**
     * @param url
     * @param params
     * @return
     */
    public ResInfo post(String url, Map<String, String> params) {
        return post(url, null, params);
    }

    /**
     * 对内post请求，真正发送请求
     *
     * @param httpPost
     * @return
     */
    private ResInfo post(HttpPost httpPost) {
        try {
            logger.info("发送Post请求：" + httpPost.getURI());
            httpResponse = httpClient.execute(httpPost);
        } catch (Exception e) {
            logger.error("post请求执行异常");
            logger.error(e.getMessage());
            httpResponse = null;
        }

        ResInfo resInfo = new ResInfo();
        resInfo.setResHeader(getHeader(httpResponse));
        resInfo.setResCode(httpResponse.getStatusLine().getStatusCode());
        resInfo.setResBody(getBody(httpResponse));
        resInfo.setCookies(getCookies());

        return resInfo;
    }

    /**
     * 从响应中获取body
     *
     * @param response
     * @return
     * @throws IOException
     */
    private String getBody(CloseableHttpResponse response) {
        String strResult = "";

        if (null == response) {
            logger.error("response为空！");
            return "";
        }

        HttpEntity httpEntity = response.getEntity();

        try {
            logger.info("获取响应的Body：");
            strResult = EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            logger.error("获取Body信息失败");
            logger.error(e.getMessage());
        }

        return strResult;
    }

    /**
     * 从响应中获取header
     * @param response
     * @return
     */
    private Map<String, String> getHeader(HttpResponse response) {
        Map<String, String> resHeader = new HashMap<>(16);

        if (null == response) {
            logger.error("response为空！");
            return resHeader;
        }

        Header[] headers = response.getAllHeaders();

        for (Header header : headers) {
            resHeader.put(header.getName(), header.getValue());
        }
        return resHeader;
    }

    /**
     * 获取返回结果中的cookies信息
     *
     * @return
     */
    private Map<String, String> getCookies() {
        Map<String, String> result = new HashMap<>();
        List<Cookie> cookies = cookieStore.getCookies();

        for (Cookie cookie : cookies) {
            result.put(cookie.getName(), cookie.getValue());
        }
        return result;
    }

}
