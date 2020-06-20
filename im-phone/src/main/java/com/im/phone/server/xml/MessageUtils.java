package com.im.phone.server.xml;

import java.util.*;

/**
 * 转换xml报文
 */
public class MessageUtils {

    public static String mapToXml(Map<String,Object> map,Map<String,Object> bodyMap){
        Map<String,Object> resultMap = new LinkedHashMap<>();
        Map<String,Object> sysHeadMap = MessageFactory.getSysHead(map);

        Map<String,Object> headMap = new LinkedHashMap<>();
        Map<String,Object> toXmlMap = new LinkedHashMap<>();
        resultMap.put("SYS_HEAD",sysHeadMap);
        headMap.put("HEAD",resultMap);
        headMap.put("BODY",bodyMap);
        toXmlMap.put("SERVICE",headMap);
        return callMapToXML(toXmlMap);
    }

    /**
     * Map 转 XML
     * @param map
     * @return
     */
    public static String callMapToXML(Map<String,Object> map) {
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        mapToXMLTest2(map, sb);
        try {
            return sb.toString();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    private static void mapToXMLTest2(Map<String,Object> map, StringBuffer sb) {
        Set set = map.keySet();
        for (Iterator it = set.iterator(); it.hasNext();) {
            String key = (String) it.next();
            Object value = map.get(key);
            if (null == value)
                value = "";
            if (value.getClass().getName().equals("java.util.ArrayList")) {
                ArrayList list = (ArrayList) map.get(key);
                sb.append("<" + key + ">");
                for (int i = 0; i < list.size(); i++) {
                    HashMap hm = (HashMap) list.get(i);
                    mapToXMLTest2(hm, sb);
                }
                sb.append("</" + key + ">");

            } else {
                if (value instanceof HashMap) {
                    sb.append("<" + key + ">");
                    mapToXMLTest2((HashMap) value, sb);
                    sb.append("</" + key + ">");
                } else {
                    sb.append("<" + key + ">" + value + "</" + key + ">");
                }
            }
        }

    }
}
