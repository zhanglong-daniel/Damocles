package com.damocles.android.view;

import com.damocles.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhanglong02 on 16/2/29.
 */
public class CascadeLayout extends ViewGroup {

    private int mHorizontalSpacing;
    private int mVerticalSpacing;

    public CascadeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CascadeLayout);
        int defaultHorizontalSpacing = getResources().getDimensionPixelSize(R.dimen.cascade_horizontal_spacing);
        mHorizontalSpacing =
                a.getDimensionPixelSize(R.styleable.CascadeLayout_horizontal_spacing, defaultHorizontalSpacing);
        int defaultVerticalSpacing = getResources().getDimensionPixelSize(R.dimen.cascade_vertical_spacing);
        mVerticalSpacing = a.getDimensionPixelSize(R.styleable.CascadeLayout_vertical_spacing, defaultVerticalSpacing);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 0;
        int height = getPaddingTop();
        final int count = getChildCount();
        int horizontalSpacing;
        int verticalSpacing;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            horizontalSpacing = lp.honrizontalSpacing > 0 ? lp.honrizontalSpacing : mHorizontalSpacing;
            verticalSpacing = lp.verticalSpacing > 0 ? lp.verticalSpacing : mVerticalSpacing;
            width = getPaddingLeft() + horizontalSpacing * i;
            lp.x = width;
            lp.y = height;
            width += child.getMeasuredWidth();
            height += verticalSpacing;
        }
        width += getPaddingRight();
        height += getChildAt(count - 1).getMeasuredHeight() + getPaddingBottom();
        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            child.layout(lp.x, lp.y, lp.x + child.getMeasuredWidth(), lp.y + child.getMeasuredHeight());
        }
    }

    // Override to allow type-checking of LayoutParams.
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof CascadeLayout.LayoutParams;
    }

    /**
     * Returns a set of layout parameters with a width of
     * {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT},
     * a height of {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT} and no spanning.
     */
    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {

        int x;
        int y;
        int honrizontalSpacing;
        int verticalSpacing;

        public LayoutParams(Context context, AttributeSet attrs) {
            super(context, attrs);
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CasCadeLayout_LayoutParams);
            honrizontalSpacing =
                    a.getDimensionPixelSize(R.styleable.CasCadeLayout_LayoutParams_layout_honrizontal_spacing, -1);
            verticalSpacing = a.getDimensionPixelSize(R.styleable.CasCadeLayout_LayoutParams_layout_vertical_spacing,
                    -1);
            a.recycle();
        }

        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

    }
}
