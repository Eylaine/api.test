package utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Description: 解析Json字符串，返回Map
 * Date: 2018/10/22
 *
 * @author : Eylaine
 */
public class JsonUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    /**
     * 从Json串中读取value，目前只支持三层json嵌套
     *
     * @param str 源字符串
     * @param key 格式eg：a.b.c
     * @return 返回字符串
     */
    public static String getValue(String str, String key) {
        JSONObject json = JSONObject.fromObject(str);
        String[] sk = key.split("\\.");
        int num = sk.length;
        String result = "";

        switch (num) {
            case 1:
                result = json.getString(sk[0]);
                break;
            case 2:
                JSONObject jone = json.getJSONObject(sk[0]);
                result = jone.getString(sk[1]);
                break;
            case 3:
                JSONObject jone1 = json.getJSONObject(sk[0]);
                JSONObject jtwo = jone1.getJSONObject(sk[1]);
                result = jtwo.getString(sk[2]);
                break;
            default:
                LOGGER.error("嵌套太深，暂不支持：" + key);
                break;
        }

        return result;
    }

    /**
     * 从Json串中读取List，目前只支持三层json嵌套
     *
     * @param str 源字符串
     * @param key 格式eg：a.b，c
     * @return
     */
    public static ArrayList<String> getArrayList(String str, String key) {
        JSONObject json = JSONObject.fromObject(str);
        String[] sk = key.split("\\.");
        int num = sk.length;

        ArrayList<String> al = new ArrayList<>();

        switch (num) {
            case 1:
                JSONArray jone = json.getJSONArray(sk[0]);
                for (int i = 0; i < jone.size(); i++) {
                    al.add(jone.getString(i));
                }
                break;
            case 2:
                JSONObject jone1 = json.getJSONObject(sk[0]);
                JSONArray jtwo = jone1.getJSONArray(sk[1]);
                for (int i = 0; i < jtwo.size(); i++) {
                    al.add(jtwo.getString(i));
                }
                break;
            default:
                LOGGER.error("嵌套太深，暂不支持：" + key);
                break;
        }

        return al;
    }

}