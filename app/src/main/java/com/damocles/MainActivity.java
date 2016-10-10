package com.damocles;

import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.damocles.android.util.DeviceInfoUtils;
import com.damocles.common.log.Log;
import com.damocles.common.util.CommonUtils;
import com.damocles.common.util.DeviceID;
import com.damocles.location.LocationCallback;
import com.damocles.location.LocationSdk;
import com.damocles.navi.callback.NaviInitCallback;
import com.damocles.navi.NaviSdk;
import com.damocles.navi.callback.RoutePlanCallback;
import com.damocles.sample.AnimationActivity;
import com.damocles.sample.BlueToothActivity;
import com.damocles.sample.BroadcastActivity;
import com.damocles.sample.CanvasAnimationActivity;
import com.damocles.sample.CascadeLayoutActivity;
import com.damocles.sample.LEDActivity;
import com.damocles.sample.ListViewChoiceModeActivity;
import com.damocles.sample.ListViewSectionActivity;
import com.damocles.sample.MultiIntentActivity;
import com.damocles.sample.NaviActivity;
import com.damocles.sample.NightModeActivity;
import com.damocles.sample.PendulumActivity;
import com.damocles.sample.PreferenceActivitySample;
import com.damocles.sample.RecyclerViewActivity;
import com.damocles.sample.SwitcherActivity;
import com.damocles.sample.TTSActivity;
import com.damocles.sample.ToastActivity;
import com.damocles.sample.ViewGroupAnimationActivity;
import com.damocles.sample.util.Utils;
import com.damocles.tts.TTSPlayer;

