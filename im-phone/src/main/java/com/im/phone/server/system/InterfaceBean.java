package com.im.phone.server.system;

import com.im.phone.server.config.SystemConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

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
            log.info("获取加密的url信息："+desUrl+",解密过后的url："+desUrl);
            httpRespons = request.sendPostXml(desUrl,xml);
            log.info("post返回结果为："+httpRespons.getContent());
        } catch (Exception e) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpRespons;
    }
}