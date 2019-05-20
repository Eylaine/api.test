package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

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
    public static FileInputStream readFileToInputStream(String filename) throws FileNotFoundException {
        return new FileInputStream(filename);
    }

    private static InputStreamReader readToInputStreamReader(String filePath) throws FileNotFoundException {
        return new InputStreamReader(readFileToInputStream(filePath));
    }

    private static BufferedReader readToBufferedReader(String filePath) throws FileNotFoundException {
        return new BufferedReader(readToInputStreamReader(filePath));
    }

    private InputStream readToInoutStream(String filename) {
        return this.getClass().getResourceAsStream(filename);
    }

    /**
     * 读Resources目录下的文件
     * @param filename
     * @return
     */
    public String readAsResources(String filename) {
        BufferedReader br;

        StringBuilder sb = new StringBuilder();
        String line;

        try {
            br = new BufferedReader(new InputStreamReader(readToInoutStream(filename)));
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\r\n");
            }
        } catch (IOException e) {
            LOGGER.error("读取文件失败：" + filename);
            LOGGER.error(e.getMessage());
        }

        return sb.toString();
    }

    /**
     * 写Resources目录下的文件
     * @param filename
     * @param data
     */
    public void writeToResources(String filename, String data) {
        String filepath = this.getClass().getResource(filename).getPath();

        try {
            FileOutputStream fos = new FileOutputStream(new File(filepath));
            fos.write(data.getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            LOGGER.error("文件读写异常：" + filename);
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * 读文件
     * @param filePath filePath
     * @return String
     */
    public static String readToString(String filePath) {

        BufferedReader br;
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            br = readToBufferedReader(filePath);

            while ((line = br.readLine()) != null) {
                sb.append(line).append("\r\n");
            }
        } catch (IOException e) {
            LOGGER.error("读取文件失败：" + filePath);
            LOGGER.error(e.getMessage());
        }

        return sb.toString();
    }

    /**
     * 删除目录下的测试用例文件
     * @return
     */
    public static Boolean deleteTestCase() {
        return false;
    }


}
