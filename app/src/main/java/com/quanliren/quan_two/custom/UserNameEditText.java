package com.quanliren.quan_two.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.util.Util;

public class UserNameEditText extends EditText {
    public int max_nickname_length = 6;

    public UserNameEditText(Context context) {
        super(context);
        init(context);
    }

    public UserNameEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NickNameEditText);
        max_nickname_length = a.getInt(R.styleable.NickNameEditText_maxlen, 6);
        a.recycle();
        init(context);
    }

    public void init(Context context) {
        addTextChangedListener(tw);
    }

    TextWatcher tw = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
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
//                InputFilter inputFilter=new InputFilter();
//                if(inputFilter.filter(s)){
//                  s.delete(selectionStart - 2, selectionEnd);
//                }
            } else if(selectionStart > 0){
                s.delete(selectionStart - 1, selectionEnd);
                setText(s);
                setSelection(s.toString().length());
            }
        }
    };
}
