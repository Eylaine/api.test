package utils.http;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import utils.LogUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Description:
 * Date: 2018/8/30
 * User: Eylaine
 * Name: HttpUtil.java
 */
public class HttpUtil {

    private LogUtil logger = LogUtil.getLogger(this.getClass());

    private CloseableHttpClient httpclient =  HttpClients.createDefault();
    private HttpGet httpGet;
    private HttpPost httpPost;
    private HttpResponse httpResponse;

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
     * 默认的headers
     * @return
     */
    public Map<String, String> getCommonHeader() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        headers.put("User-Agent", "QihooTest-Leo/1.0.0 Apache-HttpClient/4.2.5 (java 1.7)");
        headers.put("Accept", "*/*");
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        return headers;
    }

    /**
     * 设置请求头，会覆盖默认的header
     * @param httpGet
     * @param headers
     * @return
     */
    private HttpGet setGetHeader(HttpGet httpGet, Map<String, String> headers) {
        Map<String, String> temp = getCommonHeader();
        if (headers != null && headers.size()>0) {
            Iterator<Map.Entry<String, String>> iteTmp = headers.entrySet().iterator();
            while(iteTmp.hasNext()){
                Map.Entry<?, ?> entryTmp = iteTmp.next();
                temp.put(entryTmp.getKey().toString(), entryTmp.getValue().toString());
            }
        }
        for (Map.Entry<String, String> entry : temp.entrySet()) {
            httpGet.addHeader(entry.getKey(), entry.getValue());
        }
        return httpGet;
    }

    /**
     * 将返回的消息体转换成string
     * @param httpResponse HttpResponse
     * @return
     */
    private String getResBody(HttpResponse httpResponse) {
        String strResult = "";
        if (httpResponse == null ) return strResult;
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            try {
                strResult = strResult + EntityUtils.toString(httpResponse.getEntity());
            } catch (ParseException e) {
                logger.error("获取body信息异常450：");
                logger.error(e.getMessage());
                strResult = e.getMessage();
            } catch (IOException e) {
                logger.error("获取body信息异常454：");
                logger.error(e.getMessage());
                strResult = e.getMessage();
            }
        } else if (httpResponse.getStatusLine().getStatusCode() == 302) {
            String url = httpResponse.getLastHeader("Location").getValue();
            httpResponse.setStatusCode(200);
            strResult = url;
            return strResult;
        } else {
            strResult = "Error Response:" + httpResponse.getStatusLine().toString();
            if (httpResponse.getEntity() != null) {
                try {
                    httpResponse.getEntity().consumeContent();
                } catch (IOException e) {
                    logger.error("获取body信息异常469：");
                    logger.error(e.getMessage());
                }
            }
        }
        return strResult;
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
