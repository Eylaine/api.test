package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * Description: Excel文件读工具类，只支持2007及以后版本
 * Date: 2018/10/23
 * @author : Eylaine
 */
public class ExcelUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(ExcelUtil.class);

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private String filename;

    /**
     * 默认第一个Sheet
     * @param filename
     */
    public ExcelUtil(String filename) {
        this.filename = filename;
        setWorkbook();
        setSheet(0);
    }

    /**
     * 构造方法
     * @param filename
     * @param index
     */
    public ExcelUtil(String filename, int index) {
        this.filename = filename;
        setWorkbook();
        setSheet(index);
    }

    /**
     * 构造方法
     * @param filename
     * @param sheetName
     */
    public ExcelUtil(String filename, String sheetName) {
        this.filename = filename;
        setWorkbook();
        setSheet(sheetName);
    }

    private void setWorkbook() {
        try {
            this.workbook = new XSSFWorkbook(new FileInputStream(this.filename));
        } catch (IOException e) {
            LOGGER.error("读取Excel文件异常：" + filename);
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * sheet序号
     * @param index
     */
    public void setSheet(int index) {
        this.sheet = this.workbook.getSheetAt(index);
    }

    /**
     * sheet名称
     * @param sheetName
     */
    public void setSheet(String sheetName) {
        this.sheet = this.workbook.getSheet(sheetName);
    }

    public XSSFSheet getSheet() {
        return this.sheet;
    }

    /**
     * 获取总行数
     * @return
     */
    public int getRowNum() {
        return this.sheet.getLastRowNum() + 1;
    }

    /**
     * 获取总列数
     * @return
     */
    public int getColNum() {
        int max = 0;

        if (this.sheet.getPhysicalNumberOfRows() < 1) {
            return 0;
        }

        for (int i = 0; i < getRowNum(); i++) {
            int temp = getRow(i).getPhysicalNumberOfCells();

            if (temp > max) {
                max = temp;
            }
        }
        return max;
    }

    /**
     * 获取第几行的数据
     * @param index
     * @return
     */
    private Row getRow(int index) {
        return this.sheet.getRow(index);
    }

    /**
     * 获取单元格的值
     * @param rowIndex
     * @param colIndex
     * @return
     */
    public String getCellValue(int rowIndex, int colIndex) {

        String result = "";
        Row row = getRow(rowIndex);
        Cell cell = row.getCell(colIndex);

        if (cell == null) {
            LOGGER.error("单元格为空：" + rowIndex + " " + colIndex);
            return result;
        }

        switch (cell.getCellTypeEnum()) {
            case BLANK:
                break;
            case STRING:
                result = cell.getStringCellValue();
                break;
            case BOOLEAN:
                boolean value = cell.getBooleanCellValue();
                result = Boolean.toString(value);
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)){
                    result = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cell.getDateCellValue());
                }else{
                    BigDecimal bd =  new BigDecimal(cell.getNumericCellValue());
                    String str = bd.toString();
                    if (str.contains(".0")) {
                        str = str.replace(".0", "");
                    }
                    result = str;
                }
                break;
            case FORMULA:
                result = cell.getCellFormula();
                break;
            default:
                LOGGER.error("未知类型");
                break;
        }

        return result;
    }

}
