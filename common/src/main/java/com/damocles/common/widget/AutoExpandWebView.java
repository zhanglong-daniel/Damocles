package com.damocles.common.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * 自动展开webview（scollview嵌套时使用）
 * 
 * @author Daniel
 * 
 */
public class AutoExpandWebView extends WebView {

    public AutoExpandWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AutoExpandWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoExpandWebView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
