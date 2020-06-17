package com.im.phone.server.encryption;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES 高级加密算法，本项目中用于对数据库的验证信息进行加密
 */
public class AESUtil {

    private static String key="ilonw_aes";

    /**
     * 加密
     * @param content
     * @param strKey
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(String content,String strKey ) throws Exception {
        SecretKeySpec skeySpec = getKey(strKey);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(content.getBytes());
        return  encrypted;
    }

    /**
     * 解密
     * @param strKey
     * @param content
     * @return
     * @throws Exception
     */
    public static String decrypt(byte[] content,String strKey ) throws Exception {
        SecretKeySpec skeySpec = getKey(strKey);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] original = cipher.doFinal(content);
        String originalString = new String(original);
        return originalString;
    }

    private static SecretKeySpec getKey(String strKey) throws Exception {
        byte[] arrBTmp = strKey.getBytes();
        byte[] arrB = new byte[16]; // 创建一个空的16位字节数组（默认值为0）

        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }

        SecretKeySpec skeySpec = new SecretKeySpec(arrB, "AES");

        return skeySpec;
    }


    /**
     * base 64 encode
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    public static String base64Encode(byte[] bytes){
        return new BASE64Encoder().encode(bytes);
    }

    /**
     * base 64 decode
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public static byte[] base64Decode(String base64Code) throws Exception{
        return base64Code.isEmpty() ? null : new BASE64Decoder().decodeBuffer(base64Code);
    }

    /**
     * AES加密为base 64 code
     * @param content 待加密的内容
     * @return 加密后的base 64 code
     * @throws Exception  //加密传String类型，返回String类型
     */
    public static String aesEncrypt(String content) throws Exception {
        return base64Encode(encrypt(content, key));

    }
    /**
     * 将base 64 code AES解密
     * @param encryptStr 待解密的base 64 code
     * @return 解密后的string   //解密传String类型，返回String类型
     * @throws Exception
     */
    public static String aesDecrypt(String encryptStr) throws Exception {
        return encryptStr.isEmpty() ? null : decrypt(base64Decode(encryptStr), key);
    }


    /**
     * protected static final String FROM = "jack1053996819@163.com";
     *
     *     protected static final String PROTOCOL_KEY = "mail.transport.protocol";
     *     protected static final String PROTOCOL_VALUE = "smtp";
     *     protected static final String HOST_KEY = "mail.smtp.host";
     *     protected static final String HOST_VALUE = "smtp.163.com";
     *     protected static final String PORT_KEY = "mail.smtp.port";
     *     protected static final String PORT_VALUE = "25";
     *     protected static final String AUTH_KEY = "mail.smtp.auth";
     *     protected static final String AUTH_VALUE = "true";
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String a1 = aesEncrypt("ilonw!QAZ@WSX");

        System.out.println(a1);

//        String decrypt = aesDecrypt("+hf8qB4LhS7L7BzBy83bLg==");
//        System.out.println(decrypt);
    }
}