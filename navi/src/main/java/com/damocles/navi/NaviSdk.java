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
import com.damocles.common.util.CommonUtils;
import com.damocles.navi.callback.NaviCallback;
import com.damocles.navi.callback.NaviInitCallback;
import com.damocles.navi.callback.RoutePlanCallback;

import android.app.Activity;
import android.content.res.Configuration;
import android.view.View;

/**
 * Created by zhanglong02 on 16/8/24.
 */
public class NaviSdk {

    private final static String LOG_TAG = "navi";

    private static final String APP_FOLDER_NAME = "navi";
    private String mSDCardPath = null;

    private BaiduNaviCommonModule mBaiduNaviCommonModule;

    private NaviSdk() {
    }

    private final static class InstanceHolder {
        final static NaviSdk INSTANCE = new NaviSdk();
    }

    public static NaviSdk getInstance() {
        return InstanceHolder.INSTANCE;
    }

    public void initNavi(final Activity activity, NaviInitCallback callback) {
        Log.i("init()");
        if (isInited()) {
            Log.e("navi sdk is Inited!");
            callback.onSuccess();
            return;
        }
        if (!initDirs(activity)) {
            Log.e("init dirs failed!");
            callback.onFailed();
            return;
        }
        // 打开log开关
        BNOuterLogUtil.setLogSwitcher(true);
        BaiduNaviManager.NaviInitListener naviInitListener = new NaviInitListenerImpl(callback);
        BNOuterTTSPlayerCallback ttsCallback = new BNOuterTTSPlayerCallbackImpl();
        BaiduNaviManager.getInstance().init(activity, mSDCardPath, APP_FOLDER_NAME, naviInitListener, ttsCallback, null,
                null);
    }

    public void initNaviCommonModule(Activity activity, NaviCallback callback) {
        int moduleType = NaviModuleImpl.BNaviCommonModuleConstants.ROUTE_GUIDE_MODULE;
        int callbackType = BNaviBaseCallbackModel.BNaviBaseCallbackConstants.CALLBACK_ROUTEGUIDE_TYPE;
        BNaviBaseCallback mBNaviBaseCallback = new NavigationListenerImpl(callback);
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

    public BNRoutePlanNode createRoutePlanNode(double longitude, double latitude, String name) {
        return new BNRoutePlanNode(longitude, latitude, name, null, BNRoutePlanNode.CoordinateType.GCJ02);
    }

    public void routeplanToNavi(Activity activity, BNRoutePlanNode startNode, BNRoutePlanNode endNode, RoutePlanCallback
            callback) {
        Log.i("routeplanToNavi()");
        List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
        list.add(startNode);
        list.add(endNode);
        BaiduNaviManager.getInstance()
                .launchNavigator(activity, list, 1, true, new RoutePlanListenerImpl(startNode, callback));
    }

    public void naviToWindowOfTheWorld(Activity activity, double longitude, double latitude, String name,
                                       RoutePlanCallback callback) {
        BNRoutePlanNode startNode = createRoutePlanNode(longitude, latitude, name);
        BNRoutePlanNode endNode = createRoutePlanNode(113.974428, 22.535891, "世界之窗");
        routeplanToNavi(activity, startNode, endNode, callback);
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

    private boolean initDirs(Activity activity) {
        mSDCardPath = CommonUtils.getExternalCacheDir(activity).getAbsolutePath();
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

}
