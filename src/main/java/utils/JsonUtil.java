package utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Description: 解析Json字符串，返回Map
 * Date: 2018/10/22
 *
 * @author : Eylaine
 */
public class JsonUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    private static Gson gson = new Gson();
    private static JsonParser jsonParser = new JsonParser();

    /**
     * 从Json串中读取value，目前只支持三层json嵌套
     *
     * @param str 源字符串
     * @param key 格式eg：a.b.c
     * @return 返回字符串
     */
    public static String getValue(String str, String key) {
        JSONObject json = JSONObject.parseObject(str);
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
        JSONObject json = JSONObject.parseObject(str);
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

    /**
     * 比较两个bean是否等价
     * @param oa
     * @param ob
     * @return
     */
    public static boolean compare(Object oa, Object ob) {

        if (null == oa) {
            return ob == null;
        }

        return compare(gson.toJson(oa), gson.toJson(ob));
    }

    /**
     * 比较json字符串
     * @param sa
     * @param sb
     * @return
     */
    public static boolean compare(String sa, String sb) {

        if (null == sa) {
            return null == sb;
        }

        if (sa.equals(sb)) {
            return true;
        }

        JsonElement ja = jsonParser.parse(sa);
        JsonElement jb = jsonParser.parse(sb);

        if (gson.toJson(ja).equals(gson.toJson(jb))) {
            return true;
        }

        return compare(ja, jb);
    }

    /**
     * 比较json数组
     * @param aa
     * @param ab
     * @return
     */
    private static boolean compare(JsonArray aa, JsonArray ab) {

        if (aa.size() != ab.size()) {
            return false;
        }

        List<JsonElement> la = sortList(aa);
        List<JsonElement> lb = sortList(ab);

        for (int i = 0; i < la.size(); i++) {
            if (!compare(la.get(i), lb.get(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * json数组排序
     * @param ja
     * @return
     */
    private static List<JsonElement> sortList(JsonArray ja) {
        List<JsonElement> jlist = new ArrayList<>();
        ja.forEach(jlist::add);
        jlist.sort(Comparator.comparing(gson::toJson));
        return jlist;
    }

    /**
     * 比较Json对象
     * @param joa
     * @param job
     * @return
     */
    private static boolean compare(JsonObject joa, JsonObject job) {
        Set<String> aSet = joa.keySet();
        Set<String> bSet = job.keySet();

        if (!aSet.equals(bSet)) {
            return false;
        }

        for (String aKey: aSet) {
            if (!compare(joa.get(aKey), job.get(aKey))) {
                return false;
            }
        }

        return true;
    }

    /**
     *
     * @param pa
     * @param pb
     * @return
     */
    private static boolean compare(JsonPrimitive pa, JsonPrimitive pb) {
        return pa.equals(pb);
    }

    /**
     *
     * @param ea
     * @param eb
     * @return
     */
    private static boolean compare(JsonElement ea, JsonElement eb) {

        if (ea.isJsonObject() && eb.isJsonObject()) {
            return compare((JsonObject) ea, (JsonObject)eb);
        } else if (ea.isJsonArray() && eb.isJsonArray()) {
            return compare((JsonArray)ea, (JsonArray)eb);
        } else if (ea.isJsonPrimitive() && eb.isJsonPrimitive()) {
            return compare((JsonPrimitive)ea, (JsonPrimitive)eb);
        } else return ea.isJsonNull() && eb.isJsonNull();
    }

}