package com.damocles.android.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {

	public static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.84 Safari/537.36";

	public static boolean isNetworkConnected(Context context) {
		NetworkInfo activeInfo = getActiveNetworkInfo(context);
		return activeInfo != null && activeInfo.isConnected();
	}

	public static boolean isWifiConnected(Context context) {
		NetworkInfo activeInfo = getActiveNetworkInfo(context);
		if (activeInfo != null && activeInfo.isConnected()) {
			return activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
		}
		return false;
	}

	public static boolean isMobileConnected(Context context) {
		NetworkInfo activeInfo = getActiveNetworkInfo(context);
		if (activeInfo != null && activeInfo.isConnected()) {
			return activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;
		}
		return false;
	}

	public static String getNetworkInfoTypeName(Context context) {
		NetworkInfo activeInfo = getActiveNetworkInfo(context);
		return activeInfo == null ? null : activeInfo.getTypeName();
	}

	private static NetworkInfo getActiveNetworkInfo(Context context) {
		if (context == null) {
			return null;
		}
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return connMgr.getActiveNetworkInfo();
	}
}
