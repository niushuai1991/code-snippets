/**
  * -------------------------------------------------------------------------
  * (C) Copyright Gyyx Tec Corp. 1996-2017 - All Rights Reserved
  * @版权所有：北京光宇在线科技有限责任公司
  * @项目名称：check-role
  * @作者：niushuai
  * @联系方式：niushuai@gyyx.cn
  * @创建时间：2017年3月30日 下午5:47:23
  * @版本号：0.0.1
  *-------------------------------------------------------------------------
  */
package cn.gyyx.action.oa.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import com.google.common.base.Throwables;

import cn.gyyx.action.oa.beans.questionnaire.Constants;
import cn.gyyx.log.sdk.GYYXLogger;
import cn.gyyx.log.sdk.GYYXLoggerFactory;

/**
 * <p>
 * Excel表格生成类
 * </p>
 * 
 * @author niushuai
 * @since 0.0.1
 */
public class ExportExcel {
    
    private static final GYYXLogger LOG = GYYXLoggerFactory
            .getLogger(ExportExcel.class);
    // 显示的导出表的标题
    private String title;
    // 导出表的列名
    private String[] rowName;

    private List<Object[]> dataList = new ArrayList<>();

    /**
     * 构造方法，传入要导出的数据
     * 
     * @param title
     * @param rowName
     * @param dataList
     */
    public ExportExcel(String title, String[] rowName,
            List<Object[]> dataList) {
        initial(title, rowName, dataList);
    }

    private void initial(String title, String[] rowName,
            List<Object[]> dataList) {
        this.dataList = dataList;
        this.rowName = rowName;
        this.title = title;
    }

    /**
     * <p>
     * 导出数据
     * </p>
     *
     * @action niushuai 2017年3月30日 下午5:58:32 描述
     *
     * @return
     * @throws Exception
     *             byte[]
     */
    public byte[] export() {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(title); // 创建工作表

        // 产生表格标题行
        HSSFRow rowm = sheet.createRow(0);
        HSSFCell cellTiltle = rowm.createCell(0);

        // sheet样式定义【getColumnTopStyle()/getStyle()均为自定义方法 - 在下面 - 可扩展】
        HSSFCellStyle columnTopStyle = this.getColumnTopStyle(workbook);// 获取列头样式对象
        HSSFCellStyle style = this.getStyle(workbook); // 单元格样式对象
        final int TITLE_HEIGHT = 1; // 标题高度，会影响到数据表的起始行位置
        sheet.addMergedRegion(
            new CellRangeAddress(0, TITLE_HEIGHT - 1, 0, (rowName.length - 1)));
        cellTiltle.setCellStyle(columnTopStyle);
        cellTiltle.setCellValue(title);

        // 定义所需列数
        int columnNum = rowName.length;
        HSSFRow rowRowName = sheet.createRow(TITLE_HEIGHT); // 在索引1的位置创建行(最顶端的行开始的第二行)

        // 将列头设置到sheet的单元格中
        for (int n = 0; n < columnNum; n++) {
            HSSFCell cellRowName = rowRowName.createCell(n); // 创建列头对应个数的单元格
            cellRowName.setCellType(HSSFCell.CELL_TYPE_STRING); // 设置列头单元格的数据类型
            HSSFRichTextString text = new HSSFRichTextString(rowName[n]);
            cellRowName.setCellValue(text); // 设置列头单元格的值
            cellRowName.setCellStyle(columnTopStyle); // 设置列头单元格样式
        }

        // 将查询出的数据设置到sheet对应的单元格中
        for (int i = 0; i < dataList.size(); i++) {
            Object[] obj = dataList.get(i);// 遍历每个对象
            HSSFRow row = sheet.createRow(i + TITLE_HEIGHT + 1);// 创建所需的行数

            for (int j = 0; j < obj.length; j++) {
                HSSFCell cell = row.createCell(j, HSSFCell.CELL_TYPE_STRING); // 设置单元格的数据类型

                if (!"".equals(obj[j]) && obj[j] != null) {
                    // 设置单元格的值
                    cell.setCellValue(obj[j].toString());
                } else {
                    cell.setCellValue("");
                }
                cell.setCellStyle(style); // 设置单元格样式

            }
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            workbook.write(out);
            byte[] bytes = out.toByteArray();
            return bytes;
        } catch (IOException e) {
            LOG.error(Constants.ERROR_LOG + "导出Excel时异常！{}",
                Throwables.getStackTraceAsString(e));
        }
        return null;
    }

