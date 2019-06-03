package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

/**
 * Description: 单例模式
 * Date: 2019-02-26
 * @author: Eylaine
 */
public class YamlUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(YamlUtil.class);

    private Yaml yaml;

    private static volatile YamlUtil yamlUtil = null;

    private YamlUtil() {
        yaml = new Yaml();
    }

    /**
     * 获取
     * @return
     */
    public static YamlUtil getYamlUtil() {
        if (yamlUtil == null) {
            synchronized (YamlUtil.class) {
                if (yamlUtil == null) {
                    yamlUtil = new YamlUtil();
                }
            }
        }

        return yamlUtil;
    }

    /**
     * 读取默认Yaml文件配置
     * @return Map<String, Object>
     */
    public Map<String, Object> parse() {
        return yaml.load(YamlUtil.class.getResourceAsStream("/config.yaml"));
    }
}
