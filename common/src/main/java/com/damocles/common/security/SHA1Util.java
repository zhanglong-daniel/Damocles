package com.damocles.common.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.damocles.common.util.StringUtil;

/**
 * Created by zhanglong02 on 16/7/15.
 */
public final class SHA1Util {

    private SHA1Util() {
    }

    /**
     * 获取SHA1签名信息（长度40）
     *
     * @param input
     *
     * @return
     *
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String sha1(byte[] input) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        byte[] hash = messageDigest.digest(input);
        return StringUtil.byteToHexString(hash);
    }

}
