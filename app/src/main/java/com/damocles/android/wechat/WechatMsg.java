package com.damocles.android.wechat;

import org.json.JSONObject;

/**
 * Created by zhanglong02 on 16/6/30.
 */
public class WechatMsg {

    private String msgId;
    /**
     * 消息类型：<br>
     * 1 文字<br>
     * 3 图片<br>
     * 34 语音<br>
     * 47 动画表情<br>
     * 62 视频<br>
     */
    private int msgType;
    private String fromUserName;
    private String toUserName;
    private String content;
    private int status;
    private long createTime;

    public WechatMsg(JSONObject json) {
        try {
            msgId = json.getString("MsgId");
            msgType = json.getInt("MsgType");
            fromUserName = json.getString("FromUserName");
            toUserName = json.getString("ToUserName");
            content = json.getString("Content");
            status = json.getInt("Status");
            createTime = json.getLong("CreateTime");
            if (msgType != 1 && msgType != 34) {
                content = "暂不支持的消息类型 type=" + msgType;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getMsgId() {
        return msgId;
    }

    public int getMsgType() {
        return msgType;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public String getToUserName() {
        return toUserName;
    }

    public String getContent() {
        return content;
    }

    public int getStatus() {
        return status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public String getVoiceUrl() {
        return "https://wx2.qq.com"
                + "/cgi-bin/mmwebwx-bin/webwxgetvoice?"
                + "msgid=" + msgId
                + "&skey=" + WechatUserInfo.getInstance().skey;
    }
}
