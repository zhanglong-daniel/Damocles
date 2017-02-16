/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.damocles.common.widget;

/**
 * 波形动画
 * Created by zhanglong02 on 16/11/1.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class WaveformView extends View {
    private static final float MIN_AMPLITUDE = 0.0575f;
    private float mAmplitude = MIN_AMPLITUDE * 15;
    private int mDensity = 2;
    private int mWaveCount = 4;
    private float mFrequency = 0.1875f;
    private float mPhaseShift = -0.1875f;
    private float mPhase = mPhaseShift;

    private Paint mPrimaryPaint;
    private Paint mSecondaryPaint;

    private Path mPath;

    private float mLastX;
    private float mLastY;

    public WaveformView(Context context) {
        this(context, null);
    }

    public WaveformView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveformView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    private void initialize(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        mPrimaryPaint = new Paint();
        mPrimaryPaint.setStrokeWidth(density * 3.0f);
        mPrimaryPaint.setAntiAlias(true);
        mPrimaryPaint.setStyle(Paint.Style.STROKE);
        mPrimaryPaint.setColor(Color.parseColor("#ff2de87c"));

        mSecondaryPaint = new Paint();
        mSecondaryPaint.setStrokeWidth(density * 2.0f);
        mSecondaryPaint.setAntiAlias(true);
        mSecondaryPaint.setStyle(Paint.Style.STROKE);

        mPath = new Path();
    }

    public void updateAmplitude(float amplitude) {
        mAmplitude = Math.max(amplitude, MIN_AMPLITUDE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        for (int l = 0; l < mWaveCount; ++l) {
            float midH = height / 2.0f;
            float midW = width / 2.0f;

            float maxAmplitude = midH / 2f - 4.0f;
            float progress = 1.0f - l * 1.0f / mWaveCount;
            float normalAmplitude = (1.5f * progress - 0.5f) * mAmplitude;

            mPath.reset();
            for (int x = 0; x < width + mDensity; x += mDensity) {
                float scaling = 1f - (float) Math.pow(1 / midW * (x - midW), 2);
                float y = scaling * maxAmplitude * normalAmplitude * (float) Math.sin(
                        180 * x * mFrequency / (width * Math.PI) + mPhase) + midH;
                if (x == 0) {
                    mPath.moveTo(x, y);
                } else {
                    mPath.lineTo(x, y);
                }

                mLastX = x;
                mLastY = y;
            }
            if (l > 1) {
                mSecondaryPaint.setColor(Color.parseColor("#ffea00"));
            } else {
                mSecondaryPaint.setColor(Color.parseColor("#ff2de87c"));
            }
            if (l % 2 == 0) {
                mSecondaryPaint.setAlpha(255);
            } else {
                mSecondaryPaint.setAlpha(192);
            }
            if (l == 0) {
                canvas.drawPath(mPath, mPrimaryPaint);
            } else {
                canvas.drawPath(mPath, mSecondaryPaint);
            }
        }

        mPhase += mPhaseShift;
        invalidate();
    }
}
