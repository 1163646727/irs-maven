<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/page/include/taglib.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title>添加管理员</title>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="format-detection" content="telephone=no">
<link rel="stylesheet" href="${ctx }/layui/css/layui.css" media="all" />
<!-- 七牛上传 -->
<link rel="stylesheet" href="${ctx }/assets/qiniu/bootstrap.min.css" />
<link rel="stylesheet" href="${ctx }/assets/qiniu/demo/styles/main.css" />
<!--视频预览-->
<link  rel="stylesheet" href="${ctx }/assets/qiniu/video/css/video-js.min.css">
<script>  
        var ctx = "${ctx}";  
</script> 
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<script type="text/javascript">
function ajaxFileUpload(obj) {
		    var fileName = $(obj).val();		                                //上传的本地文件绝对路径
		    var suffix = fileName.substring(fileName.lastIndexOf("."),fileName.length);	//后缀名
		    var file = $(obj).get(0).files[0];	                                        //上传的文件
		    var size = file.size > 1024 ? file.size / 1024 > 1024 ? file.size / (1024 * 1024) > 1024 ? (file.size / (1024 * 1024 * 1024)).toFixed(2) + 'GB' : (file.size
		        / (1024 * 1024)).toFixed(2) + 'MB' : (file.size 
		    	/ 1024).toFixed(2) + 'KB' : (file.size).toFixed(2) + 'B';		//文件上传大小
		    //七牛云上传
		    $.ajax({
		        type:'post',
				url: ctx+"/user/QiniuUpToken",
		        data:{"suffix":suffix},
		        dataType:'json',
		        success: function(result){
		            if(result.success == 1){
		                var observer = {                         //设置上传过程的监听函数
		                    next(result){                        //上传中(result参数带有total字段的 object，包含loaded、total、percent三个属性)
		                       Math.floor(result.total.percent); //查看进度[loaded:已上传大小(字节);total:本次上传总大小;percent:当前上传进度(0-100)]
		                    },
		                    error(err){                          //失败后
		                       alert(err.message);
		                    },
		                    complete(res1){                      //成功后
		                         //****:填写你的绑定域名或七牛云提供的测试域名
		                         //?imageView2/2/h/100：展示缩略图，不加显示原图
		                        $("#img").attr("src","****/"+result.imgUrl+"?imageView2/2/h/100");
		                    }
		                };
		                var putExtra = {
		                    fname: "",                          //原文件名
		                    params: {},                         //用来放置自定义变量
		                    mimeType: null                      //限制上传文件类型
		                };
		                var config = {
		                    region:qiniu.region.z0,             //存储区域(z0: 代表华东;不写默认自动识别)
		                    concurrentRequestLimit:3            //分片上传的并发请求量
		                };
		                var observable = qiniu.upload(file,result.imgUrl,result.token,putExtra,config);
		                observable.subscribe(observer)          // 上传开始
		            }else{
		              alertMsg(result.message);                 //获取凭证失败
		            }
		        },error:function(){                             //服务器响应失败处理函数
		              alertMsg("服务器繁忙");
		      }
		    });
		 }

</script>
<style type="text/css">
.layui-form-item .layui-inline {
	width: 33.333%;
	float: left;
	margin-right: 0;
}

