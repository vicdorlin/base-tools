package com.jmeng.basetools.utils;

import java.security.MessageDigest;

/**
 * @author linss
 * @program wishing-tree
 * @create 2019-05-27 17:01
 */
public class Md5Util {
    /**
     * 对字符串自行2次MD5加密MD5(MD5(s))
     *
     * @param s
     * @return
     */
    public final static String md5x2(String s) {
        return md5(md5(s));
    }

    /**
     * MD5加密工具类
     *
     * @param s
     * @return
     */
    public final static String md5(String s) {

        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};

        try {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 使用MD5 对两端加密后的密文进行比较
     *
     * @param content    未加密的字符串
     * @param md5Content 已加密的字符串
     * @return boolean
     */
    public static boolean check(String content, String md5Content) {
        return md5(content).equals(md5Content.toUpperCase());
    }
}
