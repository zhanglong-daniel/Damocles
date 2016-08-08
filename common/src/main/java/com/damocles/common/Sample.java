package com.damocles.common;

import com.damocles.common.constant.Constants;
import com.damocles.common.log.Log;
import com.damocles.common.security.Base64Util;
import com.damocles.common.security.MD5Util;
import com.damocles.common.security.SHA1Util;
import com.damocles.common.util.HanziToPinyin;
import com.damocles.common.util.StringUtil;
import com.damocles.common.util.Utils;

/**
 * Created by zhanglong02 on 16/7/15.
 */
public class Sample {

    public static void main(String... args) {
        String log = Constants.LOG_TAG;
        boolean flag=false;
        Log.e("test");
        try {
            log = Base64Util.encode(log.getBytes());
            log = MD5Util.md5(log.getBytes());
            log = SHA1Util.sha1(log.getBytes());
            log = HanziToPinyin.getInstance().get(log);
            log = StringUtil.reverse(log);
            flag = Utils.checkPermission(null, null);
        } catch (Exception e) {

        }
        System.out.println(log + String.valueOf(flag));
    }

}
