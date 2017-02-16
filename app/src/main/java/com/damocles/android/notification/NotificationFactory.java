package com.damocles.android.notification;

import java.util.List;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;

public class NotificationFactory {

	public static void showNormal(Context context, NotificationParams params) {
		NormalNotification notification = new NormalNotification(context,
				params);
		notification.show();
	}

	public static void showBigText(Context context, NotificationParams params,
			String summary, String bigText) {
		BigTextNotification notification = new BigTextNotification(context,
				params, summary, bigText);
		notification.show();
	}

	public static void showBigPicture(Context context,
			NotificationParams params, String summary, Bitmap picture) {
		BigPictureNotification notification = new BigPictureNotification(
				context, params, summary, picture);
		notification.show();
	}

	public static void showBigInbox(Context context, NotificationParams params,
			String summary, List<String> lines) {
		BigInboxNotification notification = new BigInboxNotification(context,
				params, summary, lines);
		notification.show();
	}

	public static void showRemoteViews(Context context,
			RemoteNotificationParams params) {
		RemoteNotification notification = new RemoteNotification(context,
				params);
		notification.show();
	}

	public static void cancel(Context context, int id) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(id);
	}

	public static void cancelAll(Context context) {
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
	}

}
