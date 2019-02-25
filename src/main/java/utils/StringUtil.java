package utils;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Date: 2019-02-25
 *
 * @author: Eylaine
 */
public class StringUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(StringUtil.class);

    public static Boolean isEmptyOrNull(String str) {

        if (null == str || str.equals("")) {
            LOGGER.error("str为空" + str);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 字符串转换成Map
     * @param str json串
     * @return
     */
    public static Map<String, String> stringToMap(String str) {
        Map<String, String> result = new HashMap<>();

        if (!isEmptyOrNull(str)) {
            JSONObject jsonObject = JSONObject.parseObject(str);

            for (String each: jsonObject.keySet()) {
                result.put(each, jsonObject.getString(each));
            }
        }

        return result;
    }
}
