package com.wesgue.signinexample.Custom.Font;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.wesgue.signinexample.Custom.Font.FontCache;
import com.wesgue.signinexample.R;

/**
 * Created by Wesley Gue on 10/11/2016.
 *
 *  Used to display imaged using custom fonts
 */

public class FontTextView extends TextView {


    public FontTextView(Context context) {
        super(context);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomFont);
        String customFont = a.getString(R.styleable.CustomFont_font);
        setCustomFont(ctx, customFont);
        a.recycle();
    }

    public boolean setCustomFont(Context ctx, String asset) {
        Typeface tf = null;
        try {
            tf = FontCache.get("fonts/" + asset, ctx);
        } catch (Exception e) {
            return false;
        }
        setTypeface(tf);
        return true;
    }
}