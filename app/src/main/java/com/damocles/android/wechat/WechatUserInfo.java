package com.damocles.android.wechat;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by zhanglong02 on 16/6/28.
 */
public class WechatUserInfo {

    public static WechatUserInfo getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private WechatUserInfo() {
    }

    private static class InstanceHolder {
        public static final WechatUserInfo INSTANCE = new WechatUserInfo();
    }

    public String skey;
    public String wxsid;
    public String wxuin;
    public String webwxuvid;
    public String webwx_data_ticket;
    public String pass_ticket;
    public String mm_lang;
    public String wxloadtime;

    public String userName;
    public String nickName;

    public List<SyncKey> SyncKeys;
    public List<WechatContact> contacts;

    public String getSyncKeyString() {
        if (SyncKeys == null || SyncKeys.size() == 0) {
            return null;
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0, size = SyncKeys.size(); i < size; i++) {
            buffer.append(SyncKeys.get(i).Key + "_" + SyncKeys.get(i).Val);
            if (i < size - 1) {
                buffer.append("%7c");
            }
        }
        return buffer.toString();
    }

    public JSONObject getSyncKeyJson() {
        if (SyncKeys == null || SyncKeys.size() == 0) {
            return null;
        }
        JSONObject syncKeyJson = new JSONObject();
        JSONArray list = new JSONArray();
        JSONObject itemJson;
        int count = SyncKeys.size();
        try {
            syncKeyJson.put("Count", count);
            for (int i = 0; i < count; i++) {
                itemJson = new JSONObject();
                itemJson.put("Key", SyncKeys.get(i).Key);
                itemJson.put("Val", SyncKeys.get(i).Val);
                list.put(itemJson);
            }
            syncKeyJson.put("List", list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return syncKeyJson;
    }

    public JSONObject getBaseRequestJson() {
        JSONObject baseRequestJson = new JSONObject();
        try {
            baseRequestJson.put("Sid", wxsid);
            baseRequestJson.put("Skey", skey);
            baseRequestJson.put("Uin", wxuin);
            baseRequestJson.put("DeviceID", generateDeviceId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baseRequestJson;
    }

    @Override
    public String toString() {
        return "WechatUserInfo[\n"
                + "\tuserName = " + userName + "\n"
                + "\tnickName = " + nickName + "\n"
                + "\tskey = " + skey + ",\n"
                + "\twxsid = " + wxsid + ",\n"
                + "\twxuin = " + wxuin + ",\n"
                + "\twebwx_data_ticket = " + webwx_data_ticket + ",\n"
                + "\tpass_ticket = " + pass_ticket + ",\n"
                + "\tmm_lang = " + mm_lang + ",\n"
                + "\twxloadtime = " + wxloadtime + ",\n"
                + "\twebwxuvid = " + webwxuvid + "\n"
                + "]";
    }

    public String generateDeviceId() {

        Random random = new Random();

        // 获得随机数
        double pross = (1 + random.nextDouble()) * Math.pow(10, 15);

        // 将获得的获得随机数转化为字符串
        String randomNumStr = String.valueOf(pross);
        Log.e("fuck", "DeviceID = " + randomNumStr);
        // 返回固定的长度的随机数
        String deviceId = "e" + randomNumStr.substring(2, 17);
        Log.e("fuck", "DeviceID = " + deviceId);
        return deviceId;
    }

    public String getContactNameByUserName(String userName) {
        if (contacts == null) {
            return "";
        }
        for (WechatContact contact : contacts) {
            if (contact.UserName.equals(userName)) {
                return TextUtils.isEmpty(contact.RemarkName) ? contact.NickName : contact.RemarkName;
            }
        }
        if (userName.equals(this.userName)) {
            return this.nickName;
        }
        return "";
    }
}
