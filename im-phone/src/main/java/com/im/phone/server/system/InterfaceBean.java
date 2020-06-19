package com.im.phone.server.system;

import com.alibaba.fastjson.JSON;
import com.im.phone.server.config.SystemConfig;
import com.im.phone.server.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.net.ConnectException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 接口bean，用于统一接口调用
 */
public class InterfaceBean extends HttpRequestUtils {

    protected static final Logger log = LoggerFactory.getLogger(InterfaceBean.class);

    /**
     * 发送post请求
     * @param url
     * @return
     */
    public static String toSendPostXml(String url, String xml){
        HttpRespons httpRespons = new HttpRespons();
        try {
            HttpRequester request = new HttpRequester();

            //获取url，需解密
            String desUrl = SystemConfig.getProperties(url);
            log.info("获取url信息："+desUrl);
            httpRespons = request.sendPostXml(desUrl,xml);
            log.info("post返回结果为："+httpRespons.getContent());
        } catch(ConnectException tex){
            //单独处理超时
            return setTimeOut();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return httpRespons.getContent();
    }

    /**
     * 文件上传发送post请求
     * @param url
     * @return
     */
    public static String uploadPortalFileToSendPost(String url, MultipartFile files[], String user){
        String  httpRespons = new String();
        try {
            HttpRequester request = new HttpRequester();

            //获取url，需解密
            String desUrl = SystemConfig.getProperties(url);
            log.info("获取加密的url信息："+desUrl+",解密过后的url："+desUrl);
            httpRespons = request.uploadFileSendPost(desUrl,files,user);
            log.info("post返回结果为："+httpRespons);
        } catch(ConnectException tex){
            //单独处理超时
            return setTimeOut();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return httpRespons;
    }

    /**
     * 设置超时单独处理
     * @return
     */
    public static String setTimeOut(){
        //单独处理超时
        Map<String,Object> timeOutMap = new HashMap<>();
        timeOutMap.put("resCode","TIMEOUT");
        timeOutMap.put("resMessage","请求超时");
        timeOutMap.put("resTimes", DateUtil.getDateTime(new Date()));
        return JSON.toJSONString(timeOutMap);
    }
}