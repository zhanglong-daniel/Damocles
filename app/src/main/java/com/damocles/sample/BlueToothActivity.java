package com.damocles.sample;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.baidu.naviauto.R;
import com.damocles.android.bluetooth.BlueToothWrapper;
import com.damocles.sample.util.Utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BlueToothActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTextView;
    private ListView mListView;

    private BlueToothWrapper mBlueToothWrapper;

    private Set<BluetoothDevice> bluetoothDevices = new HashSet<BluetoothDevice>();

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {//找到设备
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //                                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                //                bluetoothDevices.add(device);
                Log.e("fuck", device.getName() + "  " + device.getAddress());
                bluetoothDevices.add(device);
                //                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {//搜索完成
                Toast.makeText(context, "搜索完成", Toast.LENGTH_SHORT).show();
                setListView();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBlueToothWrapper = BlueToothWrapper.getInstance();
        setContentView(R.layout.activity_bluetooth);
        Utils.initToolbar(this, R.id.bluetooth_toolbar);
        initViews();

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    private void initViews() {
        mTextView = (TextView) findViewById(R.id.bluetooth_txt);
        mTextView.setText("当前蓝牙：" + mBlueToothWrapper.getName() + "；状态：" + mBlueToothWrapper.getState());
        mListView = (ListView) findViewById(R.id.bluetooth_listview);
        findViewById(R.id.bluetooth_btn_enable).setOnClickListener(this);
        findViewById(R.id.bluetooth_btn_disable).setOnClickListener(this);
        findViewById(R.id.bluetooth_btn_start_discovery).setOnClickListener(this);
        findViewById(R.id.bluetooth_btn_cancel_discovery).setOnClickListener(this);
    }

    private void setListView() {
        mListView.setAdapter(new DeviceListAdapter());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bluetooth_btn_enable:
              mBlueToothWrapper.enable(this);
                    mTextView.setText("当前蓝牙：" + mBlueToothWrapper.getName() + "；状态：" + mBlueToothWrapper.getState());
                Log.e("fuck", "" + mBlueToothWrapper.getAddress());
                break;
            case R.id.bluetooth_btn_disable:
                mBlueToothWrapper.disable();
                mTextView.setText("当前蓝牙：" + mBlueToothWrapper.getName() + "；状态：" + mBlueToothWrapper.getState());
                break;
            case R.id.bluetooth_btn_start_discovery:
                mBlueToothWrapper.startDiscovery();
                break;
            case R.id.bluetooth_btn_cancel_discovery:
                mBlueToothWrapper.cancelDiscovery();
                break;
        }
    }

    private class DeviceListAdapter extends BaseAdapter {

        private List<BluetoothDevice> devices;

        public DeviceListAdapter() {
            Set<BluetoothDevice> bondedDeviceSet = mBlueToothWrapper.getBondedDevices();
            Iterator<BluetoothDevice> iterator = bondedDeviceSet.iterator();
            devices = new ArrayList<BluetoothDevice>();
            while (iterator.hasNext()) {
                devices.add(iterator.next());
            }
            iterator = bluetoothDevices.iterator();
            devices = new ArrayList<BluetoothDevice>();
            while (iterator.hasNext()) {
                devices.add(iterator.next());
            }
        }

        @Override
        public int getCount() {
            return devices.size();
        }

        @Override
        public BluetoothDevice getItem(int position) {
            return devices.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new TextView(BlueToothActivity.this);
            }
            BluetoothDevice device = getItem(position);
            StringBuffer buffer = new StringBuffer();
            buffer.append(device.getName()).append("  ").append(
                    "bondState : " + convertBondState(device.getBondState())).append("\n");
            buffer.append("uuid : " + device.getUuids());
            ((TextView) convertView).setText(buffer.toString());
            return convertView;
        }
    }

    private static String convertBondState(int bondState) {
        String stateStr = null;
        switch (bondState) {
            case BluetoothDevice.BOND_BONDED:
                stateStr = "已配对";
                break;
            case BluetoothDevice.BOND_BONDING:
                stateStr = "配对中。。。";
                break;
            case BluetoothDevice.BOND_NONE:
                stateStr = "未配对";
                break;
            default:
                break;
        }
        return stateStr;
    }
}
