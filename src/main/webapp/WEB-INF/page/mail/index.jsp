<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>邮件发送</title>
<link rel="stylesheet" href="${ctx }/layui/css/layui.css" media="all" />
<script>  
    var ctx = "${ctx}";  
</script> 
</head>
<body class="childrenBody">
	<form class="layui-form" style="width: 80%;">
		<div class="layui-form-item">
			<label class="layui-form-label">接收邮箱</label>
			<div class="layui-input-block">
				<input type="text" id="mail" class="layui-input userName"
					lay-verify="required" placeholder="请输入接收邮箱" name="mail" value="">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">邮件标题</label>
			<div class="layui-input-block">
				<input type="text" id="title" class="layui-input userName"
					lay-verify="required" placeholder="请输入标题" name="title" value="">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">邮件内容</label>
			<div class="layui-input-block">
				<input type="text" id="content" class="layui-input userName"
					lay-verify="required" placeholder="请输入邮件内容" name="content" value="">
			</div>
		</div>
				<div class="layui-form-item">
			<div class="layui-input-block">
				<button class="layui-btn" lay-submit="" lay-filter="addAdmin">立即提交</button>
				<span class="send">立即提交</span>
			</div>
		</div>
	</form>
	<script type="text/javascript" src="${ctx }/layui/layui.js"></script>
	<script type="text/javascript">
	
	$(".send").on("click",function(){
		
		alert(1);
		$.ajax({
	    		type: "post",
	            url: ctx+"/test/send"+$("#mail").val()+"&title="+$("#title").val()+"&content="+$("#content").val(),
				success:function(d){
					if(d.code==0){
			        	alert("成功！");
					}else{
			        	alert("失败！");
					}
				}
	    });
	})
		
	var $;
	layui.config({
		base : "js/"
	}).use(['form','layer','jquery'],function(){
		
		form.on("submit(addAdmin)",function(){
	 		//弹出loading
	 		debugger;
	 		$.ajax({
	    		type: "post",
	            url: ctx+"/test/send"+$("#mail").val()+"&title="+$("#title").val()+"&content="+$("#content").val(),
				success:function(d){
					if(d.code==0){
			        	alert("成功！");
					}else{
			        	alert("失败！");
					}
				}
	        });
	 	})
		
	})
		
		
		
	</script>

</body>
</html>