import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private LinearLayout mLinearLayout;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = Utils.initToolbar(this, R.id.main_toolbar);
        mToolbar.setLogo(R.mipmap.ic_launcher);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        mTextView = (TextView) findViewById(R.id.main_txt);
        mLinearLayout = (LinearLayout) findViewById(R.id.main_linearlayout);
        init();
    }

    @Override
    protected void onStart() {
        Log.i("onStart()");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.i("onRestart()");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        Log.i("onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i("onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i("onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i("onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.i("onWindowFocusChanged()");
        super.onWindowFocusChanged(hasFocus);
        Log.e("statusBarHeight = " + DeviceInfoUtils.getStatusBarHeightOnWindowFocusChanged(this));
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("DeviceID = " + DeviceID.getID(this)).append("\n");
        stringBuffer.append("分辨率：").append(DeviceInfoUtils.getScreenWidth());
        stringBuffer.append(" * ").append(DeviceInfoUtils.getScreenHeight());
        stringBuffer.append("\nLinearLayout size : width = ").append(mLinearLayout.getWidth());
        stringBuffer.append(" ; height = ").append(mLinearLayout.getHeight());
        mTextView.setText(stringBuffer.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.e(DeviceInfoUtils.getScreenWidth() + " ; " + DeviceInfoUtils.getScreenHeight());
        super.onConfigurationChanged(newConfig);
    }

    private void init() {
        Toast.makeText(MainActivity.this, "正在初始化百度TTS", Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                TTSPlayer.getInstance().init(MainActivity.this);
                TTSPlayer.getInstance().play("欢迎使用达摩克里斯");
            }
        }, 1000);

    }

    public void onPendulumClick(View view) {
        startActivity(new Intent(this, PendulumActivity.class));
    }

    public void onTTSClick(View view) {
        startActivity(new Intent(this, TTSActivity.class));
    }

    public void onNaviClick(View view) {
        TTSPlayer.getInstance().play("正在为您规划路线");
        Toast.makeText(MainActivity.this, "正在为您规划路线", Toast.LENGTH_SHORT).show();
        LocationSdk.getInstance().start(this, new LocationCallback() {
            @Override
            public void onSuccess(final String address, final double lng, final double lat) {
                Log.i("定位成功");
                if (NaviSdk.getInstance().isInited()) {
                    naviToWindowOfTheWorld(lng, lat, address);
                } else {
                    NaviSdk.getInstance().initNavi(MainActivity.this, new NaviInitCallback() {
                        @Override
                        public void onSuccess() {
                            naviToWindowOfTheWorld(lng, lat, address);
                        }

                        @Override
                        public void onFailed() {
                            TTSPlayer.getInstance().play("百度导航初始化失败，请重试");
                            Toast.makeText(MainActivity.this, "百度导航初始化失败，请重试", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailed(int code, String msg) {
                TTSPlayer.getInstance().play("当前位置定位失败");
                Toast.makeText(MainActivity.this, "当前位置定位失败 code=" + code, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void naviToWindowOfTheWorld(double longitude, double latitude, String name) {
        RoutePlanCallback cb = new RoutePlanCallback() {
            @Override
            public void onRoutePlanSuccess(BNRoutePlanNode routePlanNode) {
                Intent intent = new Intent(MainActivity.this, NaviActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("routePlanNode", routePlanNode);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onRoutePlanFailed() {
                TTSPlayer.getInstance().play("路线规划失败");
                Toast.makeText(MainActivity.this, "路线规划失败", Toast.LENGTH_SHORT).show();
            }
        };
        NaviSdk.getInstance().naviToWindowOfTheWorld(MainActivity.this, longitude, latitude, name, cb);
    }

    public void onAmapClick(View view) {
        if (!CommonUtils.isAppInstalled(this, "com.autonavi.amapauto")) {
            Toast.makeText(MainActivity.this, "未安装高德地图车机版", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        Uri uri = Uri.parse(
                "androidauto://navi?sourceApplication=damocles&poiname=fangheng"
                        + "&lat=22.535891&lon=113.974428&dev=0&style=0");
        intent.setData(uri);
        startActivity(intent);
    }

    public void onHomeClick(View view) {
        moveTaskToBack(false);
    }

    public void onWechatClick(View view) {
        if (CommonUtils.isAppInstalled(this, "com.baidu.wechathelper")) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setComponent(new ComponentName("com.baidu.wechathelper", "com.baidu.wechathelper.MainActivity"));
            startActivity(intent);
        } else {
            Toast.makeText(this, "微信助手未安装", Toast.LENGTH_SHORT).show();
        }
    }

    public void onNightModeClick(View view) {
        startActivity(new Intent(this, NightModeActivity.class));
    }

    public void onRecyclerViewClick(View view) {
        startActivity(new Intent(this, RecyclerViewActivity.class));
    }

    public void onAnimationClick(View view) {
        startActivity(new Intent(this, AnimationActivity.class));
    }

    public void onBluetoothClick(View view) {
        startActivity(new Intent(this, BlueToothActivity.class));
    }

    public void onMultiIntentClick(View view) {
        startActivity(new Intent(this, MultiIntentActivity.class));
    }

    public void onListViewChoiceModeClick(View view) {
        startActivity(new Intent(this, ListViewChoiceModeActivity.class));
    }

    public void onListViewSectionClick(View view) {
        startActivity(new Intent(this, ListViewSectionActivity.class));
    }

    public void onBroadcastClick(View view) {
        startActivity(new Intent(this, BroadcastActivity.class));
    }

    public void onToastClick(View view) {
        startActivity(new Intent(this, ToastActivity.class));
    }

    public void onLEDClick(View view) {
        startActivity(new Intent(this, LEDActivity.class));
    }

    public void onCanvasAnimationClick(View view) {
        startActivity(new Intent(this, CanvasAnimationActivity.class));
    }

    public void onViewGroupAnimationClick(View view) {
        startActivity(new Intent(this, ViewGroupAnimationActivity.class));
    }

    public void onSwitcherClick(View view) {
        startActivity(new Intent(this, SwitcherActivity.class));
    }

    public void onCascadeClick(View view) {
        startActivity(new Intent(this, CascadeLayoutActivity.class));
    }

    public void onPreferenceClick(View view) {
        startActivity(new Intent(this, PreferenceActivitySample.class));
    }

}
