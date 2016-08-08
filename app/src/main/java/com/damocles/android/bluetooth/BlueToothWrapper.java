package com.damocles.android.bluetooth;

import java.lang.reflect.Method;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;

/**
 * Created by zhanglong02 on 16/3/10.
 */
public final class BlueToothWrapper {

    private static final BlueToothWrapper INSTANCE = new BlueToothWrapper();

    public static BlueToothWrapper getInstance() {
        return INSTANCE;
    }

    private BluetoothAdapter mAdapter;

    private BlueToothWrapper() {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        try {
            Class<?> c = Class.forName("com.android.settings.bluetooth.RequestPermissionActivity");
            Method[] methods = c.getDeclaredMethods();
            for (int i = 0; i < methods.length; i++) {
                Log.e("fuck", methods[i].toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void enable(Activity activity) {
        if (!mAdapter.isEnabled()) {
            Log.e("fuck", "enable bluetooth.......");
            Intent enabler=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enabler, 12345);//同startActivity(enabler);
//            return mAdapter.enable();
        } else {
            Log.e("fuck", "bluetooth is already enabled!");
        }
    }

    public boolean disable() {
        Log.e("fuck", "disable bluetooth.......");
        return mAdapter.disable();
    }

    public boolean startDiscovery() {
        Log.e("fuck", "startDiscovery.......");
        return mAdapter.startDiscovery();
    }

    public boolean cancelDiscovery() {
        return mAdapter.cancelDiscovery();
    }

    public String getName() {
        return mAdapter.getName();
    }

    public int getState() {
        return mAdapter.getState();
    }

    public String getAddress() {
        return mAdapter.getAddress();
    }

    public Set<BluetoothDevice> getBondedDevices() {
        return mAdapter.getBondedDevices();
    }

}
