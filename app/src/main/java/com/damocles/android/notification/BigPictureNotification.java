package com.damocles.android.notification;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat.BigPictureStyle;

class BigPictureNotification extends NormalNotification {

	public BigPictureNotification(Context context, NotificationParams params,
			String summary, Bitmap picture) {
		super(context, params);
		BigPictureStyle pictureStyle = new BigPictureStyle();
		pictureStyle.setBigContentTitle(params.title);
		pictureStyle.setSummaryText(summary);
		pictureStyle.bigPicture(picture);
		builder.setStyle(pictureStyle);
	}
}
