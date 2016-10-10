package com.damocles.location;

import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.damocles.common.log.Log;

import android.content.Context;

/**
 * Created by zhanglong02 on 16/9/9.
 */
public class LocationSdk {

    private LocationSdk() {
    }

    private final static class InstanceHolder {
        final static LocationSdk INSTANCE = new LocationSdk();
    }

    public static LocationSdk getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private LocationClient mLocationClient = null;
    private LocationCallback mLocationCallback;

    public void start(Context context, LocationCallback callback) {
        if (callback == null) {
            throw new NullPointerException("LocationCallback is NULL");
        }
        mLocationCallback = callback;
        init(context);
        Log.i("location", "location start");
        mLocationClient.start();
    }

    private void init(Context context) {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(context);
            mLocationClient.registerLocationListener(new BDLocationListenerImpl());
            initOption();
        }
    }

    private void initOption() {
        LocationClientOption option = new LocationClientOption();
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setCoorType("gcj02");
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setScanSpan(0);
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true);
        //可选，默认false,设置是否使用gps
        option.setOpenGps(true);
        //可选，默认false，设置是否当gps有效时按照1秒1次频率输出GPS结果
        option.setLocationNotify(false);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIsNeedLocationPoiList(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setIgnoreKillProcess(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setEnableSimulateGps(false);
        mLocationClient.setLocOption(option);
    }

    private class BDLocationListenerImpl implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            Log.i("location", "location end");
            String address;
            double lng;
            double lat;
            StringBuffer sb = new StringBuffer(256);
            sb.append("\ntime : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            lat = location.getLatitude();
            sb.append(lat);
            sb.append("\nlontitude : ");
            lng = location.getLongitude();
            sb.append(lng);
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                address = location.getAddrStr();
                sb.append(address);
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
                mLocationCallback.onSuccess(address, lng, lat);
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                address = location.getAddrStr();
                sb.append(address);
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
                mLocationCallback.onSuccess(address, lng, lat);
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
                mLocationCallback.onSuccess(location.getAddrStr(), lng, lat);
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                mLocationCallback.onFailed(location.getLocType(), "服务端网络定位失败");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不通导致定位失败，请检查网络是否通畅");
                mLocationCallback.onFailed(location.getLocType(), "网络不通导致定位失败");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                mLocationCallback.onFailed(location.getLocType(), "无法获取有效定位依据导致定位失败");
            } else {
                mLocationCallback.onFailed(location.getLocType(), "未知错误");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.i("location", sb.toString());
            mLocationClient.stop();
        }
    }

}
