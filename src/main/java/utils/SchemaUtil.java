package utils;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description:
 * Date: 2019-04-04
 * @author: Eylaine
 */
public class SchemaUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(SchemaUtil.class);

    /**
     * Json校验
     * @param str Json字符串
     * @param filePath Json Schema文件路径
     * @return bool
     */
    public static boolean validate(String str, String filePath) {

        JSONObject data = new JSONObject(new JSONTokener(str));
        JSONObject temp = null;

        temp = new JSONObject(new JSONTokener(FileUtil.readFileToInputStream(filePath)));

        Schema schema = SchemaLoader.load(temp);

        try {
            schema.validate(data);
        } catch (ValidationException e) {
            e.getCausingExceptions().stream().map(ValidationException::getMessage)
                    .forEach(LOGGER::error);
            return false;
        }

        return true;

    }
}
