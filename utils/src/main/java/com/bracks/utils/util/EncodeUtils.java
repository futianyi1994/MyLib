package com.bracks.utils.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * good programmer.
 *
 * @data : 2018-03-01 上午 09:53
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class EncodeUtils {
    /**
     * URL转码
     *
     * @param url
     * @param enc
     * @return
     */
    public static String urlEncoder(String url, String enc) {
        String encode = null;
        try {
            encode = URLEncoder.encode(url, enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encode;
    }

    public static String URLEncoder(String url) {
        return urlEncoder(url, "UTF-8");
    }

    /**
     * URL解码
     *
     * @param s
     * @param enc
     * @return
     */
    public static String urlDecoder(String s, String enc) {
        String decode = null;
        try {
            decode = URLDecoder.decode(s, enc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decode;
    }

    public static String urlDecoder(String s) {
        return urlDecoder(s, "UTF-8");
    }

    /**
     * 使用md5的算法进行加密
     */
    public static String md5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
        // 16进制数字
        String md5code = new BigInteger(1, secretBytes).toString(16);

        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

    public static String toMD5(String text) throws NoSuchAlgorithmException {
        //获取摘要器 MessageDigest
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        //通过摘要器对字符串的二进制字节数组进行hash计算
        byte[] digest = messageDigest.digest(text.getBytes());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            //循环每个字符 将计算结果转化为正整数;
            int digestInt = digest[i] & 0xff;
            //将10进制转化为较短的16进制
            String hexString = Integer.toHexString(digestInt);
            //转化结果如果是个位数会省略0,因此判断并补0
            if (hexString.length() < 2) {
                sb.append(0);
            }
            //将循环结果添加到缓冲区
            sb.append(hexString);
        }
        //返回整个结果
        return sb.toString();
    }
}
