package com.damocles.android.wechat;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by zhanglong02 on 16/6/28.
 */
public class WechatAPI {

    /**
     * 获取uuid
     */
    private static final String API_jslogin =
            "https://" + "login.wx.qq.com" + "/jslogin?appid=wx782c26e4c19acffb&redirect_uri="
                    + "https%3A%2F%2Fwx.qq.com%2Fcgi-bin%2Fmmwebwx-bin%2Fwebwxnewloginpage"
                    + "&fun=new&lang=zh_CN&_={timestamp}";

    /**
     * 获取二维码
     */
    private static final String API_qrcode = "https://login.weixin.qq.com/qrcode/{uuid}";

    /**
     * 检查是否登录成功<br>
     * code:201 扫码成功，未确认登录
     * code:200 登录成功
     */
    private static final String API_login =
            "https://login.wx.qq.com"
                    + "/cgi-bin/mmwebwx-bin/login?"
                    + "loginicon=true&uuid={uuid}&tip=0&r={r}&_={timestamp}";

    /**
     * web微信初始化，获取用户信息，聊天记录
     */
    private static final String API_webwxinit =
            "https://wx2.qq.com"
                    + "/cgi-bin/mmwebwx-bin/webwxinit?"
                    + "r={r}&lang={lang}&pass_ticket={pass_ticket}";

    /**
     * 获取通讯录
     */
    private static final String API_webwxgetcontact = "https://wx2.qq.com"
            + "/cgi-bin/mmwebwx-bin/webwxgetcontact?"
            + "r={timestamp}&seq=0&skey={skey}&pass_ticket={pass_ticket}&lang={lang}";

    /**
     * 发送消息
     */
    private static final String API_webwxsendmsg = "https://wx2.qq.com"
            + "/cgi-bin/mmwebwx-bin/webwxsendmsg?"
            + "lang={lang}&pass_ticket={pass_ticket}";

    /**
     * 轮询检测新消息
     */
    private static final String API_synccheck = "https://webpush.wx2.qq.com"
            + "/cgi-bin/mmwebwx-bin/synccheck?"
            + "r={r}&skey={skey}&sid={sid}&uin={uin}&deviceid={deviceid}&synckey={synckey}&_={timestamp}";

    /**
     * 同步新消息
     */
    private static final String API_webwxsync = "https://wx2.qq.com"
            + "/cgi-bin/mmwebwx-bin/webwxsync?"
            + "skey={skey}&sid={sid}&pass_ticket={pass_ticket}&lang={lang}";

    public static final String getJSLoginUrl() {
        return API_jslogin.replace("{timestamp}", String.valueOf(System.currentTimeMillis()));
    }

    public static final String getQrCodeUrl(String uuid) {
        return API_qrcode.replace("{uuid}", uuid);
    }

    public static final String getLoginUrl(String uuid) {
        String url = API_login.replace("{uuid}", uuid);
        url = url.replace("{r}", String.valueOf(~new Date().getTime()));
        return url.replace("{timestamp}", String.valueOf(System.currentTimeMillis()));
    }

    public static final String getWebwxInitUrl(String pass_ticket, String lang) {
        String url = API_webwxinit.replace("{pass_ticket}", pass_ticket);
        url = url.replace("{r}", String.valueOf(~new Date().getTime()));
        return url.replace("{lang}", lang);
    }

    public static final String getWebwxContactUrl(String pass_ticket, String skey, String lang) {
        String url = API_webwxgetcontact.replace("{pass_ticket}", pass_ticket);
        url = url.replace("{timestamp}", String.valueOf(System.currentTimeMillis()));
        url = url.replace("{skey}", skey);
        return url.replace("{lang}", lang);
    }

    public static final String getWebwxSendmsgUrl(String pass_ticket, String lang) {
        String url = API_webwxsendmsg.replace("{pass_ticket}", pass_ticket);
        return url.replace("{lang}", lang);
    }

    public static final String getSynccheckUrl(WechatUserInfo userInfo) {
        String url = API_synccheck.replace("{r}", String.valueOf(System.currentTimeMillis()));
        url = url.replace("{skey}", userInfo.skey);
        url = url.replace("{sid}", userInfo.wxsid);
        url = url.replace("{uin}", userInfo.wxuin);
        url = url.replace("{deviceid}", userInfo.generateDeviceId());
        if (userInfo.getSyncKeyString() != null) {
            url = url.replace("{synckey}", userInfo.getSyncKeyString());
        } else {
            url = url.replace("{synckey}", "");
        }
        return url.replace("{timestamp}", String.valueOf(System.currentTimeMillis()));
    }

    public static final String getWebwxSyncUrl(WechatUserInfo userInfo) {
        String url = API_webwxsync.replace("{skey}", userInfo.skey);
        url = url.replace("{sid}", userInfo.wxsid);
        url = url.replace("{pass_ticket}", userInfo.pass_ticket);
        return url.replace("{lang}", userInfo.mm_lang);
    }

    public static String generateCookie() {
        Map<String, String> cookies = new HashMap<String, String>();
        cookies.put("MM_WX_NOTIFY_STATE", "1");
        cookies.put("MM_WX_SOUND_STATE", "1");
        cookies.put("mm_lang", WechatUserInfo.getInstance().mm_lang);
        //        cookies.put("pgv_pvi", "1");
        //        cookies.put("pgv_si", "1");
        cookies.put("wxuin", WechatUserInfo.getInstance().wxuin);
        cookies.put("wxsid", WechatUserInfo.getInstance().wxsid);
        cookies.put("wxloadtime", WechatUserInfo.getInstance().wxloadtime);
        cookies.put("webwx_data_ticket", WechatUserInfo.getInstance().webwx_data_ticket);
        cookies.put("webwxuvid", WechatUserInfo.getInstance().webwxuvid);
        Iterator<String> iterator = cookies.keySet().iterator();
        StringBuffer cookieBuffer = new StringBuffer();
        while (iterator.hasNext()) {
            String key = iterator.next();
            cookieBuffer.append(key).append("=").append(cookies.get(key)).append("; ");
        }
        String cookie = cookieBuffer.toString();
        cookie = cookie.substring(0, cookie.length() - 2);
        return cookie;
    }

}
