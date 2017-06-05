package com.damocles.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串处理工具类
 * Created by zhanglong02 on 16/7/15.
 */
public final class StringUtil {

    /**
     * String逆序
     *
     * @param str
     *
     * @return
     */
    public static String reverse(String str) {
        return new StringBuffer(str).reverse().toString();
    }

    /**
     * 去除空白字符（空格、制表、换行、回车）
     *
     * @param str
     *
     * @return
     */
    public static String removeBlank(String str) {
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            str = m.replaceAll("");
        }
        return str;
    }

    /**
     * byte转十六进制字符串
     *
     * @param input
     *
     * @return
     */
    public static String byteToHexString(byte[] input) {
        StringBuilder stringBuilder = new StringBuilder();
        int length = input.length;
        for (int i = 0; i < length; i++) {
            byte b = input[i];
            String hexString = Integer.toHexString(0xff & b);
            if (hexString.length() == 1) {
                stringBuilder.append("0");
            }
            stringBuilder.append(hexString);
        }
        return stringBuilder.toString();
    }
}
