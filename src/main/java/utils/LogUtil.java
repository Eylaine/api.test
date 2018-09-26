package utils;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 * Date: 2018/8/30
 * User: Eylaine
 * Name: LogUtil.java
 */
public class LogUtil {

    private Logger logger;

    private LogUtil(String name) {
        configLogProperties();
        logger = LoggerFactory.getLogger(name);
    }

    private LogUtil(Class<?> clazz) {
        configLogProperties();
        logger = LoggerFactory.getLogger(clazz);
    }

    public static LogUtil getLogger(String name) {
        return new LogUtil(name);
    }

    public static LogUtil getLogger(Class<?> clazz) {
        return new LogUtil(clazz);
    }

    public void info(String message) {
        logger.info(message);
    }

    public void error(String message) {
        logger.error(message);
    }

    private void configLogProperties() {
        PropertyConfigurator.configure(CommUtil.getRootPath() + "src/main/resources/log4j.properties");
    }
}
