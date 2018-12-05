package utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Description:
 * User: Eylaine
 * Date: 18-10-22
 */
public class Jsonutil {

    private static Logger LOGGER = LoggerFactory.getLogger(Jsonutil.class);

    /**
     *
     * @param str
     * @return
     */
    public static Map<String, Object> jsonToMap(String str) {
        JSONObject json;;
        try {
            json = JSONObject.fromObject(str);
        } catch (JSONException e) {
            return null;
        }
        Map<String, Object> resultMap = new TreeMap<>();

        for (Iterator it = json.keys(); it.hasNext();) {
            String key = it.next().toString();
            Object value = json.get(key);

            if (value instanceof JSONObject) {
                resultMap = jsonToMap(value.toString());
            } else if (value instanceof JSONArray) {
                JSONArray ja = (JSONArray) value;
                for (int i = 0; i < ja.size(); i++) {
                    Object jo2 = ja.get(i);
                    resultMap = jsonToMap(jo2.toString());
                    //TODO
                }
            } else { //简单类型
                if (resultMap.containsKey(key)) {
                    ArrayList valueList = new ArrayList();

                    if (resultMap.get(key) instanceof ArrayList) {
                        valueList = (ArrayList) resultMap.get(key);
                        valueList.add(value.toString().trim());
                        resultMap.put(key, valueList);
                    } else {
                        valueList.add(value.toString().trim());
                        valueList.add(resultMap.get(key));
                        resultMap.put(key, valueList);
                    }
                } else {
                    resultMap.put(key, value.toString().trim());
                }
            }
        }
        return resultMap;
    }
}
