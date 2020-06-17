package com.im.phone.server.config;

import com.im.phone.server.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * 系统配置类
 * 主要为了获取properties里面的各个接口路径值
 */
public class SystemConfig {
    protected static final Logger log = LoggerFactory.getLogger(SystemConfig.class);

    /**
     * 获取properties的所有key-value
     */
    public static String getProperties(String sysConfigKey) {
        Properties pop = new Properties();
        try{
            InputStream in = SystemConfig.class.getResourceAsStream("/application.properties");
            pop.load(in);
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        Enumeration em = pop.propertyNames();
        while(em.hasMoreElements()) {
            String key = (String) em.nextElement();
            String value = pop.getProperty(key);
            Cache.put(key,value);
        }
        String value = (String)Cache.get(sysConfigKey);
        log.info("从缓存中取出value信息："+value);
        return value;
    }
}
