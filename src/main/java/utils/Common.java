package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.httputil.ResInfo;

/**
 * Description:
 * Date: 2018/11/19
 *
 * @author: Eylaine
 */
public class Common {

    private static Logger LOGGER = LoggerFactory.getLogger(Common.class);

    /**
     * 检查http状态码
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

}
