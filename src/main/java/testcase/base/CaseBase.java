package testcase.base;

import com.google.gson.Gson;
import business.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import utils.FileUtil;
import utils.JsonUtil;
import utils.httputil.ResInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 测试用例基类
 * 通用账号Cookie保持，需要继承此类
 * Date: 2018/12/5
 *
 * @author: Eylaine
 */
public class CaseBase {

    private static Logger LOGGER = LoggerFactory.getLogger(CaseBase.class);

    /**
     * 该类子类所有测试用例执行前置步骤
     */
    @BeforeSuite
    public void login() {
        LOGGER.info("登陆");
    }

    /**
     * 该类子类所有测试用例执行后置步骤
     */
    @AfterSuite
    public void logout() {
        LOGGER.info("退出登陆");
    }

    public static void init() {
        FileUtil fu = new FileUtil();
        Gson gson = new Gson();

        List<String> temp = JsonUtil.jsonToList(fu.readAsResources("/account.json"));
        List<String> write = new ArrayList<>();

        for (String str : temp) {
            Account account = gson.fromJson(str, Account.class);
            String username = account.getUsername();
            String password = account.getPassword();
            ResInfo response = Login.loginByPassword(username, password);

            String sid = response.getCookies().get("SID");
            String userId = response.getCookies().get("USERID");

            account.setSid(sid);
            account.setUserId(userId);

            write.add(gson.toJson(account));
        }

        fu.writeToResources("/account.json", write.toString());
    }
}
