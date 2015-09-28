package com.quanliren.quan_two.custom.emoji;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.application.AppClass;
import com.quanliren.quan_two.custom.HandyTextView;
import com.quanliren.quan_two.util.DrawableCache;
import com.quanliren.quan_two.util.ImageUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.droidsonroids.gif.GifDrawable;

public class EmoticonsTextView extends HandyTextView {

    public EmoticonsTextView(Context context) {
        super(context);
    }

    public EmoticonsTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    int imgSize = 0;

    public EmoticonsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        imgSize = ImageUtil.dip2px(context, 30);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.NickNameEditText);
        if (a != null) {
            imgSize = a.getDimensionPixelSize(R.styleable.NickNameEditText_imgSize,
                    imgSize);
            a.recycle();
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        num = 0;
        if (!TextUtils.isEmpty(text)) {
            super.setText(replace(text), type);
        } else {
            super.setText(text, type);
        }
    }

    private Pattern buildPattern() {
        StringBuilder patternString = new StringBuilder(
                AppClass.mEmoticons.size() * 3);
        patternString.append('(');
        for (int i = 0; i < AppClass.mEmoticons.size(); i++) {
            String s = AppClass.mEmoticons.get(i);
            patternString.append(Pattern.quote(s));
            patternString.append('|');
        }
        StringBuilder pattern2String = new StringBuilder(
                AppClass.mEmoticons2.size() * 3);
        for (int i = 0; i < AppClass.mEmoticons2.size(); i++) {
            String s = AppClass.mEmoticons2.get(i);
            pattern2String.append(Pattern.quote(s));
            pattern2String.append('|');
        }
        patternString.append(pattern2String);
        patternString.replace(patternString.length() - 1,
                patternString.length(), ")");
        return Pattern.compile(patternString.toString());
    }

    private int num = 0;

    public static Map<String, Integer> mEmoticonsId = new HashMap<String, Integer>();
    public static Map<String, Integer> mEmoticons2Id = new HashMap<String, Integer>();

    private CharSequence replace(CharSequence text) {
        try {
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            Pattern pattern = buildPattern();
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                if (AppClass.mEmoticonsId.containsKey(matcher.group())) {
                    int id = AppClass.mEmoticonsId.get(matcher.group());

                    GifDrawable bitmap = (GifDrawable) DrawableCache.getInstance().getDrawable(Long.valueOf(id), getContext());
//					Bitmap bitmap = ImageLoader.getInstance().loadImageSync("drawable://" + id);
                    if (bitmap != null) {
                        num++;
                        bitmap.setBounds(8, 0, imgSize,
                                imgSize);
                        ImageSpan span = new ImageSpan(bitmap, ImageSpan.ALIGN_BOTTOM);
                        builder.setSpan(span, matcher.start(), matcher.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                if (AppClass.mEmoticons2Id.containsKey(matcher.group())) {
                    int id = AppClass.mEmoticons2Id.get(matcher.group());

                    GifDrawable bitmap = (GifDrawable) DrawableCache.getInstance().getDrawable(Long.valueOf(id), getContext());
//					Bitmap bitmap = ImageLoader.getInstance().loadImageSync("drawable://" + id);
                    if (bitmap != null) {
                        num++;
                        bitmap.setBounds(2, 0, imgSize,
                                imgSize);
                        ImageSpan span = new ImageSpan(bitmap, ImageSpan.ALIGN_BOTTOM);
                        builder.setSpan(span, matcher.start(), matcher.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
            if (num<= 50) {
                b1=true;
                handler.post(refere);
            }else{
                b1=false;
            }
            return builder;
        } catch (Exception e) {
            return text;
        }
    }

    boolean b1 = true;
    int defaultTime = 150;
    Handler handler = new Handler(Looper.getMainLooper());

    Runnable refere = new Runnable() {

        @Override
        public void run() {
            invalidate();
            if (b1)
                handler.postDelayed(this, defaultTime);
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        b1 = false;
    }
}
