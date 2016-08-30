package com.damocles.navi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.baidu.navisdk.adapter.BNOuterLogUtil;
import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRouteGuideManager;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNaviBaseCallback;
import com.baidu.navisdk.adapter.BNaviBaseCallbackModel;
import com.baidu.navisdk.adapter.BaiduNaviCommonModule;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.baidu.navisdk.adapter.NaviModuleFactory;
import com.baidu.navisdk.adapter.NaviModuleImpl;
import com.damocles.common.log.Log;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.view.View;

/**
 * Created by zhanglong02 on 16/8/24.
 */
public class NaviSdkWrapper {

    private final static String LOG_TAG = NaviUtils.LOG_TAG;

    private static final String APP_FOLDER_NAME = "wechathelper_BNSDK";
    private String mSDCardPath = null;

    private NaviCallback mCallback;

    private BaiduNaviCommonModule mBaiduNaviCommonModule;

    private NaviSdkWrapper() {
    }

    private final static class InstanceHolder {
        final static NaviSdkWrapper INSTANCE = new NaviSdkWrapper();
    }

    public static NaviSdkWrapper getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void setCallback(NaviCallback callback) {
        this.mCallback = callback;
    }

    public void initNavi(Activity activity) {
        if (!initDirs()) {
            Log.e("init dirs failed!");
            return;
        }
        Log.i("init()");
        // 打开log开关
        BNOuterLogUtil.setLogSwitcher(true);
        BaiduNaviManager.NaviInitListener naviInitListener = new NaviInitListenerImpl();
        BNOuterTTSPlayerCallback ttsCallback = new BNOuterTTSPlayerCallback() {
            @Override
            public int getTTSState() {
                return 0;
            }

            @Override
            public int playTTSText(String s, int i) {
                return 0;
            }

            @Override
            public void phoneCalling() {

            }

            @Override
            public void phoneHangUp() {

            }

            @Override
            public void initTTSPlayer() {

            }

            @Override
            public void releaseTTSPlayer() {

            }

            @Override
            public void stopTTS() {

            }

            @Override
            public void resumeTTS() {

            }

            @Override
            public void pauseTTS() {

            }
        };
        BaiduNaviManager naviManager = BaiduNaviManager.getInstance();
        naviManager.init(activity, mSDCardPath, APP_FOLDER_NAME, naviInitListener, ttsCallback, ttsHandler,
                ttsPlayStateListener);
    }

    public void initNaviCommonModule(Activity activity) {
        int moduleType = NaviModuleImpl.BNaviCommonModuleConstants.ROUTE_GUIDE_MODULE;
        int callbackType = BNaviBaseCallbackModel.BNaviBaseCallbackConstants.CALLBACK_ROUTEGUIDE_TYPE;
        BNaviBaseCallback mBNaviBaseCallback = new NavigationListenerImpl(mCallback);
        NaviModuleImpl naviModule = NaviModuleFactory.getNaviModuleManager();
        mBaiduNaviCommonModule = naviModule.getNaviCommonModule(moduleType, activity, callbackType, mBNaviBaseCallback);
    }

    public boolean isInited() {
        return BaiduNaviManager.isNaviInited();
    }

    public View getView() {
        Log.i("getView()");
        if (mBaiduNaviCommonModule != null) {
            return mBaiduNaviCommonModule.getView();
        } else {
            return null;
        }
    }

    public void routeplanToNavi(Activity activity, BNRoutePlanNode.CoordinateType coType) {
        Log.i("routeplanToNavi()");
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;
        switch (coType) {
            case GCJ02: {
                sNode = new BNRoutePlanNode(116.30142, 40.05087, "百度大厦", null, coType);
                eNode = new BNRoutePlanNode(116.39750, 39.90882, "北京天安门", null, coType);
                break;
            }
            case WGS84: {
                sNode = new BNRoutePlanNode(116.300821, 40.050969, "百度大厦", null, coType);
                eNode = new BNRoutePlanNode(116.397491, 39.908749, "北京天安门", null, coType);
                break;
            }
            case BD09_MC: {
                sNode = new BNRoutePlanNode(12947471, 4846474, "百度大厦", null, coType);
                eNode = new BNRoutePlanNode(12958160, 4825947, "北京天安门", null, coType);
                break;
            }
            case BD09LL: {
                sNode = new BNRoutePlanNode(116.30784537597782, 40.057009624099436, "百度大厦", null, coType);
                eNode = new BNRoutePlanNode(116.40386525193937, 39.915160800132085, "北京天安门", null, coType);
                break;
            }
            default:
                break;
        }
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance()
                    .launchNavigator(activity, list, 1, false, new RoutePlanListenerImpl(sNode, mCallback));
        }
    }

    public void onCreate() {
        Log.i("onCreate()");
        if (mBaiduNaviCommonModule != null) {
            mBaiduNaviCommonModule.onCreate();
        }
    }

    public void onStart() {
        Log.i("onStart()");
        if (mBaiduNaviCommonModule != null) {
            mBaiduNaviCommonModule.onStart();
        }
    }

    public void onResume() {
        Log.i("onResume()");
        if (mBaiduNaviCommonModule != null) {
            mBaiduNaviCommonModule.onResume();
        }
    }

    public void onPause() {
        Log.i("onPause()");
        if (mBaiduNaviCommonModule != null) {
            mBaiduNaviCommonModule.onPause();
        }
    }

    public void onStop() {
        Log.i("onStop()");
        if (mBaiduNaviCommonModule != null) {
            mBaiduNaviCommonModule.onStop();
        }
    }

    public void onDestroy() {
        Log.i("onDestroy()");
        if (mBaiduNaviCommonModule != null) {
            mBaiduNaviCommonModule.onDestroy();
        }
    }

    public void onBackPressed(boolean showDialog) {
        Log.i("onBackPressed()");
        if (mBaiduNaviCommonModule != null) {
            mBaiduNaviCommonModule.onBackPressed(showDialog);
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        Log.i("onConfigurationChanged()");
        if (mBaiduNaviCommonModule != null) {
            mBaiduNaviCommonModule.onConfigurationChanged(newConfig);
        }
    }

    public void forceQuitNaviWithoutDialog() {
        Log.i("forceQuitNaviWithoutDialog()");
        BNRouteGuideManager.getInstance().forceQuitNaviWithoutDialog();
    }

    private boolean initDirs() {
        mSDCardPath = NaviUtils.getSdcardDir();
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 内部TTS播报状态回传handler
     */
    private Handler ttsHandler = new Handler() {
        public void handleMessage(Message msg) {
            int type = msg.what;
            switch (type) {
                case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
                    Log.e(LOG_TAG, "Handler : TTS play start");
                    break;
                }
                case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
                    Log.e(LOG_TAG, "Handler : TTS play end");
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 内部TTS播报状态回调接口
     */
    private BaiduNaviManager.TTSPlayStateListener ttsPlayStateListener = new BaiduNaviManager.TTSPlayStateListener() {

        @Override
        public void playEnd() {
            Log.e(LOG_TAG, "TTSPlayStateListener : TTS play end");
            if (mCallback != null) {
                mCallback.onTtsEnd();
            }
        }

        @Override
        public void playStart() {
            Log.e(LOG_TAG, "TTSPlayStateListener : TTS play start");
            if (mCallback != null) {
                mCallback.onTtsStart();
            }
        }
    };

}
