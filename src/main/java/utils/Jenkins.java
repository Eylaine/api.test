package utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang.StringUtils;
import utils.httputil.HttpUtil;
import utils.httputil.ResInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description:
 * Date: 2019-06-03
 * @author: Eylaine
 */
public class Jenkins {

    private int lastBuildNum = 0;
    private HttpUtil httpUtil = new HttpUtil();
    private JsonParser jsonParser = new JsonParser();
    private Map<String, Object> caseResult = new HashMap<>();
    private Map<String, String> header = new HashMap<>();

    public Jenkins() {
        header.put("Token", "");
        setLastestReport();
        setTotalCaseNum();
    }

    private void setLastestReport() {
        //ip:port/job/${project_name}/api/json?pretty=true
        String urlString = "";

        ResInfo resInfo = httpUtil.get(urlString, header);
        String responseString = resInfo.getResBody();

        jsonParser.parse(responseString);
        String lastBuildReport = JsonPath.read(responseString,"$.lastBuild.url") + "allure/";
        lastBuildNum = JsonPath.read(responseString,"$.lastBuild.number");

        caseResult.put("reportUrl", lastBuildReport);
        caseResult.put("lastBuildNum", lastBuildNum);
    }

    private void setTotalCaseNum() {
        //ip:port/job/${project_name}/${build_num}/consoleText
        String urlString = "";

        ResInfo resInfo = httpUtil.get(urlString, header);
        String responseString = resInfo.getResBody();

        Pattern p = Pattern.compile("Tests run: (.*), Failures: (.*), Errors: (.*), Skipped:");

        //让正则对象和要作用的字符串相关联。获取匹配器对象。
        Matcher m  = p.matcher(responseString);

        if (m.find()) {
            caseResult.put("totals", Integer.parseInt(m.group(1)));
            caseResult.put("failures", Integer.parseInt(m.group(2)));
            caseResult.put("errors", Integer.parseInt(m.group(3)));
        } else {
            caseResult.put("totals", 0);
            caseResult.put("failures", 0);
            caseResult.put("errors", 0);
        }

        Pattern q = Pattern.compile("Total time: (.*)s");
        Matcher n = q.matcher(responseString);

        if (n.find()) {
            caseResult.put("duration", Float.parseFloat(n.group(1)));
        } else {
            caseResult.put("duration", 0);
        }

    }

    public Map<String,Object> getCaseInfo() {

        java.text.SimpleDateFormat d = new java.text.SimpleDateFormat();
        d.applyPattern("YYYY-MM-dd HH:mm:ss");
        java.util.Date nowdate = new java.util.Date();
        String str_date = d.format(nowdate);
        caseResult.put("startTime", str_date);

        return caseResult;
    }

    public List<Map<String, Object>> getSuites() {
        String filepath = CommonUtil.ROOTPATH + "allure-report/data/suites.json";
//        String filepath = CommonUtil.ROOTPATH + "output/report/data/suites.json";
        JsonObject je = (JsonObject)jsonParser.parse(FileUtil.readToString(filepath));
        //root
        JsonArray first = je.getAsJsonArray("children");
        //suites - C端接口
        JsonObject second = (JsonObject)first.get(0);
        //tests
        JsonArray third = second.getAsJsonArray("children");
        //test数量
        int num = third.size();

        List<Map<String, Object>> result = new ArrayList<>(num);
        DecimalFormat df = new DecimalFormat("0.00");

        //遍历tests，即遍历每个appid
        for (JsonElement jo : third) {
            Map<String, Object> temp = new HashMap<>();
            //appid用例数
            int total = 0;
            //成功数
            int pass = 0;
            JsonObject jObject = (JsonObject)jo;
            String name = jObject.get("name").toString();
            temp.put("app_id", StringUtils.strip(name, "\""));
            //classes数组
            JsonArray fourth = jObject.getAsJsonArray("children");

            for (JsonElement joo : fourth) {
                JsonObject joObject = (JsonObject)joo;
                //用例组
                JsonArray ja = joObject.getAsJsonArray("children");
                total += ja.size();
                for (JsonElement jee : ja) {
                    JsonObject jj = (JsonObject)jee;
                    String status = jj.get("status").toString();
                    if (status.equals("\"passed\"")) {
                        pass += 1;
                    }
                }

                temp.put("total_cases", total);
                temp.put("pass_cases", pass);
                String rate = df.format((float)100 * pass/total);
                temp.put("pass_rate", Float.valueOf(rate));
            }

            result.add(temp);
        }

        return result;
    }
}
