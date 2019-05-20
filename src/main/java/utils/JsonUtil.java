package utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.*;
import keyword.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Description: 解析Json字符串，返回Map
 * Date: 2018/10/22
 * @author : Eylaine
 */
public class JsonUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    private static Gson gson = new Gson();
    private static JsonParser jsonParser = new JsonParser();

    /**
     * 读取Json文件为String
     * @param filename 文件名
     * @return 字符串
     */
    public static String readToString(String filename) {

        try {
            FileInputStream fis = new FileInputStream(filename);
            BufferedReader buffer = new BufferedReader();
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage());
            LOGGER.error("文件读取失败，请检查文件路径：" + filename);
        }
    }

    /**
     * 从Json串中读取value，目前只支持三层json嵌套
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

    public static List<String> jsonToList(String str) {
        List<String> result = new ArrayList<>();

        JsonArray jsonArray = (JsonArray)jsonParser.parse(str);

        for (JsonElement e : jsonArray) {
            result.add(e.toString());
        }

        return result;
    }

    /**
     * 根绝账号类型，获取sid
     * @param index: 0 是通用账号, 1 是vip账号
     * @return
     */
    public static String getSid(int index) {
        FileUtil fu = new FileUtil();
        Gson gson = new Gson();

        List<String> data = JsonUtil.jsonToList(fu.readAsResources("/account.json"));
        String temp = data.get(index);
        Account account = gson.fromJson(temp, Account.class);
        return account.getSid();
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
     * large Json串是否包含small Json串
     * 并比较包含的key所对应的value
     * @param large
     * @param small
     * @return
     */
    //unchecked
    public static boolean contains(String large, String small) {

        if (null == large) {
            return null == small;
        }

        JsonElement eLarge = jsonParser.parse(large);
        JsonElement eSmall = jsonParser.parse(small);

        return contains(eLarge, eSmall);
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
     *
     * @param aa
     * @param ab
     * @return
     */
    private static boolean contains(JsonArray aa, JsonArray ab) {

        if (aa.size() < ab.size()) {
            return false;
        }

//        List<JsonElement> la = sortList(aa);
//        List<JsonElement> lb = sortList(ab);

        for (JsonElement je: ab) {

            if (!aa.toString().contains(je.toString())) {
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
     * 判断joa是否包含job
     * 1、判断job的keySet是否在joa中
     * 2、比较相同key的值是否相等
     * @param joa
     * @param job
     * @return
     */
    private static boolean contains(JsonObject joa, JsonObject job) {
        Set<String> bSet = job.keySet();
        Set<String> aSet = joa.keySet();

        if (!aSet.containsAll(bSet)) {
            return false;
        } else if (aSet.containsAll(bSet)) {
            for (String bKey: bSet) {
                if (!compare(joa.get(bKey), job.get(bKey))) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * 比较Json对象，部分字段不比较
     * Json串集合大的在前
     * @param joa
     * @param job destination
     * @param exclude 排除字段的List
     * @return
     */
    public static boolean compare(JsonObject joa, JsonObject job, List<String> exclude) {

        Set<String> aSet = joa.keySet();
//        Set<String> bSet = job.keySet();
//
//        if (!aSet.equals(bSet)) {
//            return false;
//        }

        for (String aKey: aSet) {

            if (!exclude.contains(aKey)) {
                if (!compare(joa.get(aKey), job.get(aKey))) {
                    return false;
                }
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

    /**
     *
     * @param eLarge
     * @param eSmall
     * @return
     */
    private static boolean contains(JsonElement eLarge, JsonElement eSmall) {

        if (eLarge.isJsonObject() && eSmall.isJsonObject()) {
            return contains((JsonObject)eLarge, (JsonObject)eSmall);
        } else if (eLarge.isJsonArray() && eSmall.isJsonArray()) {
            return contains((JsonArray)eLarge, (JsonArray)eSmall);
        } else {
            LOGGER.error(eLarge.getClass() + " 类型不能比较 " + eSmall.getClass());
            return false;
        }

    }

}