package com.quanliren.quan_two.custom.emoji;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.widget.EditText;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.application.AppClass;
import com.quanliren.quan_two.util.DrawableCache;
import com.quanliren.quan_two.util.ImageUtil;
import com.quanliren.quan_two.util.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.droidsonroids.gif.GifDrawable;

public class EmoticonsEditText extends EditText {

    public EmoticonsEditText(Context context) {
        super(context);
        addTextChangedListener(tw);
    }

    public EmoticonsEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addTextChangedListener(tw);
    }

    public int max_nickname_length = 6;

    public EmoticonsEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.NickNameEditText);
        if (a != null) {
            max_nickname_length = a.getInt(R.styleable.NickNameEditText_maxlen,
                    6);
            a.recycle();
        }
        addTextChangedListener(tw);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
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

    private CharSequence replace(CharSequence text) {
        try {
            SpannableStringBuilder builder = new SpannableStringBuilder(text);
            Pattern pattern = buildPattern();
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                if (AppClass.mEmoticonsId.containsKey(matcher.group())) {
                    int id = AppClass.mEmoticonsId.get(matcher.group());
                    // Bitmap bitmap = ImageLoader.getInstance().loadImageSync(
                    // "drawable://" + id);
                    GifDrawable bitmap = DrawableCache
                            .getInstance().getDrawable(Long.valueOf(id), getContext());
                    if (bitmap != null) {
                        bitmap.setBounds(8, 0, ImageUtil.dip2px(getContext(), 30),
                                ImageUtil.dip2px(getContext(), 30));
                        ImageSpan span = new ImageSpan(bitmap);
                        builder.setSpan(span, matcher.start(), matcher.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                if (AppClass.mEmoticons2Id.containsKey(matcher.group())) {
                    int id = AppClass.mEmoticons2Id.get(matcher.group());
                    // Bitmap bitmap = ImageLoader.getInstance().loadImageSync(
                    // "drawable://" + id);
                    GifDrawable bitmap = DrawableCache
                            .getInstance().getDrawable(Long.valueOf(id), getContext());
                    if (bitmap != null) {
                        bitmap.setBounds(8, 0, ImageUtil.dip2px(getContext(), 30),
                                ImageUtil.dip2px(getContext(), 30));
                        ImageSpan span = new ImageSpan(bitmap);
                        builder.setSpan(span, matcher.start(), matcher.end(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
            return builder.toString();
        } catch (Exception e) {
            return text;
        }
    }

    TextWatcher tw = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void afterTextChanged(Editable s) {

            int selectionStart = getSelectionStart();
            int selectionEnd = getSelectionEnd();

            int num = (int) (Util.getLengthString(getText().toString()) / 2);
            if (Util.getLengthString(getText().toString()) % 2 > 0) {
                num++;
            }
            int ss = max_nickname_length - num;
            if (ss >= 0) {
            } else {
                boolean b = false;
                String str = s.toString();
                while (!b) {
                    str = str.substring(0, str.length() - 1);
                    int nums = (int) (Util.getLengthString(str) / 2);
                    if (Util.getLengthString(str) % 2 > 0) {
                        nums++;
                    }
                    int sss = max_nickname_length - nums;
                    if (sss >= 0) {
                        b = true;
                    }
                }
                setText(str);
                setSelection(str.length());
            }
        }
    };
}
