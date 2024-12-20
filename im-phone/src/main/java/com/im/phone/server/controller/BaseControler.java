package com.im.phone.server.controller;

import com.alibaba.fastjson.JSON;
import com.im.phone.server.cache.ICacheManager;
import com.im.phone.server.crypto.SM2;
import com.im.phone.server.redis.RedisUtils;
import com.im.phone.server.system.InterfaceBean;
import com.im.phone.server.xml.MessageUtils;
import net.sf.json.JSONObject;
import org.bouncycastle.math.ec.ECPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BaseControler extends InterfaceBean {
    @Autowired
    public ICacheManager cacheManager;

    @Autowired
    public RedisUtils redisUtils;

    public  Map<String, Object> sendXmlMsg(String  transCode){
        Map<String, Object> map = new HashMap<>();
        map.put("sys_channel_id",transCode);
        map.put("sys_channel_name","IM即时通讯");
        map.put("key","20200701");
        map.put("ip","127.0.0.1");
        return map;
    }

    public String  responseResult(String transCode,Map<String, Object> bodyMap,String esb){
        Map<String, Object> map = sendXmlMsg(transCode);
        String xml = MessageUtils.mapToXml(map,bodyMap);
        log.info("请求的xml信息为："+xml);
        //对请求参数进行加密
        String signParam = sign(xml);
        JSONObject jsonObject = JSONObject.fromObject(signParam.trim());
        HashMap<String, String> maps = JsonObjectToHashMap(jsonObject);
        //判断签名是否正确
        if(!StringUtils.isEmpty(maps.get("resCode"))){
            if(String.valueOf(maps.get("resCode")).equals("SIGN_ERROR")){
                return signParam;
            }
        }
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
        Map<String, Object> returnMap = new HashMap<String, Object>();
        SM2 sm02 = new SM2();
        try{
            ECPoint publicKey = sm02.importPublicKey("/usr/src/crypto/esb-publickey.pem");
            //ECPoint publicKey = sm02.importPublicKey("H:\\crypto\\esb-publickey.pem");
            byte[] data = sm02.encrypt(sign, publicKey);
            String aesEncrypt1 = SM2.printHexString(data);

            returnMap.put("code","SIGN_SUCCESS");
            returnMap.put("data",aesEncrypt1);
            return JSON.toJSONString(returnMap);
        }catch(Exception e){
            return getBaseResultMaps("SIGN_ERROR","签名异常","");
        }
    }
}
