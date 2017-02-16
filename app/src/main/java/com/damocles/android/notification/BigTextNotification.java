package com.damocles.android.notification;

import android.content.Context;
import android.support.v4.app.NotificationCompat;

class BigTextNotification extends NormalNotification {

	public BigTextNotification(Context context, NotificationParams params,
			String summary, String bigText) {
		super(context, params);
		NotificationCompat.BigTextStyle textStyle = new NotificationCompat.BigTextStyle();
		textStyle.setBigContentTitle(params.title);
		textStyle.setSummaryText(summary);
		textStyle.bigText(bigText);
		builder.setStyle(textStyle);
	}
}
