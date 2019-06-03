package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.httputil.ResInfo;

import java.util.Map;

/**
 * Description: 通用工具类
 * Date: 2018/11/19
 *
 * @author: Eylaine
 */
public class CommonUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(CommonUtil.class);
    public static String ROOTPATH = getRootPath();

    private static String getRootPath() {
        String temp = System.getProperty("user.dir");
        temp = temp.replace("\\", "/");
        return temp + "/";
    }

    /**
     * 检查http状态码
     *
     * @param statusCode int
     * @return True or False
     */
    public static Boolean checkStatusCode(ResInfo resInfo, int statusCode) {

        if (resInfo.getResCode() == statusCode) {
            return true;
        } else {
            LOGGER.error("http状态码不正确：" + resInfo.getResCode());
            LOGGER.error(resInfo.getResBody());
            return false;
        }
    }

    /**
     * 格式化Get请求的URL
     * @param params Map键值对
     * @return 返回"？key=value&key=value"
     */
    public static String parseUrl(Map<String, String> params) {
        StringBuilder sb = new StringBuilder("?");

        if (params.size() < 1) {
            LOGGER.error("入参不正确， 长度为0" + params);
            return "";
        }

        for (String each: params.keySet()) {
            sb.append(each).append("=").append(params.get(each)).append("&");
        }

        //字符串截取，去除最后一个"&"
        return sb.substring(0, sb.length() - 1);
    }

}
