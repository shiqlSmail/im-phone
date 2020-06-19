package com.im.phone.server.controller;

import com.alibaba.fastjson.JSON;
import com.im.phone.server.system.InterfaceBean;
import com.im.phone.server.xml.MessageUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Iterator;
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

    public String getBaseResultMaps(String code,String msg,Object object) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("resCode", code);
        map.put("resMsg", msg);
        map.put("resData", object);
        return JSON.toJSONString(map);
    }

    //1.將JSONObject對象轉換為HashMap<String,String>
    public static HashMap<String, String> JsonObjectToHashMap(JSONObject jsonObj){
        HashMap<String, String> data = new HashMap<String, String>();
        Iterator it = jsonObj.keys();
        while(it.hasNext()){
            String key = String.valueOf(it.next().toString());
            String value = (String)jsonObj.get(key).toString();
            data.put(key, value);
        }
        System.out.println(data);
        return data;
    }
}
