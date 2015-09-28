package com.quanliren.quan_two.custom.emoji;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.emoticon.EmoticonActivityListBean;
import com.quanliren.quan_two.util.Util;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.Bind;

@EViewGroup(R.layout.chat_bottom)
public class XhsEmoticonsKeyBoardBar extends AutoHeightLayout implements View.OnFocusChangeListener, TextView.OnEditorActionListener, EmoteGridView.EmoticonListener {

    public static int FUNC_CHILLDVIEW_EMOTICON = 0;
    public static int FUNC_CHILLDVIEW_APPS = 1;

    @ViewById
    ImageView chatVoiceBtn;
    @ViewById
    TextView chatRadioBtn;
    @ViewById
    EmoticonsEditText et_chat;
    @ViewById
    ImageView chatFaceBtn;
    @ViewById
    LinearLayout editLl;
    @ViewById
    ImageView chatAddBtn;
    @ViewById
    Button sendBtn;
    @FragmentById
    EmoteView chatEivInputview;
    @ViewById
    RelativeLayout chatLayoutEmote;
    @ViewById
    RelativeLayout moreGridviewRl;
    @ViewById
    GridView moreGridview;

    private AtomicBoolean alreadyInflated = new AtomicBoolean(false);

    public XhsEmoticonsKeyBoardBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        if (!alreadyInflated.get()) {
            alreadyInflated.compareAndSet(false, true);


            setAutoHeightLayoutView(chatLayoutEmote);

            et_chat.setOnFocusChangeListener(this);

            et_chat.setOnEditorActionListener(this);

            et_chat.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    emojiShow.compareAndSet(true, false);
                }
            });

            et_chat.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String str = s.toString();
                    if (TextUtils.isEmpty(str)) {
                        chatAddBtn.setVisibility(VISIBLE);
                        sendBtn.setVisibility(GONE);
                    } else {
                        chatAddBtn.setVisibility(GONE);
                        sendBtn.setVisibility(VISIBLE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            et_chat.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (!et_chat.isFocused()) {
                        et_chat.setFocusable(true);
                        et_chat.setFocusableInTouchMode(true);
                    }
                    return false;
                }
            });
            et_chat.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {
                        setEditableState(true);
                    } else {
                        setEditableState(false);
                    }
                }
            });

            chatEivInputview.setEditText(et_chat);

            chatEivInputview.setListener(this);

            chatEivInputview.addMoreEmote();

            editLl.setSelected(true);

            MoreAdapter adapter = new MoreAdapter(mContext);
            List<MoreBean> list = new ArrayList<>();
            list.add(new MoreBean("真心话", R.drawable.chat_panel_zhenxinhua_selector));
            list.add(new MoreBean("大冒险", R.drawable.chat_panel_damaoxian_selector));
            list.add(new MoreBean("相册", R.drawable.chat_panel_picture_selector));
            list.add(new MoreBean("拍照", R.drawable.chat_panel_camera_selector));
            list.add(new MoreBean("私密真心话", R.drawable.chat_panel_simi_selector));
            list.add(new MoreBean("小视频", R.drawable.chat_panel_radio_selector));
            adapter.setList(list);
            moreGridview.setAdapter(adapter);

            moreGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mKeyBoardBarViewListener != null) {
                        mKeyBoardBarViewListener.OnMoreItemClick(position);
                    }
                    hideAutoView();
                }
            });
        }
    }

    private void setEditableState(boolean b) {
        if (b) {
            et_chat.setFocusable(true);
            et_chat.setFocusableInTouchMode(true);
            et_chat.requestFocus();
        } else {
            et_chat.setFocusable(false);
            et_chat.setFocusableInTouchMode(false);
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                if (chatLayoutEmote != null && chatLayoutEmote.isShown() && emojiShow.get()) {
                    hideAutoView();
                    return true;
                } else {
                    return super.dispatchKeyEvent(event);
                }
        }
        return super.dispatchKeyEvent(event);
    }

    @Click({R.id.chat_face_btn, R.id.send_btn, R.id.chat_voice_btn, R.id.chat_add_btn})
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.chat_face_btn) {
            switch (mKeyboardState) {
                case KEYBOARD_STATE_NONE:
                case KEYBOARD_STATE_BOTH:
                    moreGridviewRl.setVisibility(GONE);
                    showAutoView();
                    Util.closeSoftKeyboard(mContext);
                    break;
                case KEYBOARD_STATE_FUNC:
                    if (moreGridviewRl.getVisibility() == VISIBLE) {
                        moreGridviewRl.setVisibility(GONE);
                        break;
                    }
                    emotionClose();
                    editRequestFocus();
                    Util.openSoftKeyboard(mContext, et_chat);
                    break;
            }
        } else if (id == R.id.send_btn) {
            if (isFastDoubleClick()) {
                Util.toast(mContext, "太快了，休息一下~");
                return;
            }
            String t = et_chat.getText().toString().trim();
            if (!TextUtils.isEmpty(t)) {
                if (mKeyBoardBarViewListener != null) {
                    mKeyBoardBarViewListener.OnSendBtnClick(t);
                }
            } else {
                Util.toast(mContext, "请输入内容");
            }
            sendBtnClick();
        } else if (id == R.id.chat_voice_btn) {
            voiceBtnClick();
        } else if (id == R.id.chat_add_btn) {
            switch (mKeyboardState) {
                case KEYBOARD_STATE_NONE:
                case KEYBOARD_STATE_BOTH:
                    moreGridviewRl.setVisibility(VISIBLE);
                    showAutoView();
                    Util.closeSoftKeyboard(mContext);
                    break;
                case KEYBOARD_STATE_FUNC:
                    if (moreGridviewRl.getVisibility() == GONE) {
                        moreGridviewRl.setVisibility(VISIBLE);
                        break;
                    }
                    emotionClose();
                    editRequestFocus();
                    if (chatRadioBtn.getVisibility() == GONE) {
                        Util.openSoftKeyboard(mContext, et_chat);
                    } else {
                        hideAutoView();
                    }
                    break;
            }
        }
    }


    public void sendBtnClick() {
        sendBtn.setVisibility(GONE);
        et_chat.setText("");
        chatAddBtn.setVisibility(VISIBLE);
    }

    public void voiceBtnClick() {
        if (chatRadioBtn.getVisibility() == VISIBLE) {
            chatRadioBtn.setVisibility(View.GONE);
            editLl.setVisibility(View.VISIBLE);
            if (et_chat.getText().toString().length() > 0) {
                sendBtn.setVisibility(View.VISIBLE);
                chatAddBtn.setVisibility(GONE);
            } else {
                sendBtn.setVisibility(View.GONE);
                chatAddBtn.setVisibility(VISIBLE);
            }
            chatVoiceBtn.setImageResource(R.drawable.chat_voice_btn);
            editRequestFocus();
            Util.openSoftKeyboard(mContext, et_chat);
        } else {
            Util.closeSoftKeyboard(mContext);
            hideAutoView();
            chatRadioBtn.setVisibility(View.VISIBLE);
            editLl.setVisibility(View.GONE);
            sendBtn.setVisibility(View.GONE);
            chatAddBtn.setVisibility(View.VISIBLE);
            chatVoiceBtn.setImageResource(R.drawable.chat_borad_btn);
        }
    }

    public void editRequestFocus() {
        et_chat.setFocusable(true);
        et_chat.setFocusableInTouchMode(true);
        et_chat.requestFocus();
        editLl.setSelected(true);
    }


    @Override
    public void OnSoftPop(final int height) {
        super.OnSoftPop(height);
        post(new Runnable() {
            @Override
            public void run() {
                if (mKeyBoardBarViewListener != null) {
                    mKeyBoardBarViewListener.OnKeyBoardStateChange(mKeyboardState, height);
                }
            }
        });
    }



    @Override
    public void OnSoftClose(int height) {
        super.OnSoftClose(height);
        if (mKeyBoardBarViewListener != null) {
            mKeyBoardBarViewListener.OnKeyBoardStateChange(mKeyboardState, height);
        }
        if (!emojiShow.get()) {
            setAutoViewHeight(0);
            mKeyboardState = KEYBOARD_STATE_NONE;
        }
    }

    @Override
    public void OnSoftChanegHeight(int height) {
        super.OnSoftChanegHeight(height);
        if (mKeyBoardBarViewListener != null) {
            mKeyBoardBarViewListener.OnKeyBoardStateChange(mKeyboardState, height);
        }
    }

    KeyBoardBarViewListener mKeyBoardBarViewListener;

    public void setOnKeyBoardBarViewListener(KeyBoardBarViewListener l) {
        this.mKeyBoardBarViewListener = l;
    }

    public interface KeyBoardBarViewListener {

        public void OnKeyBoardStateChange(int state, int height);

        public void OnSendBtnClick(String msg);

        public void OnSendEmotion(EmoticonActivityListBean.EmoticonZip.EmoticonImageBean bean);

        public void OnMoreItemClick(int position);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            sendBtn.performClick();
            return true;
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            chatLayoutEmote.setVisibility(View.GONE);
        }
    }

    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    @Override
    public void onEmoticonClick(EmoticonActivityListBean.EmoticonZip.EmoticonImageBean bean) {
        if (isFastDoubleClick()) {
            Util.toast(mContext, "太快了，休息一下~");
            return;
        }
        if (mKeyBoardBarViewListener != null) {
            mKeyBoardBarViewListener.OnSendEmotion(bean);
        }
    }

    @Override
    public void onEmoticonLongPress(EmoticonActivityListBean.EmoticonZip.EmoticonImageBean bean, int[] xy, int[] wh) {

    }

    @Override
    public void onEmoticonLongPressCancle() {

    }

    class MoreAdapter extends BaseAdapter<MoreBean> {

        public MoreAdapter(Context context) {
            super(context);
        }

        @Override
        public BaseHolder getHolder(View view) {
            return new ViewHolder(view);
        }

        @Override
        public int getConvertView(int position) {
            return R.layout.chat_bottom_grid_item;
        }

        class ViewHolder extends BaseHolder {

            @Bind(R.id.icon)
            ImageView icon;
            @Bind(R.id.name)
            TextView name;

            public ViewHolder(View view) {
                super(view);
            }

            @Override
            public void bind(MoreBean bean, int position) {
                icon.setImageResource(bean.icon);
                name.setText(bean.name);
            }
        }
    }

    public static class MoreBean {

        public String name;
        public int icon;

        public MoreBean(String name, int icon) {
            this.name = name;
            this.icon = icon;
        }
    }
}
