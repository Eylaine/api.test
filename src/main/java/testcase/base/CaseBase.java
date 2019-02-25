package testcase.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

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
}
