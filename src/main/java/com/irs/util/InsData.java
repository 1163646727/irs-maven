package com.irs.util;

/**
 * 导入导出使用对象
 * @author 王聚金
 *
 */
public class InsData {

	private boolean success;
	private int total;
	private int count;
	private String err;
	private String fileName;
	private String errName;
	private String msg;
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getErr() {
		return err;
	}
	public void setErr(String err) {
		this.err = err;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getErrName() {
		return errName;
	}
	public void setErrName(String errName) {
		this.errName = errName;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
