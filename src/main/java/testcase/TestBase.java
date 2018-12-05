package testcase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

/**
 * Description:
 * Date: 2018/12/5
 *
 * @author: Eylaine
 */
public class TestBase {

    private static Logger LOGGER = LoggerFactory.getLogger(TestBase.class);

    @BeforeSuite
    public void login() {
        LOGGER.info("登陆");
    }

    @AfterSuite
    public void logout() {
        LOGGER.info("推出登陆");
    }
}
