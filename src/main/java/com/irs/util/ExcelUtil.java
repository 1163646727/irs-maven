package com.irs.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * Excel操作类
 * 
 * @author wjj
 *
 */
public class ExcelUtil {

	String dirPath = "/orderExcel/";
	Workbook wb;
	Sheet sheet;
	FileOutputStream outputStream;
	private int rowCount;
	private int colCount;

	/**
	 * 构造函数
	 * @param title 标题
	 * @param headers 表头
	 * @param e_type 1.Excel2003 2.Excel2007
	 */
	public ExcelUtil(String title, String[] headers, int e_type) {
		if (e_type == 2)
			wb = new XSSFWorkbook(); // Excel 2007
		else
			wb = new HSSFWorkbook(); // Excel 2003
		sheet = wb.createSheet(title);
		Row row0 = sheet.createRow(0);
		for (int i = 0; i < headers.length; i++) {
			Cell cell = row0.createCell(i, CellType.STRING);
			cell.setCellValue(headers[i]);
			// sheet.autoSizeColumn(i);//自动调整宽度
		}
	}

	public ExcelUtil(String fileName) throws Exception {
		wb = WorkbookFactory.create(new File(fileName));
		sheet = wb.getSheetAt(0);
		rowCount = sheet.getLastRowNum();
		colCount = sheet.getRow(0).getPhysicalNumberOfCells();
	}

	public void writeCell(int r, int c, String value) {
		Row row = sheet.getRow(r);
		if (row == null)
			row = sheet.createRow(r);
		Cell cell = row.createCell(c, CellType.STRING);
		cell.setCellValue(value);
	}

	public void writeFile(String fileName) throws Exception {
		outputStream = new FileOutputStream(fileName);
		wb.write(outputStream);
		outputStream.flush();
		outputStream.close();
	}

	public void close() throws IOException {
		if (outputStream != null)
			outputStream.close();
		if (wb != null)
			wb.close();
	}

	public String readCell(int r, int c) {
		Row row = sheet.getRow(r);
		return getStringCellValue(row.getCell(c));
	}

	private String getStringCellValue(Cell cell) {
		if (cell == null)
			return "";
		String strCell = "";
		switch (cell.getCellTypeEnum()) {
		case STRING:
			strCell = cell.getStringCellValue();
			break;
		case NUMERIC:
			strCell = String.valueOf(cell.getNumericCellValue());
//			strCell = cell.getStringCellValue();
			break;
		case BOOLEAN:
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		case FORMULA:
			// 判断当前的cell是否为Date
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				// 如果是Date类型则，转化为Data格式

				// 方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
				// cellvalue = cell.getDateCellValue().toLocaleString();

				// 方法2：这样子的data格式是不带带时分秒的：2011-10-12
				Date date = cell.getDateCellValue();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				strCell = sdf.format(date);

			}else {// 如果是纯数字
				strCell = String.valueOf(cell.getNumericCellValue());// 取得当前Cell的数值
			}
			break;
		case BLANK:
			strCell = "";
			break;
		default:
			strCell = "";
			break;
		}
		if (strCell.equals("") || strCell == null) {
			return "";
		}
		return strCell;
	}

	public static boolean exportExcel(String fileName, String[] headers, String[][] datas) {
		boolean success = false;
		Workbook wb = null;
		try {
			wb = new XSSFWorkbook();
			Sheet sheet = wb.createSheet("保单数据");
			Row row0 = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = row0.createCell(i, CellType.STRING);
				cell.setCellValue(headers[i]);
				// sheet.autoSizeColumn(i);//自动调整宽度
			}
			for (int rowNum = 0; rowNum < datas.length; rowNum++) {
				Row row = sheet.createRow(rowNum + 1);
				for (int i = 0; i < headers.length; i++) {
					Cell cell = row.createCell(i, CellType.STRING);
					cell.setCellValue(datas[rowNum][i]);
				}
			}
			FileOutputStream outputStream = new FileOutputStream(fileName);
			wb.write(outputStream);
			outputStream.flush();
			outputStream.close();
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		} finally {
			try {
				if (wb != null)
					wb.close();
			} catch (Exception e) {
			}
		}
		return success;
	}

	public int getRowCount() {
		return rowCount;
	}

	public int getColCount() {
		return colCount;
	}

	public static void main(String[] args) {
		// String headers[] = {"AA", "BB", "CC", "DD"};
		// String datas[][] = {
		// {"1", "2", "3", "4"},
		// {"11", "22", "33", "44"},
		// {"111", "222", "333", "444"}
		// };
		// ExcelUtil.exportExcel("aaa.xlsx", headers, datas);
		// System.out.println("successful");
	}
}
