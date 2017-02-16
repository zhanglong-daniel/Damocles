package com.damocles.android.notification;

import java.util.List;

import android.content.Context;
import android.support.v4.app.NotificationCompat;

class BigInboxNotification extends NormalNotification {

	public BigInboxNotification(Context context, NotificationParams params,
			String summary, List<String> lines) {
		super(context, params);
		NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
		inboxStyle.setBigContentTitle(params.title);
		inboxStyle.setSummaryText(summary);
		if (lines != null) {
			for (String line : lines) {
				inboxStyle.addLine(line);
			}
		}
		builder.setStyle(inboxStyle);
	}
}
