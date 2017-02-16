package com.damocles.android.notification;

import android.widget.RemoteViews;

public class RemoteNotificationParams {

	protected int id = 0;

	public RemoteViews remoteViews;

	public String ticker = "";
	public int smallIcon = 0;

	public int soundResId = 0;
	public boolean vibrateEnable = true;
	public boolean ongoing = true;

	public RemoteNotificationParams(int id) {
		this.id = id;
	}

}
