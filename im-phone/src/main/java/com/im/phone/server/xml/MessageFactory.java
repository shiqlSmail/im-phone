package com.im.phone.server.xml;

import com.im.phone.server.util.DateUtil;
import com.im.phone.server.util.OrderCodeFactory;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 组装xml报文格式
 */
public class MessageFactory {

    public static Map<String,Object> getSysHead(Map<String,Object> map){
        Map<String,Object> sysHeadMap = new LinkedHashMap<>();
        sysHeadMap.put("SYS_CHANNEL_ID",map.get("sys_channel_id"));
        sysHeadMap.put("SYS_CHANNEL_NAME",map.get("sys_channel_name"));
        sysHeadMap.put("SYS_DATE", DateUtil.YYYYMMDDHHMMSS_8.format(new Date()));
        sysHeadMap.put("SYS_TIME",DateUtil.YYYYMMDDHHMMSS_6.format(new Date()));
        sysHeadMap.put("SYS_SERIAL", OrderCodeFactory.getOrderCode(Long.valueOf(map.get("key").toString())));
        sysHeadMap.put("APP_IP",map.get("ip"));
        return sysHeadMap;
    }
}
