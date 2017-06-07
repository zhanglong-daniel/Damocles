package com.damocles;

import com.damocles.android.notification.NotificationFactory;
import com.damocles.android.notification.RemoteNotificationParams;
import com.damocles.android.util.DeviceInfoUtils;
import com.damocles.common.log.Log;
import com.damocles.common.util.DeviceID;
import com.damocles.common.util.DoubleClickExit;
import com.damocles.sample.AnimationActivity;
import com.damocles.sample.BlueToothActivity;
import com.damocles.sample.BroadcastActivity;
import com.damocles.sample.CanvasAnimationActivity;
import com.damocles.sample.CascadeLayoutActivity;
import com.damocles.sample.LEDActivity;
import com.damocles.sample.ListViewChoiceModeActivity;
import com.damocles.sample.ListViewSectionActivity;
import com.damocles.sample.MultiIntentActivity;
import com.damocles.sample.NightModeActivity;
import com.damocles.sample.PendulumActivity;
import com.damocles.sample.PreferenceActivitySample;
import com.damocles.sample.RecyclerViewActivity;
import com.damocles.sample.SwitcherActivity;
import com.damocles.sample.ToastActivity;
import com.damocles.sample.ViewGroupAnimationActivity;
import com.damocles.sample.util.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private LinearLayout mLinearLayout;
    private TextView mTextView;

    private long startTime = 0L;

    private DoubleClickExit mDoubleClickExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startTime = System.currentTimeMillis();
        Log.i("fuck", "onCreate()");
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
        mLinearLayout = (LinearLayout) findViewById(R.id.main_linearlayout);
        mTextView = (TextView) findViewById(R.id.main_txt);
        acquireWakeLock();
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
        Log.i("fuck", "onResume()");
        super.onResume();
        Log.e("used time :" + (System.currentTimeMillis() - startTime));
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
        releaseWakeLock();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        Log.i("fuck", "onWindowFocusChanged()");
        super.onWindowFocusChanged(hasFocus);
        Log.e("fuck", "statusBarHeight = " + DeviceInfoUtils.getStatusBarHeightOnWindowFocusChanged(this));
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

    @Override
    public void onBackPressed() {
        if (mDoubleClickExit == null) {
            mDoubleClickExit = new DoubleClickExit.Builder(this)
                    .setClickInterval(1500)
                    .setClickTips("再按一次退出Damocles")
                    .setCallback(new DoubleClickExit.Callback() {
                        @Override
                        public void onExit() {
                            MainActivity.this.finish();
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    })
                    .build();
        }
        mDoubleClickExit.execute();
    }

    PowerManager.WakeLock wakeLock;

    private void acquireWakeLock() {
        if (wakeLock == null) {
            Log.e("fuck", "Acquiring wake lock");
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, this.getClass().getCanonicalName());
            wakeLock.acquire();
        }

    }

    private void releaseWakeLock() {
        if (wakeLock != null && wakeLock.isHeld()) {
            Log.e("fuck", "Release wake lock");
            wakeLock.release();
            wakeLock = null;
        }

    }

    public void onCustomNotificationClick(View view) {
        RemoteNotificationParams params = new RemoteNotificationParams(5);
        params.ticker = "ticker_custom";
        params.smallIcon = R.mipmap.ic_launcher;
        params.ongoing = true;
        params.vibrateEnable = true;
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_remoteviews_custom);
        params.remoteViews = remoteViews;
        NotificationFactory.showRemoteViews(this, params);
    }

    public void onPendulumClick(View view) {
        startActivity(new Intent(this, PendulumActivity.class));
    }

    public void onHomeClick(View view) {
        moveTaskToBack(false);
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
