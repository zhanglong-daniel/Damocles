package com.damocles.android.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

public class RemoteNotification {

	private Context context;
	private int id;

	protected NotificationCompat.Builder builder;

	public RemoteNotification(Context context, RemoteNotificationParams params) {
		this.context = context;
		this.id = params.id;
		this.builder = initBuilder(params);
	}

	public void show() {
		Notification notification = builder.build();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(id, notification);
	}

	private NotificationCompat.Builder initBuilder(
			RemoteNotificationParams params) {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context);
		// remoteViews
		builder.setContent(params.remoteViews);
		// small icon
		if (params.smallIcon != 0) {
			builder.setSmallIcon(params.smallIcon);
		}
		// ticker
		builder.setTicker(params.ticker);
		// ongoing
		builder.setOngoing(params.ongoing);
		// vibrate
		if (params.vibrateEnable) {
			builder.setVibrate(new long[] { 50, 400 });
		}
		// sound
		if (params.soundResId > 0) {
			Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
					+ "://" + context.getPackageName() + "/"
					+ params.soundResId);
			if (sound != null) {
				builder.setSound(sound);
			}
		}
		// defaults
		builder.setDefaults(Notification.DEFAULT_LIGHTS);
		return builder;
	}

}
