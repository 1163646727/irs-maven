package com.irs.model;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.irs.util.ExcelUtil;
import com.irs.util.InsData;


/**
 * 导入导出
 * @author 王聚金
 *
 */
public class ImpExpModel {

	/**
	 *  数据导入
	 * @param taskId 任务ID
	 * @param fileName 文件名
	 * @param tableName 表名
	 * @param field 不允许重复的字段名
	 * @param headers 表头字段数据
	 * @param userId 登录用户ID
	 * @param sysId 系统ID
	 * @param type 字典类型
	 * @return {@link InsData}
	 */
	public static InsData imp(String taskId, String fileName, String tableName, String field, String[][] headers, int userId, int sysId, String type) {
		InsData id = new InsData();
		try {
			ExcelUtil excel = new ExcelUtil("webapp" + fileName);
			int rowCount = excel.getRowCount();
			Map<String, String> errMap = new HashMap<String, String>();
			int total = 0;
			for(int i=1; i<=rowCount; i++) {
				StringBuffer errStr = new StringBuffer();
				//获取每一行的第一个数据，如果为空，自动过滤
				String code = excel.readCell(i, 0);
				if(StringUtils.isEmpty(code)) continue;
				total ++;
			}
			excel.close();
			if(errMap.size() > 0) { //如果有错误，将错误整理并生成文件提供下载
				String[][] data = new String[errMap.size()][2];
				int ex = 0;
				for (String getKey : errMap.keySet()) {
					data[ex][0] = getKey;
					data[ex++][1] = errMap.get(getKey);
				}
				String errName = File.separator + "upload" + File.separator + "orderExcel" + File.separator + "err_" + System.currentTimeMillis() + ".xls";
				ExcelUtil.exportExcel("webapp" + errName, new String[] {"主字段", "错误信息"},  data); 
				id.setErrName(errName);
			}
			id.setSuccess(true);
			id.setTotal(total - errMap.size());
			id.setCount(errMap.size());
		}catch(Exception e) {
			e.printStackTrace();
			id.setSuccess(false);
			id.setErr("服务器异常，联系管理员");
		}
		return id;
	}
	
	/**
	 * 数据导出
	 * @param taskId 任务ID
	 * @param sheetname Sheet名称
	 * @param fileName 文件名
	 * @param headers 表头字段数据
	 * @param datas 查询的数据集合
	 * @return {@link InsData}
	 */
	public static InsData exp(String taskId, String sheetname, String fileName, String[][] headers, List<Object> datas) {
		InsData res = new InsData();
		//以下执行导出程序
		try {
			/**
			 * 初始化导出组件
			 * 参数1：文件中sheet名
			 * 参数2：表头
			 * 参数3：1.Excel2003 2.Excel2007
			 */
			ExcelUtil excel = new ExcelUtil(sheetname, headers[0], 2);
			int row = 0, col = 0;
			for(Object r : datas) {
				/** 循环遍历数据，可以在里面处理单列数据 */
				col = 0;
				row++;
				String content;
				for(String header : headers[1]) {
					/********************************此处可能要修改或者增加**********************/
//							if("sex".equals(header)) { //用户状态
//								content = StaticObject.USER_SEX.get(r.getInt(header));
//							}else {
						content = r.get(header)==null?"":r.get(header).toString();
//							}
					/********************************此处可能要修改或者增加**********************/
					excel.writeCell(row, col++, content); //写入每格数据
				}
			}
			excel.writeFile("webapp"+ fileName);//写入文件
			/** 关闭资源 */
			excel.close();
			res.setSuccess(true);
			res.setTotal(datas.size());
			res.setFileName(fileName);
		}catch(Exception e) {
			e.printStackTrace();
			res.setSuccess(false);
			res.setMsg(e.getMessage());
		}
		return res;
	}
}
