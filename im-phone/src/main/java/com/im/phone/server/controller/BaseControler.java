package com.im.phone.server.controller;

import com.im.phone.server.system.InterfaceBean;
import org.springframework.beans.factory.annotation.Value;

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
}
