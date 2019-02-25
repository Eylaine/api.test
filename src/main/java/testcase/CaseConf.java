package testcase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.PropUtil;

/**
 * Description: 测试用例配置文件
 * Date: 2018/9/30
 * User: Eylaine
 */
public class CaseConf {

    private static Logger LOGGER = LoggerFactory.getLogger(CaseConf.class);

    public static String DOMAIN = getDomain();

    /**
     * 读取服务器的domain（域名）
     *
     * @return domain
     */
    private static String getDomain() {
        PropUtil propUtil = PropUtil.getPropUtil();
        LOGGER.info("读取配置文件中的：domain");
        return propUtil.getValue("domain");
    }
}