@media ( max-width :1240px) {
	.layui-form-item .layui-inline {
		width: 100%;
		float: none;
	}
}
</style>
</head>
<body class="childrenBody">
	<form class="layui-form" style="width: 80%;" id="auf">
		<div class="layui-form-item">
			<label class="layui-form-label">登陆邮箱</label>
			<div class="layui-input-block">
				<input type="text" id="eMail" name="eMail" class="layui-input userName"
					lay-verify="email" placeholder="请输入邮箱" value="">
				<label><span style="color: red">(激活账号、找回密码必须！)</span></label>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">昵称</label>
			<div class="layui-input-block">
				<input type="text" id="nickname" class="layui-input userName"
					lay-verify="required" placeholder="请输入昵称" name="nickname" value="">
					<!-- <label><span style="color: red">(谨慎设置，暂时不支持修改！)</span></label> -->
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">密码</label>
			<div class="layui-input-block">
				<input type="password" id="password" class="layui-input userName"
					lay-verify="pass" placeholder="请输入密码" name="password" value="">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">确认密码</label>
			<div class="layui-input-block">
				<input type="password" class="layui-input userName"
					lay-verify="repass" placeholder="请输入确认密码" value="">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">性别</label>
			<div class="layui-input-block">
				<input type="radio" name="sex" value="1" title="男" checked>
				<input type="radio" name="sex" value="0" title="女"> <input
					type="radio" name="sex" value="2" title="保密">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">出生日期</label>
			<div class="layui-input-block">
				<input type="text" id="birthday" class="layui-input userName"
					name="birthday" lay-verify="required" placeholder="请输入出生日期">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">地址</label>
			<div class="layui-input-block">
				<input type="text" name="address" class="layui-input userName" lay-verify="required" placeholder="请输入地址" value="">
			</div>
		</div> 
		<div class="layui-form-item">
			<label class="layui-form-label">手机号</label>
			<div class="layui-input-block">
				<input type="text" name="phone" class="layui-input userName"
					lay-verify="phone" placeholder="请输入手机号" value="">
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">头像</label>
			<div class="layui-input-block">
				<div class="col-md-12">
			        <div id="container">
			            <a class="btn btn-default btn-lg " id="pickfiles" href="#" >
			                <i class="glyphicon glyphicon-plus"></i>
			                <sapn>选择文件</sapn>
			            </a>
			        </div>
			    </div>
			
				<span style="display:none" class="load">上传中。。。</span>
				<span style="display:none" class="success">上传完毕！</span>
				<img class="previewImg" style="height: 50px; width: auto;display: none;" src="#" />	
			   	<div class="previewVideo" style="display: none;">
			      <video id="my-video" class="video-js" controls preload="auto" data-setup="{}" style="width: 99%;height: auto;">
			        <source src="#" type="video/mp4">     
					</video>
			    </div>
			</div>
		</div>
		<div class="layui-form-item">
			<label class="layui-form-label">富文本</label>
			<div class="layui-input-block">
				<script id="editor" type="text/plain" style="width:90%;height:auto;"></script>
			</div>
		</div>
		<div class="layui-form-item">
			<div class="layui-input-block">
				<button class="layui-btn" lay-submit="" lay-filter="addUser">立即提交</button>
			</div>
		</div>
	</form>
	<script type="text/javascript" src="${ctx }/layui/layui.js"></script>
	<script type="text/javascript" src="${ctx }/page/user/addUser.js"></script>
	<!-- 七牛上传 -->
	<script type="text/javascript" src="${ctx }/assets/qiniu/jquery.min.js"></script>
	<script type="text/javascript" src="${ctx }/assets/qiniu/src/plupload/moxie.js"></script>
	<script type="text/javascript" src="${ctx }/assets/qiniu/src/plupload/plupload.dev.js"></script>
	<script type="text/javascript" src="${ctx }/assets/qiniu/src/qiniu.js"></script>
	<script type="text/javascript" src="${ctx }/assets/qiniu/demo/scripts/ui.js"></script>
	<script type="text/javascript" src="${ctx }/assets/qiniu/demo/scripts/multiple.js"></script>
	
    <!--富文本-->
    <script type="text/javascript" charset="utf-8" src="${ctx }/assets/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="${ctx }/assets/ueditor/ueditor.all.min.js"> </script>
    <script type="text/javascript" charset="utf-8" src="${ctx }/assets/ueditor/lang/zh-cn/zh-cn.js"></script>
    <script type="text/javascript" charset="utf-8" src="${ctx }/assets/ueditor/index.js"></script>
</body>
</html>