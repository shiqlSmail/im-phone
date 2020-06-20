package com.im.phone.server.controller;

import com.alibaba.fastjson.JSON;
import com.im.phone.server.crypto.SM2;
import com.im.phone.server.system.InterfaceBean;
import com.im.phone.server.xml.MessageUtils;
import net.sf.json.JSONObject;
import org.bouncycastle.math.ec.ECPoint;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BaseControler extends InterfaceBean {

    public  Map<String, Object> sendXmlMsg(String  transCode){
        Map<String, Object> map = new HashMap<>();
        map.put("sys_channel_id",transCode);
        map.put("sys_channel_name","IM即时通讯");
        map.put("key","20200701");
        map.put("ip","192.168.0.5");
        return map;
    }

    public String  responseResult(String transCode,Map<String, Object> bodyMap,String esb){
        Map<String, Object> map = sendXmlMsg(transCode);
        String xml = MessageUtils.mapToXml(map,bodyMap);
        log.info("请求的xml信息为："+xml);
        //对请求参数进行加密
        String signParam = sign(xml);
        log.info("请求的xml加密信息为："+signParam);
        String response = toSendPostXml(esb,signParam);
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
            String value = jsonObj.get(key).toString();
            data.put(key, value);
        }
        System.out.println(data);
        return data;
    }

    public String sign(String sign){
        SM2 sm02 = new SM2();
        ECPoint publicKey = sm02.importPublicKey("H:\\crypto\\esb-publickey.pem");
        byte[] data = sm02.encrypt(sign, publicKey);
        String aesEncrypt1 = SM2.printHexString(data);
        return aesEncrypt1;
    }
}
