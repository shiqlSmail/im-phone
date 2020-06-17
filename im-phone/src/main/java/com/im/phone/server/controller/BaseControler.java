package com.im.phone.server.controller;

import com.im.phone.server.request.UserRegisterRequest;
import com.im.phone.server.system.InterfaceBean;
import com.im.phone.server.util.IpUtils;
import com.im.phone.server.xml.MessageUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BaseControler extends InterfaceBean {
    @Value("${ilonw.app_id}")
    private String appId;
    @Value("${ilonw.app_key}")
    private String appKey;
    @Value("${ilonw.app_public_key}")
    private String appPublicKey;
    @Value("${ilonw.app_private_key}")
    private String appPrivateKey;

    public  Map<String, Object> sendXmlMsg(String  transCode){
        Map<String, Object> map = new HashMap<>();
        map.put("sys_channel_id",transCode);
        map.put("sys_channel_name","IM即时通讯");
        map.put("key",appId);
        map.put("ip","192.168.0.1");

        map.put("app_id",appId);
        map.put("app_key",appKey);
        map.put("app_public_key",appPublicKey);
        map.put("app_private_key",appPrivateKey);
        return map;
    }

    public String  responseResult(String transCode,Map<String, Object> bodyMap,String esb){
        Map<String, Object> map = sendXmlMsg(transCode);
        String xml = MessageUtils.mapToXml(map,bodyMap);
        log.info("请求的xml信息为："+xml);
        String response = toSendPostXml(esb, xml);
        log.info("最终返回的结果为："+response);
        return response;
    }
}
