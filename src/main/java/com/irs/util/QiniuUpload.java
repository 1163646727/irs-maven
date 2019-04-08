package com.irs.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qiniu.util.Auth;

@Controller
public class QiniuUpload {
	// ******的内容需要查看七牛云账号的相关信息
    private static final String accessKey = "B2Nxuunj2pkcjpPQ1q_elxCLudQ59F5qi--suvWy";    //访问秘钥
    private static final String secretKey = "PUAlajz0hh8Lz5jKhGSJLgMrWQqBT0dsxvwlQZK6";    //授权秘钥
    private static final String bucket = "imgs";       //存储空间名称
    private static final String domain = "http://pji89idnr.bkt.clouddn.com";       //外链域名

    /**
     * 七牛云上传生成凭证
     *
     * @throws Exception
     */
    @RequestMapping("/QiniuUpToken")
    @ResponseBody
    public Map<String, Object> QiniuUpToken(@RequestParam String suffix) throws Exception{
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            //验证七牛云身份是否通过
            Auth auth = Auth.create(accessKey, secretKey);
            //生成凭证
            String upToken = auth.uploadToken(bucket);
            result.put("token", upToken);
            //存入外链默认域名，用于拼接完整的资源外链路径
            result.put("domain", domain);

            // 是否可以上传的图片格式
            /*boolean flag = false;
            String[] imgTypes = new String[]{"jpg","jpeg","bmp","gif","png"};
            for(String fileSuffix : imgTypes) {
                if(suffix.substring(suffix.lastIndexOf(".") + 1).equalsIgnoreCase(fileSuffix)) {
                    flag = true;
                    break;
                }
            }
            if(!flag) {
                throw new Exception("图片：" + suffix + " 上传格式不对！");
            }*/

            //生成实际路径名
            String randomFileName = UUID.randomUUID().toString() + suffix;
            result.put("imgUrl", randomFileName);
            result.put("success", 1);
        } catch (Exception e) {
            result.put("message", "获取凭证失败，"+e.getMessage());
            result.put("success", 0);
        } finally {
            return result;
        }
    }
}
