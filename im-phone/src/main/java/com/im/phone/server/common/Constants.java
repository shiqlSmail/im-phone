package com.im.phone.server.common;

/**
 * 常量表
 * 记录所有交易码
 */
public interface Constants {
    public static final String ESBXML = "1001";
    public static final String ESBJSON = "1002";

    /****************************  用户中心接口  **********************************/
    public static final String USER_LOGIN = "1001002";
    public static final String USER_REGISTER = "1001001";
    public static final String USER_CHECKPHONE = "1001003";
    public static final String USER_FINDUSERBYID = "1001006";

    /****************************  短信/邮箱中心接口  **********************************/
    public static final String SEND_SMS = "3001001";
    public static final String SEND_EMAIL_CODE = "3001002";
    public static final String SEND_EMAIL_URL = "3001003";

}
