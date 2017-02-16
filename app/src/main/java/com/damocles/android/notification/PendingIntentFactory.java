package com.damocles.android.notification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class PendingIntentFactory {

	public static PendingIntent getActivity(Context context, Intent intent) {
		return PendingIntent.getActivity(context, 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
	}

	public static PendingIntent getBroadcast(Context context, Intent intent) {
		return PendingIntent.getBroadcast(context, 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
	}

	public static PendingIntent getService(Context context, Intent intent) {
		return PendingIntent.getService(context, 0, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
	}

}
