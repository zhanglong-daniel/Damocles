package com.damocles.navi;

import com.baidu.navisdk.adapter.BNaviSettingManager;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.damocles.common.log.Log;

/**
 * Created by zhanglong02 on 16/8/24.
 */
class NaviInitListenerImpl implements BaiduNaviManager.NaviInitListener {

    private final static String LOG_TAG =  "navi";

    public NaviInitListenerImpl() {
    }

    @Override
    public void initStart() {
        Log.i(LOG_TAG, "百度导航引擎初始化开始");
    }

    @Override
    public void initSuccess() {
        Log.i(LOG_TAG, "百度导航引擎初始化成功");
        initSetting();
    }

    @Override
    public void initFailed() {
        Log.e(LOG_TAG, "百度导航引擎初始化失败");
    }

    @Override
    public void onAuthResult(int status, String msg) {
        String authinfo;
        if (0 == status) {
            authinfo = "key校验成功!";
        } else {
            authinfo = "key校验失败, " + msg;
        }
        Log.e(LOG_TAG, authinfo);
    }

    private void initSetting() {
        // 设置是否双屏显示
        BNaviSettingManager
                .setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
        // 设置导航播报模式
        BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
        // 是否开启路况
        BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
    }
}
