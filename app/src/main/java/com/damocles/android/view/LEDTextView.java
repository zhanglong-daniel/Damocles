package com.damocles.android.view;

import java.io.File;


import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by zhanglong02 on 16/2/29.
 */
public class LEDTextView extends TextView {

    private static final String FONTS_FOLDER = "fonts";
    private static final String FONT_DIGITAL_7 = FONTS_FOLDER
            + File.separator + "digital-7.ttf";

    public LEDTextView(Context context) {
        super(context);
        init(context);
    }

    public LEDTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LEDTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        AssetManager assets = context.getAssets();
        final Typeface font = Typeface.createFromAsset(assets, FONT_DIGITAL_7);
        setTypeface(font);
    }

}
