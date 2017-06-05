package com.damocles.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 自动展开listview（scollview嵌套时使用）
 * 
 * @author Daniel
 * 
 */
public class AutoExpandListView extends ListView {

	public AutoExpandListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AutoExpandListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AutoExpandListView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int heightMeasure = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, heightMeasure);
	}

}
