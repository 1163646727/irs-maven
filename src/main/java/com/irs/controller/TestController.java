package com.irs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.irs.util.ResultUtil;

/**
 * class name:TestController <BR>
 * class description: 测试类 <BR>
 * Remark: <BR>
 * @version 1.00 2019年1月28日
 * @author 1024)ChenQi
 */
@Controller
@RequestMapping("test/")
public class TestController {
	
	@RequestMapping("mail")
	public String toMail() {
		
		return "page/mail/index";
	}
	
	@RequestMapping("send/{mail}/{title}/{content}")
	public ResultUtil send(@PathVariable("mail")String mail,
			@PathVariable("title")String title,
			@PathVariable("content")String content) {
		System.out.println("111");
		return ResultUtil.ok();
		
	}

}
