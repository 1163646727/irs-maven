/*global Qiniu */
/*global plupload */
/*global FileProgress */
/*global hljs */
//主要配置 domain 和  uptoken_url 就好

var avatarList = [];  //存放图片的文件名
Date.prototype.format = function (format) {//格式化时间用的，方便下面上传文件时用时间来自定义文件名
    var o = {
        "M+": this.getMonth() + 1, //month
        "d+": this.getDate(), //day
        "h+": this.getHours(), //hour
        "m+": this.getMinutes(), //minute
        "s+": this.getSeconds(), //second
        "q+": Math.floor((this.getMonth() + 3) / 3), //quarter
        "S": this.getMilliseconds() //millisecond
    }

    if (/(y+)/i.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }

    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
}


$(function() {
    var uploader = Qiniu.uploader({
        runtimes: 'html5,flash,html4',  //上传模式,依次退化

        //uptoken_url: '/getQiNiuToken.shtml',     //Ajax请求upToken的Url，**强烈建议设置**（服务端提供） 接口
        uptoken : 'B2Nxuunj2pkcjpPQ1q_elxCLudQ59F5qi--suvWy:t_eOvyX5y_zf67Of4pnPj29zUck=:eyJzY29wZSI6IndvcmsiLCJkZWFkbGluZSI6MjIwOTUxMjM2MH0=', //若未指定uptoken_url,则必须指定 uptoken ,uptoken由其他程序生成
        // unique_names: true, // 默认 false，key为文件名。若开启该选项，SDK为自动生成上传成功后的key（文件名）。
        // save_key: true,   // 默认 false。若在服务端生成uptoken的上传策略中指定了 `sava_key`，则开启，SDK会忽略对key的处理
        //domain: $('#domain').val(),  //bucket 域名，下载资源时用到，**必需**  若是测试域名 记得加上 "http://"
        domain:"http://pkuelfh35.bkt.clouddn.com",
        browse_button: 'pickfiles',       //上传选择的点选按钮，**必需**（页面标签的ID）
        container: 'container',          //上传区域DOM ID，默认是browser_button的父元素，
        drop_element: 'container', //拖拽区域DOM ID，拖曳文件或文件夹后可触发上传
        max_file_size: '100mb',  //最大文件体积限制
        flash_swf_url: 'js/plupload/Moxie.swf',    //引入flash,相对路径
        dragdrop: true,          //开启可拖曳上传
        max_retries: 3,                   //上传失败最大重试次数
        chunk_size: '4mb',     //分块上传时，每片的体积
    
        auto_start: true,     //选择文件后自动上传，若关闭需要自己绑定事件触发上传
        init: {
            'FilesAdded': function(up, files) {
                $('.load').show();
                $('.success').hide();
                $('.previewImg').hide();
	            $('.previewVideo').hide();
                plupload.each(files, function(file) {    // 文件添加进队列后,处理相关的事情
                    var progress = new FileProgress(file, 'fsUploadProgress');
                    progress.setStatus("等待...");  
                });
            },
            'BeforeUpload': function(up, file) {   // 每个文件上传前,处理相关的事情
                var progress = new FileProgress(file, 'fsUploadProgress');
                var chunk_size = plupload.parseSize(this.getOption('chunk_size'));
                if (up.runtime === 'html5' && chunk_size) {
                    progress.setChunkProgess(chunk_size);
                }
            },
            'UploadProgress': function(up, file) {
                var progress = new FileProgress(file, 'fsUploadProgress');
                var chunk_size = plupload.parseSize(this.getOption('chunk_size'));

                progress.setProgress(file.percent + "%", file.speed, chunk_size);
            },
            'UploadComplete': function() {   //队列文件处理完毕后,处理相关的事情
            	$('.load').hide();
                $('.success').show();
            },
            // 每个文件上传成功后,处理相关的事情+
            'FileUploaded': function(up, file, info) {        
               // 其中 up、info 是文件上传成功后，服务端返回的json，
               var responseJSON = JSON.parse(info.response)
               var src = up.settings.domain+"/"+responseJSON.key;
               console.log("url===="+src)

               var type = file.type.split("/")[0];
               //console.log("type="+type);
              switch(type){
              	case "image":
	              	$('.previewImg').show();
	               	//$('.previewVideo').hide();
	               	$(".previewImg").attr("src",src);
	               	break;
               	case "video":
	               	//$('.previewImg').hide();
	   				$(".previewVideo source").attr("src",src);
	   				$('.previewVideo').show();
	   				$(".previewVideo video").load();
	   				//$(".previewVideo video")[0].play();
	   				break;
              }
               
                var progress = new FileProgress(file, 'fsUploadProgress');
                // progress.setComplete(up, info);
                 var res = window.JSON.parse(info.response);
                 var fileName = res.key;// 获取上传成功后的文件的Url
            },
            'Error': function(up, err, errTip) {   //上传出错时,处理相关的事情
                $('table').show();
                var progress = new FileProgress(err.file, 'fsUploadProgress');
                progress.setError();
                progress.setStatus(errTip);

            },
            'Key': function (up, file) { // 若想在前端对每个文件的key进行个性化处理，可以配置该函数
                        // 该配置必须要在 unique_names: false , save_key: false 时才生效
                //var key = "upload/avatar/"+$("#avatarKey").val() + new Date().getTime() + ".jpg";
                var now = new Date();
                var fileName = file.name;
                var fileTypeName = fileName.substring(fileName.lastIndexOf('.'), fileName.length);
                var key = "upload/trends/" + now.format("yyyyMMdd") + "/" + +now.getTime() + fileTypeName;
                $('#fileUrl').val("/" + key);
                if (avatarList.length <= 9) {
                    avatarList.push("/" + key);
                } else {
                    alert("最多只可以上传9张图片");
                    return null;
                }
                return key;
            }
        }
    });

    uploader.bind('FileUploaded', function() {
        //console.log('每个文件上传成功后,处理相关的事情    hello man,a file is uploaded');
    });


    $('body').on('click', 'table button.btn', function() {
        $(this).parents('tr').next().toggle();
    });


    var getRotate = function(url) {
        if (!url) {
            return 0;
        }
        var arr = url.split('/');
        for (var i = 0, len = arr.length; i < len; i++) {
            if (arr[i] === 'rotate') {
                return parseInt(arr[i + 1], 10);
            }
        }
        return 0;
    };

    $('#myModal-img .modal-body-footer').find('a').on('click', function() {
        var img = $('#myModal-img').find('.modal-body img');
        var key = img.data('key');
        var oldUrl = img.attr('src');
        var originHeight = parseInt(img.data('h'), 10);
        var fopArr = [];
        var rotate = getRotate(oldUrl);
        if (!$(this).hasClass('no-disable-click')) {
            $(this).addClass('disabled').siblings().removeClass('disabled');
            if ($(this).data('imagemogr') !== 'no-rotate') {
                fopArr.push({
                    'fop': 'imageMogr2',
                    'auto-orient': true,
                    'strip': true,
                    'rotate': rotate,
                    'format': 'png'
                });
            }
        } else {
            $(this).siblings().removeClass('disabled');
            var imageMogr = $(this).data('imagemogr');
            if (imageMogr === 'left') {
                rotate = rotate - 90 < 0 ? rotate + 270 : rotate - 90;
            } else if (imageMogr === 'right') {
                rotate = rotate + 90 > 360 ? rotate - 270 : rotate + 90;
            }
            fopArr.push({
                'fop': 'imageMogr2',
                'auto-orient': true,
                'strip': true,
                'rotate': rotate,
                'format': 'png'
            });
        }

        $('#myModal-img .modal-body-footer').find('a.disabled').each(function() {

            var watermark = $(this).data('watermark');
            var imageView = $(this).data('imageview');
            var imageMogr = $(this).data('imagemogr');

            if (watermark) {
                fopArr.push({
                    fop: 'watermark',
                    mode: 1,
                    image: 'http://www.b1.qiniudn.com/images/logo-2.png',
                    dissolve: 100,
                    gravity: watermark,
                    dx: 100,
                    dy: 100
                });
            }

            if (imageView) {
                var height;
                switch (imageView) {
                    case 'large':
                        height = originHeight;
                        break;
                    case 'middle':
                        height = originHeight * 0.5;
                        break;
                    case 'small':
                        height = originHeight * 0.1;
                        break;
                    default:
                        height = originHeight;
                        break;
                }
                fopArr.push({
                    fop: 'imageView2',
                    mode: 3,
                    h: parseInt(height, 10),
                    q: 100,
                    format: 'png'
                });
            }

            if (imageMogr === 'no-rotate') {
                fopArr.push({
                    'fop': 'imageMogr2',
                    'auto-orient': true,
                    'strip': true,
                    'rotate': 0,
                    'format': 'png'
                });
            }
        });

        var newUrl = Qiniu.pipeline(fopArr, key);

        var newImg = new Image();
        img.attr('src', 'images/loading.gif');
        newImg.onload = function() {
            img.attr('src', newUrl);
            img.parent('a').attr('href', newUrl);
        };
        newImg.src = newUrl;
        return false;
    });

});
