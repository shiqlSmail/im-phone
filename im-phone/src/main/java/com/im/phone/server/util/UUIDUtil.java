package com.im.phone.server.util;

import org.apache.commons.lang.StringUtils;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

public class UUIDUtil {
	static SimpleDateFormat sdf = new SimpleDateFormat("sssss");
	
	/**
	 * 获得指定数目的UUID
	 * 
	 * @param number
	 *            int 需要获得的UUID数量
	 * @return String[] UUID数组
	 */
	public static String[] getUUID(int number) {
		if (number < 1) {
			return null;
		}
		String[] retArray = new String[number];
		for (int i = 0; i < number; i++) {
			retArray[i] = getUUID();
		}
		return retArray;
	}

	/**
	 * 获得一个UUID
	 * 
	 * @return String UUID
	 */
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString();
		// 去掉“-”符号
		return uuid.replaceAll("-", "");
	}
	
	public static String primaryKeyUUID(){
		String uuid = UUID.randomUUID().toString().replaceAll("-", "")+sdf.format(new Date());
		return uuid;
	}
	
	public static String getOrderIdByUUId() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV < 0) {//有可能是负数
            hashCodeV = - hashCodeV;
        }
        // 0 代表前面补充0     
        // 4 代表长度为4     
        // d 代表参数为正数型
        return machineId + String.format("%015d", hashCodeV);
    }
	
	/** * 生成随机文件名：当前年月日时分秒+五位随机数 * * @return */
	public static String getRandomFileName() {
		SimpleDateFormat simpleDateFormat;
		simpleDateFormat = new SimpleDateFormat("yyyyMMddsss");
		Date date = new Date();
		String str = simpleDateFormat.format(date);
		Random random = new Random();
		int rannum = (int) (random.nextDouble() * (999 - 100 + 1)) + 100;
		return rannum + str;// 当前时间
	}
	
	public static char[] generate() {
	    char[] letters = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
	            'K', 'L', 'M', 'N', 'O', 'P','Q', 'R', 'S', 'T', 'U', 'V',
	            'W', 'X', 'Y', 'Z','0','1','2','3','4','5','6','7','8','9'};
	    boolean[] flags = new boolean[letters.length];
	    char[] chs = new char[6];
	    for (int i = 0; i < chs.length; i++) {
	        int index;
	        do {
	            index = (int) (Math.random() * (letters.length));
	        } while (flags[index]);// 判断生成的字符是否重复
	        chs[i] = letters[index];
	        flags[index] = true;
	    }
	    return chs;
	}

	 public static String getRandomString(){  
        String str="1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";  
        Random random=new Random();  
        StringBuffer sb=new StringBuffer();  
        for(int i=0;i<9;i++){  
            int number =random.nextInt(str.length());  
            sb.append(str.charAt(number));  
        }  
        return sb.toString();  
    }

	public static String userCode(int number){
		String vcode = "";
		for (int i = 0; i < number; i++) {
			vcode = vcode + (int)(Math.random() * 9);
		}
		return "1"+vcode;
	}
	 
	public static void main(String[] args) {
		System.out.println(userCode(11));
	}


	/*
	 * 生成32位随机字符串
	 */
	public static String getNonceStr() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replace("-", "");
	}

	/**
	 * 方法用途: 对所有传入参数按照字段名的Unicode码从小到大排序（字典序），并且生成url参数串<br>
	 * 实现步骤: <br>
	 *
	 * @param paraMap
	 *            要排序的Map对象
	 * @param urlEncode
	 *            是否需要URLENCODE
	 * @param keyToLower
	 *            是否需要将Key转换为全小写 true:key转化成小写，false:不转化
	 * @return
	 */
	public static String formatUrlMap(Map<String, String> paraMap, boolean urlEncode, boolean keyToLower) {
		String buff = "";
		Map<String, String> tmpMap = paraMap;
		try {
			List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());
			// 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
			Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
				public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
					return (o1.getKey()).toString().compareTo(o2.getKey());
				}
			});
			// 构造URL 键值对的格式
			StringBuilder buf = new StringBuilder();
			for (Map.Entry<String, String> item : infoIds) {
				if (StringUtils.isNotBlank(item.getKey())) {
					String key = item.getKey();
					String val = item.getValue();
					if (urlEncode) {
						val = URLEncoder.encode(val, "utf-8");
					}
					if (keyToLower) {
						buf.append(key.toLowerCase() + "=" + val);
					} else {
						buf.append(key + "=" + val);
					}
					buf.append("&");
				}

			}
			buff = buf.toString();
			if (buff.isEmpty() == false) {
				buff = buff.substring(0, buff.length() - 1);
			}
		} catch (Exception e) {
			return null;
		}
		return buff;
	}

	/**
	 * 功能描述: 判断值是否为double
	 *
	 * @auther: zdg
	 * @date: 2018/6/15 19:00
	 */
	public static boolean isToDouble(Object o) {

		try {
			Double.valueOf(o.toString());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
