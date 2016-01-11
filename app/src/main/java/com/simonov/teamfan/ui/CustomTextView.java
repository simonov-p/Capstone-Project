package com.simonov.teamfan.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.simonov.teamfan.R;


public class CustomTextView extends TextView {


    public CustomTextView(Context context) {
        super(context);
        setCustomFont(context, "DINPro-Regular.otf");
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        String customFont = a.getString(R.styleable.CustomTextView_customFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public boolean setCustomFont(Context ctx, String asset) {
        //setTextColor(Color.WHITE);
        Typeface tf;
        try {
            if (asset != null) {
                tf = Typeface.createFromAsset(ctx.getAssets(), asset);
            } else {
                tf = Typeface.createFromAsset(ctx.getAssets(), "DINPro-Regular.otf");
            }
        } catch (Exception e) {
            return false;
        }
        setTypeface(tf);
        return true;
    }

}
