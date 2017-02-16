package com.damocles.android.notification;

import android.app.PendingIntent;
import android.graphics.Bitmap;

public class NotificationParams {

	protected int id = 0;

	public String title = "";
	public String text = "";
	public String ticker = "";
	public Bitmap largeIcon;
	public int smallIcon = 0;
	public PendingIntent pendingIntent;

	public String contentInfo;
	public int soundResId = 0;
	public boolean vibrateEnable = true;
	public boolean ongoing = true;

	public long when = 0L;

	public NotificationParams(int id) {
		this.id = id;
	}

}
