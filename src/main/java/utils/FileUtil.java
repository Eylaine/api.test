package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Description:
 * Date: 2018/10/3
 * User: Eylaine
 */
public class FileUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 字节流读取文件
     *
     * @param filename 文件名/路径
     * @return 返回字节流
     */
    public static FileInputStream readFileToInputStream(String filename) {
        try {
            return new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            LOGGER.error("读取文件失败，请检查文件路径：" + filename);
            LOGGER.error(e.getMessage());
            return null;
        }
    }
}
