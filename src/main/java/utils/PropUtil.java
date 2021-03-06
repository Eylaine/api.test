package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * Description: resource properties配置文件读写
 * Date: 2018/10/3
 * User: Eylaine
 */
public class PropUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(PropUtil.class);

    private Properties properties;

    private static volatile PropUtil propUtil = null;

    /**
     * 构造方法，初始化Properties对象
     * 读取默认配置文件配置：config.properties
     */
    public PropUtil() {
        properties = new Properties();

        try {
            properties.load(this.getClass().getResourceAsStream("/config.properties"));
        } catch (IOException e) {
            properties = null;
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * 构造方法，初始化Properties对象
     * 读取指定文件配置
     * @param filename string
     */
    public PropUtil(String filename) {
        properties = new Properties();

        try {
            properties.load(FileUtil.readFileToInputStream(filename));
        } catch (IOException e) {
            properties = null;
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * 获取配置文件内容
     * @param key key
     * @return String
     */
    public String getValue(String key) {
        String value = properties.getProperty(key);

        if (value == null || "".equals(value)) {
            LOGGER.error("未获取到对应的value，请检查key：" + key);
            return "";
        }

        return value;
    }
}