    /*
     * 列头单元格样式
     */
    public HSSFCellStyle getColumnTopStyle(HSSFWorkbook workbook) {

        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        font.setFontHeightInPoints((short) 11);
        // 字体加粗
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框;
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        // 设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        // 设置左边框;
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        // 设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        // 设置右边框;
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        // 设置顶边框;
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        // 设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        return style;

    }

    /*
     * 列数据信息单元格样式
     */
    public HSSFCellStyle getStyle(HSSFWorkbook workbook) {
        // 设置字体
        HSSFFont font = workbook.createFont();
        // 设置字体大小
        // font.setFontHeightInPoints((short)10);
        // 字体加粗
        // font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 设置字体名字
        font.setFontName("Courier New");
        // 设置样式;
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置底边框;
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        // 设置底边框颜色;
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        // 设置左边框;
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        // 设置左边框颜色;
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        // 设置右边框;
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        // 设置右边框颜色;
        style.setRightBorderColor(HSSFColor.BLACK.index);
        // 设置顶边框;
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        // 设置顶边框颜色;
        style.setTopBorderColor(HSSFColor.BLACK.index);
        // 在样式用应用设置的字体;
        style.setFont(font);
        // 设置自动换行;
        style.setWrapText(false);
        // 设置水平对齐的样式为居中对齐;
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 设置垂直对齐的样式为居中对齐;
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        return style;

    }

    /**
     * <p>
     * 导出到文件
     * </p>
     *
     * @action niushuai 2017年3月30日 下午8:33:00 描述
     *
     * @param file
     * @param rowName
     * @param title
     * @param dataList
     *            void
     * @throws IOException
     */
    public static void exportFile(File file, String rowName[], String title,
            List<Object[]> dataList) throws IOException {
        ExportExcel excel = new ExportExcel(title, rowName, dataList);
        try (FileOutputStream out = new FileOutputStream(file)) {
            byte[] bytes = excel.export();
            out.write(bytes);
            out.flush();
            out.close();
        }
    }

    /**
     * <p>
     * 导入excel数据
     * </p>
     *
     * @action niushuai 2017年3月30日 下午9:03:38 描述
     *
     * @param file
     * @return
     * @throws IOException
     *             List<Object[]>
     */
    public static List<Object[]> inportExcel(File file) throws IOException {
        return importExcel(new FileInputStream(file));
    }

    /**
     * <p>
     * 导入excel数据
     * </p>
     *
     * @action niushuai 2017年5月25日 下午6:31:46 描述
     *
     * @param in
     * @return
     * @throws IOException
     *             List<Object[]>
     */
    public static List<Object[]> importExcel(InputStream in)
            throws IOException {
        List<Object[]> list = new ArrayList<Object[]>();
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(in);
        Sheet sheet = hssfWorkbook.getSheetAt(0);
        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row hssfRow = sheet.getRow(rowNum);
            if (hssfRow != null) {
                Object[] array = new Object[hssfRow.getLastCellNum()];
                for (int cellNum = 0; cellNum < hssfRow
                        .getLastCellNum(); cellNum++) {
                    Cell cell = hssfRow.getCell(cellNum);
                    if (cell != null) {
                        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                            array[cellNum] = cell.getStringCellValue();
                        } else if (cell
                                .getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            array[cellNum] = cell.getNumericCellValue();
                        }
                    }
                }
                list.add(array);
            }
        }
        return list;
    }

}