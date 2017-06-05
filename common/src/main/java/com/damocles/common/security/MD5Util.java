package com.damocles.common.security;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.damocles.common.util.StringUtil;

/**
 * Created by zhanglong02 on 16/7/15.
 */
public final class MD5Util {

    private MD5Util() {
    }

    /**
     * 获取MD5签名信息（长度32）
     *
     * @param input
     *
     * @return
     *
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String md5(byte[] input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] hash = messageDigest.digest(input);
        return StringUtil.byteToHexString(hash);
    }

    public static String md5(String str) {
        try {
            return md5(str.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

}
