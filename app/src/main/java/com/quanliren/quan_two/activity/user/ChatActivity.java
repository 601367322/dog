package com.quanliren.quan_two.activity.user;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.a.me.maxwin.view.XXListView.IXListViewListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.quan.lib_camera_video.MediaRecorderActivity;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.group.DongTaiDetailActivity_;
import com.quanliren.quan_two.activity.group.date.DateDetailActivity_;
import com.quanliren.quan_two.activity.image.ImageBeanList;
import com.quanliren.quan_two.activity.image.ImageBrowserActivity_;
import com.quanliren.quan_two.activity.redline.RedLineBean;
import com.quanliren.quan_two.activity.redline.RedLineDetailActivity_;
import com.quanliren.quan_two.adapter.MessageAdapter;
import com.quanliren.quan_two.application.AM;
import com.quanliren.quan_two.bean.ChatListBean;
import com.quanliren.quan_two.bean.DateBean;
import com.quanliren.quan_two.bean.DfMessage;
import com.quanliren.quan_two.bean.DongTaiBean;
import com.quanliren.quan_two.bean.ImageBean;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.bean.UserTable;
import com.quanliren.quan_two.bean.ZhenXinHuaTable;
import com.quanliren.quan_two.bean.emoticon.EmoticonActivityListBean;
import com.quanliren.quan_two.bean.emoticon.EmoticonActivityListBean.EmoticonZip.EmoticonJsonBean;
import com.quanliren.quan_two.custom.emoji.XhsEmoticonsKeyBoardBar;
import com.quanliren.quan_two.db.ChatListDao;
import com.quanliren.quan_two.db.MessageDao;
import com.quanliren.quan_two.db.ZhenXinHuaDao;
import com.quanliren.quan_two.fragment.custom.AddPicFragment;
import com.quanliren.quan_two.fragment.message.MyLeaveMessageFragment;
import com.quanliren.quan_two.pull.swipe.SwipeRefreshLayout;
import com.quanliren.quan_two.radio.AmrEncodSender;
import com.quanliren.quan_two.radio.AmrEngine;
import com.quanliren.quan_two.radio.MicRealTimeListener;
import com.quanliren.quan_two.service.SocketManage;
import com.quanliren.quan_two.util.BroadcastUtil;
import com.quanliren.quan_two.util.ImageUtil;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;
import com.quanliren.quan_two.util.http.MyJsonHttpResponseHandler;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.api.SdkVersionHelper;
import org.json.JSONObject;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
import us.pinguo.edit.sdk.PGEditActivity;
import us.pinguo.edit.sdk.base.PGEditResult;
import us.pinguo.edit.sdk.base.PGEditSDK;

@EActivity(R.layout.chat)
@OptionsMenu(R.menu.chat_menu)
public class ChatActivity extends BaseActivity implements IXListViewListener,
        OnTouchListener, XhsEmoticonsKeyBoardBar.KeyBoardBarViewListener
        , SensorEventListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "ChatActivity";

    /**
     * 动态添加消息广播
     */
    public static final String ADDMSG = "com.quanliren.quan_two.ChatActivity.ADDMSG";

    /**
     * 改变发送状态广播
     */
    public static final String CHANGESEND = "com.quanliren.quan_two.ChatActivity.CHANGESEND";

    public static final int HANDLER_COPY = 3, HANDLER_LONGPRESS = 4, HANDLER_RESEND = 6, HANDLER_ZHENXINHUA = 9, HANDLER_DAMAOXIAN = 10;
    /**
     * 父外层
     */
    @ViewById
    XhsEmoticonsKeyBoardBar chat_parent;
    /**
     * 语音按钮，录音布局，录音完成等待
     */
    @ViewById
    View chat_radio_panel, loading;
    @ViewById
    TextView chat_radio_btn;
    /**
     * 消息列表
     */
    @ViewById(R.id.listview)
    ListView listview;

    /**
     * 下拉加载更多
     */
    @ViewById(R.id.swipe)
    SwipeRefreshLayout swipe;

    /**
     * 录音音量大小，录音垃圾图标
     */
    @ViewById
    ImageView voicesize, delete;

    /**
     * 消息适配器
     */
    MessageAdapter adapter;
    /**
     * 底部
     */
    @ViewById
    View layout_bottom;
    /**
     * 聊天更多
     */
    @ViewById
    View chat_add_btn;
    /**
     * 好友
     */
    @Extra
    User friend;

    /**
     * 本用户
     */
    User user;

    /**
     * 根据最后一个ID下拉刷新获取数据
     */
    String maxid = "0";

    /**
     * 文件临时路径
     */
    String filename;

    @SystemService
    NotificationManager nm;

    /**
     * 外放监听
     */
    AudioManager audioManager;
    SensorManager mSensorManager;
    Sensor mSensor;

    /**
     * 消息DAO
     */
    public MessageDao messageDao;
    /**
     * 聊天列表DAO
     */
    public ChatListDao chatListDao;

    /**
     * 真心话dao
     */
    public ZhenXinHuaDao zhenXinHuaDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageDao = MessageDao.getInstance(getApplicationContext());
        chatListDao = ChatListDao.getInstance(getApplicationContext());
        zhenXinHuaDao = ZhenXinHuaDao.getInstance(getApplicationContext());

        EventBus.getDefault().register(this);
    }

    /**
     * 初始化界面
     */
    @AfterViews
    void initView() {
        if ("99".equals(friend.getId())) {
            layout_bottom.setVisibility(View.GONE);
        } else {
            layout_bottom.setVisibility(View.VISIBLE);
            if("".equals(ac.cs.getIsFirstChat())){
                chat_add_btn.performClick();
                ac.cs.setIsFirstChat("1");
            }
        }
        user = getHelper().getUserInfo();
        if (user == null) {
            finish();
            return;
        }
        adapter = new MessageAdapter(this, new ArrayList());
        adapter.setFriend(friend);
        adapter.setHandler(itemHandler);

        listview.setAdapter(adapter);

        swipe.setOnRefreshListener(this);

        try {
            UserTable temp = userTableDao.queryForId(friend.getId());
            if (temp != null) {
                friend = temp.getUser();
            } else {
                ac.finalHttp.post(URL.GET_USER_INFO,
                        getAjaxParams("otherid", friend.getId()), callBack);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        getSupportActionBar().setTitle(friend.getNickname());

        chat_radio_btn.setOnTouchListener(this);

        listview.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View arg0, MotionEvent arg1) {
                closeInput();
                chat_parent.hideAutoView();
                return false;
            }
        });


        audioManager = (AudioManager) this
                .getSystemService(Context.AUDIO_SERVICE);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (user != null) {
            Util.setAlarmTime(this, System.currentTimeMillis(),
                    BroadcastUtil.ACTION_CHECKCONNECT,
                    BroadcastUtil.CHECKCONNECT);

            nm.cancel((friend.getId() + friend.getNickname()).hashCode());
        }

        chat_parent.setOnKeyBoardBarViewListener(this);

        refresh();
    }

    /**
     * 刷新数据
     */
    @UiThread(delay = 200l)
    public void refresh() {
        swipe.setRefreshing(true);
    }


    /**
     * 监听广播刷新
     *
     * @param i
     */
    @Receiver(actions = {ADDMSG, CHANGESEND})
    public void receiver(Intent i) {
        receiverUI(i);
    }

    @UiThread
    void receiverUI(Intent i) {
        try {
            String action = i.getAction();
            if (action.equals(ADDMSG)) {
                DfMessage bean = (DfMessage) i.getExtras().getSerializable("bean");

                if (bean.getSendUid().equals(friend.getId())) {
                    bean.setIsRead(1);
                    messageDao.update(bean);

                    Intent broad = new Intent(MyLeaveMessageFragment.REFEREMSGCOUNT);
                    broad.putExtra("id", friend.getId());
                    sendBroadcast(broad);

                    scrollToLast(bean);

                    switch (bean.getMsgtype()) {
                        case DfMessage.VOICE:
                            ChatDownLoadManager.getInstance(this).down(bean);
                            break;
                    }
                    cancelNm();
                }
            } else if (action.equals(CHANGESEND)) {
                DfMessage bean = (DfMessage) i.getExtras().getSerializable("bean");
                List<DfMessage> list = adapter.getList();
                for (DfMessage dfMessage : list) {
                    if (dfMessage.getId() == bean.getId()) {
                        dfMessage.setDownload(bean.getDownload());
                    }
                }
                if (i.getExtras().containsKey(SocketManage.TYPE)) {
                    int type = i.getIntExtra(SocketManage.TYPE, 0);
                    switch (type) {
                        case SocketManage.ERROR_TYPE_MORE:
                            showMoreMsg();
                            break;
                    }

                }
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMoreMsg() {
        String info = "您今天已经向20位陌生人打招呼了，如果想继续与其他陌生人打招呼，请立刻成为会员吧~";
        Util.goVip(this, info);
    }

    @UiThread(delay = 1000)
    public void cancelNm() {
        nm.cancel((friend.getId() + friend.getNickname()).hashCode());
    }

    JsonHttpResponseHandler callBack = new JsonHttpResponseHandler() {

        public void onSuccess(JSONObject jo) {
            try {
                int status = jo.getInt(URL.STATUS);
                switch (status) {
                    case 0:
                        User temp = new Gson().fromJson(jo.getString(URL.RESPONSE),
                                User.class);
                        if (temp != null) {
                            friend = temp;
                            UserTable dbUser = new UserTable(temp);
                            userTableDao.deleteById(dbUser.getId());
                            userTableDao.create(dbUser);
                            adapter.setFriend(friend);
                            adapter.notifyDataSetChanged();
                            getSupportActionBar().setTitle(friend.getNickname());
                        }
                        break;
                    default:
                        showFailInfo(jo);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;
    };

    @OptionsItem
    void ziliao() {
        if (friend == null) {
            return;
        }
        AM.getActivityManager().popActivity(UserOtherInfoActivity_.class.getName());
        UserOtherInfoActivity_.intent(this).userId(friend.getId()).start();
    }

    @Override
    public void onRefresh() {
        if (friend == null) {
            return;
        }
        listview.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
        getMsgListInit();
    }

    @Background
    public void getMsgListInit() {
        try {
            //获取上面一条消息id，防止重复加载
            int maxid = -1;
            if (adapter.getList().size() > 0) {
                maxid = adapter.getItem(0).getId();
            }
            //获取最近的15条消息
            final List<DfMessage> list = messageDao.getMsgList(user.getId(), friend.getId(), maxid);
            //下载队列
            final List<DfMessage> downlist = new ArrayList<>();
            //未读消息队列
            List<Integer> ids = new ArrayList<Integer>();

            for (DfMessage dfMessage : list) {
                //将未读变为已读
                if (dfMessage.getIsRead() == 0) {
                    ids.add(dfMessage.getId());
                    dfMessage.setIsRead(1);
                }
                //如果是语音消息，并且没有被下载，添加到下载队列
                if (dfMessage.getMsgtype() == DfMessage.VOICE
                        && (dfMessage.getDownload() == SocketManage.D_nodownload)) {
                    downlist.add(dfMessage);
                }
            }

            //更新未读消息变为已读
            if (ids.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (Integer integer : ids) {
                    sb.append(integer + ",");
                }
                sb.deleteCharAt(sb.length() - 1);
                messageDao.updateMsgReaded(sb.toString());
                updateSecondTitle();
            }
            //设置消息是否显示时间，如果两条消息间隔1分钟
            if (list.size() > 0) {
                for (int i = list.size() - 1; i >= 0; i--) {
                    if (i < list.size() - 1 && i > 0) {
                        if (Util.fmtDateTime.parse(
                                list.get(i).getCtime()).getTime() - 60 * 1000 > Util.fmtDateTime
                                .parse(list.get(i - 1).getCtime())
                                .getTime()) {
                            list.get(i).setShowTime(true);
                        }
                    } else {
                        list.get(i).setShowTime(true);
                    }
                }
            }
            //发送广播，更新未读消息数量
            Intent broad = new Intent(
                    MyLeaveMessageFragment.REFEREMSGCOUNT);
            broad.putExtra("id", friend.getId());
            sendBroadcast(broad);

            getMsgListInitUI(list, downlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新列表
     *
     * @param list
     * @param downlist
     */
    @UiThread
    public void getMsgListInitUI(List<DfMessage> list, List<DfMessage> downlist) {
        for (DfMessage dfMessage : list) {
            adapter.add(0, dfMessage);
        }

        adapter.notifyDataSetChanged();
        swipe.setRefreshing(false);

        if (list.size() > 0) {
            listview.setSelection(list.size() - 1);
        }
        //下载
        for (DfMessage dfMessage : downlist) {
            ChatDownLoadManager.getInstance(this).down(dfMessage);
        }
    }

    /**
     * 更新标题栏未读消息数量
     */
    public void updateSecondTitle() {
        try {
            int count = messageDao.getUnReadCount(user.getId(), friend.getId());
            if (count > 0) {
                setSubTitle("您还有" + count + "条未读消息，向下拖动即可阅读");
            } else {
                setSubTitle(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @UiThread
    public void setSubTitle(String str) {
        getSupportActionBar().setSubtitle(str);
    }

    @Override
    public void onLoadMore() {
    }

    @OnActivityResult(AddPicFragment.Album)
    public void onAlbumResult(Intent data) {
        if (data == null) {
            return;
        }
        ContentResolver resolver = getContentResolver();
        Uri imgUri = data.getData();
        try {
            Cursor cursor = resolver.query(imgUri, null, null, null, null);
            cursor.moveToFirst();
            filename = cursor.getString(1);
            ImageUtil.downsize(
                    filename,
                    filename = StaticFactory.APKCardPathChatImg
                            + new Date().getTime(), this);
            //360
            PGEditSDK.instance().startEdit(this, PGEditActivity.class, filename, filename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnActivityResult(AddPicFragment.Camera)
    public void onCameraResult(Intent data) {
        if (filename != null) {
            File fi = new File(filename);
            if (fi != null && fi.exists()) {
                ImageUtil.downsize(filename, filename, this);
                //360
                PGEditSDK.instance().startEdit(this, PGEditActivity.class, filename, filename);
            }
            fi = null;
        }
    }

    /**
     * 360回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PGEditSDK.PG_EDIT_SDK_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            PGEditResult editResult = PGEditSDK.instance().handleEditResult(data);
            String resultPhotoPath = editResult.getReturnPhotoPath();
            ImageUtil.downsize(filename, filename, this);
            sendFile(new File(resultPhotoPath), null, DfMessage.IMAGE);
        }
        if (requestCode == PGEditSDK.PG_EDIT_SDK_REQUEST_CODE
                && resultCode == PGEditSDK.PG_EDIT_SDK_RESULT_CODE_CANCEL) {
            File file = new File(filename);
            if (file.exists()) {
                file.delete();
            }
        }

        if (requestCode == PGEditSDK.PG_EDIT_SDK_REQUEST_CODE
                && resultCode == PGEditSDK.PG_EDIT_SDK_RESULT_CODE_NOT_CHANGED) {
            sendFile(new File(filename), null, DfMessage.IMAGE);
        }
    }

    @OnActivityResult(value = AddPicFragment.RADIO)
    public void onRadioCallBack(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            try {
                File video = new File(data.getStringExtra("file"));
                File thumb_ = new File(data.getStringExtra("thumb"));
                if (video.exists() && thumb_.exists()) {
                    sendFile(video, thumb_, DfMessage.VIDEO);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 语音播放event
     *
     * @param msg
     */
    public void onEvent(DfMessage msg) {
        if (adapter != null) {
            List<DfMessage> msgs = adapter.getList();
            for (int i = 0; i < msgs.size(); i++) {
                if (msgs.get(i).getMsgid().equals(msg.getMsgid())) {
                    msgs.get(i).setPlaying(msg.isPlaying());
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * 发送文件 图片语音视频
     *
     * @param file    文件
     * @param file1   视频缩略图，没有传null
     * @param msgtype 消息类型
     */
    public void sendFile(File file, File file1, int msgtype) {

        try {

            String defaultUrl = URL.SENDFILE;

            JSONObject msg = null;
            switch (msgtype) {
                case DfMessage.VIDEO:
                    msg = DfMessage.getMessage(user, file.getPath() + "," + file1.getPath(),
                            friend, msgtype, (int) recodeTime);
                    defaultUrl = URL.SENDVIDEO;
                    break;
                default:
                    msg = DfMessage.getMessage(user, file.getPath(),
                            friend, msgtype, (int) recodeTime);
                    break;
            }
            JSONObject jo = new JSONObject();
            jo.put(SocketManage.ORDER, SocketManage.ORDER_SENDMESSAGE);
            jo.put(SocketManage.SEND_USER_ID, user.getId());
            jo.put(SocketManage.RECEIVER_USER_ID, friend.getId());
            jo.put(SocketManage.MESSAGE, msg);
            jo.put(SocketManage.MESSAGE_ID,
                    msg.getString(SocketManage.MESSAGE_ID));

            RequestParams ap = getAjaxParams();
            ap.put("file", file);
            if (file1 != null) {
                ap.put("file1", file1);
            }
            ap.put("msgattr", jo.toString());

            ac.finalHttp.post(
                    defaultUrl,
                    ap,
                    new sendfileCallBack((DfMessage) new Gson().fromJson(
                            msg.toString(), new TypeToken<DfMessage>() {
                            }.getType())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class sendfileCallBack extends JsonHttpResponseHandler {
        DfMessage msg;

        public sendfileCallBack(DfMessage msg) {
            this.msg = msg;
        }

        @Override
        public void onStart() {
            try {
                msg.setDownload(SocketManage.D_downloading);
                ChatListBean cb = new ChatListBean(user, msg, friend);
                messageDao.saveMessage(msg, cb);

                Intent broad = new Intent(MyLeaveMessageFragment.ADDMSG);
                broad.putExtra("bean", cb);
                sendBroadcast(broad);

                scrollToLast(msg);

                chat_radio_btn.setEnabled(true);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        public void onSuccess(JSONObject jo) {
            try {
                int status = jo.getInt(URL.STATUS);
                switch (status) {
                    case 0:
                        if ("".equals(jo.getJSONObject(URL.RESPONSE).getString(
                                URL.NETURL))) {
                            msg.setDownload(SocketManage.D_destroy);
                            messageDao.update(msg);
                            adapter.notifyDataSetChanged();
                        } else {
                            msg.setDownload(SocketManage.D_downloaded);
                            messageDao.update(msg);
                            adapter.notifyDataSetChanged();
                        }
                        break;
                    case 3:
                        fail();
                        showMoreMsg();
                        break;
                    default:
                        fail();
                        showFailInfo(jo);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ;

        @Override
        public void onFailure() {
            fail();
        }

        public void fail() {
            try {
                msg.setDownload(SocketManage.D_destroy);
                messageDao.update(msg);
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    public void sendTextThread(String str) {

        try {
            DfMessage temp = messageDao.getLastMessage(user.getId(), friend.getId());
            if (temp != null) {
                /**
                 * 如果最近一条消息是动态推送，则第一条回复为回复动态
                 */
                if (temp.getMsgtype() == DfMessage.PIAOLIUPING) {
                    RequestParams ap = getAjaxParams();
                    ap.put("dyid", temp.getPiaoLiuBean().rtid + "");
                    ap.put("content", str);
                    ap.put("nickname", user.getNickname());
                    ac.finalHttp.post(URL.REPLY_DONGTAI, ap, new JsonHttpResponseHandler());
                }
            }
            //将message转成json
            JSONObject msg = DfMessage.getMessage(user, str, friend, DfMessage.TEXT,
                    (int) recodeTime);
            sendMsg(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendZhenXinHuaTextThread(String str, String msgId) {

        try {
            //将message转成json
            JSONObject msg = DfMessage.getMessage(user, str, friend, DfMessage.TEXT, msgId);
            sendMsg(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void senDaMaoXianTextThread(String str, String msgId) {

        try {
            //将message转成json
            JSONObject msg = DfMessage.getMessage(user, str, friend, DfMessage.DAMAOXIAN_RESULT, msgId);
            sendMsg(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendGifThread(EmoticonJsonBean bean) {
        try {
            Gson gson = new Gson();
            JSONObject msg = DfMessage.getMessage(user,
                    gson.toJson(bean), friend, DfMessage.FACE, (int) recodeTime);
            sendMsg(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(JSONObject msg) {
        try {
            final JSONObject jo = new JSONObject();
            jo.put(SocketManage.ORDER, SocketManage.ORDER_SENDMESSAGE);
            jo.put(SocketManage.SEND_USER_ID, user.getId());
            jo.put(SocketManage.RECEIVER_USER_ID, friend.getId());
            jo.put(SocketManage.MESSAGE, msg);
            jo.put(SocketManage.MESSAGE_ID,
                    msg.getString(SocketManage.MESSAGE_ID));

            //初始化录音时间
            recodeTime = 0.0f;

            //将msg保存到数据库
            final DfMessage msgs = new Gson().fromJson(msg.toString(),
                    new TypeToken<DfMessage>() {
                    }.getType());
            msgs.setDownload(SocketManage.D_downloading);

            if (msgs.getMsgtype() == DfMessage.DAMAOXIAN_RESULT) {
                msgs.setPlay_state(1);
            }

            ChatListBean cb = new ChatListBean(user, msgs, friend);
            messageDao.saveMessage(msgs, cb);

            //刷新聊天列表页
            Intent broad = new Intent(MyLeaveMessageFragment.ADDMSG);
            broad.putExtra("bean", cb);
            sendBroadcast(broad);

            //移动到最后一行
            scrollToLast(msgs);
            //初始化输入框
            //发送
            RequestParams params = Util.getAjaxParams(this);
            params.put("msgattr", jo.toString());
            ac.finalHttp.post(URL.SENDMSG, params, new MyJsonHttpResponseHandler(this) {
                @Override
                public void onSuccessRetCode(JSONObject jo) throws Throwable {
                    JSONObject response = new JSONObject(jo.getString(URL.RESPONSE));
                    if (response.has(SocketManage.ORDER)) {
                        String cmd = response.optString(SocketManage.ORDER);
                        if (cmd.equals(SocketManage.ORDER_SENDED)) {
                            try {
                                msgs.setDownload(SocketManage.D_downloaded);
                                messageDao.update(msgs);
                                Intent i = new Intent(ChatActivity.CHANGESEND);
                                i.putExtra("bean", msgs);
                                receiver(i);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (cmd.equals(SocketManage.ORDER_SENDERROR)) {
                            try {
                                int type = response.getInt(SocketManage.TYPE);
                                if (type == SocketManage.ERROR_TYPE_MORE) {
                                    msgs.setDownload(SocketManage.D_destroy);
                                } else if (type == SocketManage.ERROR_TYPE_BLACK) {
                                    msgs.setDownload(SocketManage.D_fail);
                                }
                                messageDao.update(msgs);
                                Intent i = new Intent(ChatActivity.CHANGESEND);
                                i.putExtra("bean", msgs);
                                i.putExtra("type", response.getInt(SocketManage.TYPE));
                                receiver(i);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onFailRetCode(JSONObject jo) {
                    super.onFailRetCode(jo);
                    onFailure();
                }

                @Override
                public void onFailure() {
                    try {
                        msgs.setDownload(SocketManage.D_destroy);
                        messageDao.update(msgs);
                        Intent i = new Intent(ChatActivity.CHANGESEND);
                        i.putExtra("bean", msgs);
                        receiver(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    Handler imgHandle = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (AmrEngine.getSingleEngine().isRecordRunning()) {
                        AmrEngine.getSingleEngine().stopRecording();
                        hideall();
                        voiceValue = 0.0;
                        chat_radio_btn.setText(R.string.normaltalk);
                        chat_radio_btn.setEnabled(false);
                        if (recodeTime < MIX_TIME) {
                            showCustomToast("太短了");
                            File o = new File(filename);
                            if (o.exists()) {
                                o.delete();
                            }
                            new Handler().postDelayed(new Runnable() {

                                public void run() {
                                    hideall();
                                    chat_radio_btn.setEnabled(true);
                                }
                            }, 1000);
                        } else {
                            sendFile(new File(filename), null, DfMessage.VOICE);
                        }
                    }
                    break;
                case 1:
                    setDialogImage();
                    break;
                default:
                    break;
            }

        }
    };


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!AmrEngine.getSingleEngine().isRecordRunning()) {

                    showVoiceLoading();

                    chat_radio_panel.setVisibility(View.VISIBLE);
                    chat_radio_btn.setText(R.string.pressedtalk);
                    chat_radio_btn.setSelected(true);
                    File file = new File(StaticFactory.APKCardPathChatVoice);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    AmrEncodSender sender = new AmrEncodSender(
                            filename = (StaticFactory.APKCardPathChatVoice + String.valueOf((String
                                    .valueOf(new Date().getTime()) + ".amr")
                                    .hashCode())), new MicRealTimeListener() {

                        @Override
                        public void getMicRealTimeSize(double size,
                                                       long time) {
                            voiceValue = size;
                        }
                    });

                    AmrEngine.getSingleEngine().startRecording();
                    new Thread(sender).start();
                    showVoiceStart();
                    mythread();
                }
                return true;
            case MotionEvent.ACTION_UP:
                if (AmrEngine.getSingleEngine().isRecordRunning()) {
                    AmrEngine.getSingleEngine().stopRecording();
                    chat_radio_panel.setVisibility(View.GONE);
                    chat_radio_btn.setText(R.string.normaltalk);
                    chat_radio_btn.setSelected(false);
                    voiceValue = 0.0;
                    chat_radio_btn.setEnabled(false);
                    if (recodeTime < MIX_TIME) {
                        showCustomToast("太短了");

                        new Handler().postDelayed(new Runnable() {

                            public void run() {
                                hideall();
                                chat_radio_btn.setEnabled(true);
                            }
                        }, 1000);
                    } else {
                        showVoiceLoading();
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                hideall();
                                if (isCanle) {
                                    File o = new File(filename);
                                    if (o.exists()) {
                                        o.delete();
                                    }
                                    chat_radio_btn.setEnabled(true);
                                } else {
                                    sendFile(new File(filename), null, DfMessage.VOICE);
                                }
                            }
                        }, 500);
                    }
                }
                return false;
            case MotionEvent.ACTION_MOVE:
                chat_radio_btn.getLocationOnScreen(location);
                chat_radio_panel.getLocationOnScreen(location1);
                if (event.getRawY() < location[1]) {
                    showVoiceCancle();
                    isCanle = true;
                    if (event.getRawY() <= location1[1]
                            + ImageUtil.dip2px(this, 150)
                            && event.getRawY() >= location1[1]
                            && event.getRawX() <= location1[0]
                            + ImageUtil.dip2px(this, 150)
                            && event.getRawX() >= location1[0]) {
                        delete.setSelected(true);
                    } else {
                        delete.setSelected(false);
                    }
                } else {
                    showVoiceStart();
                    isCanle = false;
                }
                return true;
        }
        return true;
    }

    int[] location = new int[2];
    int[] location1 = new int[2];
    boolean isCanle = false;
    private MediaPlayer mediaPlayer;
    // Button player;
    private Thread recordThread;

    private static int MAX_TIME = 60; // 最长录制时间，单位秒，0为无时间限制
    private static int MIX_TIME = 1; // 最短录制时间，单位秒，0为无时间限制，建议设为1

    private static float recodeTime = 0.0f; // 录音的时间
    private static double voiceValue = 0.0; // 麦克风获取的音量值

    private static boolean playState = false; // 播放状态

    // 录音计时线程
    public void mythread() {
        recordThread = new Thread(ImgThread);
        recordThread.start();
    }

    private Runnable ImgThread = new Runnable() {

        public void run() {
            recodeTime = 0.0f;
            while (AmrEngine.getSingleEngine().isRecordRunning()) {
                if (recodeTime >= MAX_TIME && MAX_TIME != 0) {
                    imgHandle.sendEmptyMessage(0);
                } else {
                    try {
                        Thread.sleep(200);
                        recodeTime += 0.2;
                        if (AmrEngine.getSingleEngine().isRecordRunning()) {
                            // voiceValue = mr.getAmplitude();
                            imgHandle.sendEmptyMessage(1);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    // 录音Dialog图片随声音大小切换
    void setDialogImage() {
        float b = ((float) voiceValue / 25000f) * 100;
        if (b > 80f) {
            voicesize.setImageResource(R.drawable.hua7);
        } else if (b > 70f) {
            voicesize.setImageResource(R.drawable.hua6);
        } else if (b > 60f) {
            voicesize.setImageResource(R.drawable.hua5);
        } else if (b > 50f) {
            voicesize.setImageResource(R.drawable.hua4);
        } else if (b > 40f) {
            voicesize.setImageResource(R.drawable.hua3);
        } else if (b > 30f) {
            voicesize.setImageResource(R.drawable.hua2);
        } else {
            voicesize.setImageResource(R.drawable.hua1);
        }
    }

    public void hideall() {
        chat_radio_panel.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);
        voicesize.setVisibility(View.VISIBLE);
    }

    public void showVoiceLoading() {
        voicesize.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
    }

    public void showVoiceCancle() {
        loading.setVisibility(View.GONE);
        voicesize.setVisibility(View.GONE);
        delete.setVisibility(View.VISIBLE);
    }

    public void showVoiceStart() {
        loading.setVisibility(View.GONE);
        voicesize.setVisibility(View.VISIBLE);
        delete.setVisibility(View.GONE);
    }

    public void addClick(int which) {
        switch (which) {
            case 0:
                if (Util.existSDcard()) {
                    Intent intent = new Intent(); // 调用照相机
                    String messagepath = StaticFactory.APKCardPathChatImg;
                    File fa = new File(messagepath);
                    if (!fa.exists()) {
                        fa.mkdirs();
                    }
                    filename = messagepath + new Date().getTime();// 图片路径
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(filename)));
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, AddPicFragment.Camera);
                } else {
                    Toast.makeText(getApplicationContext(), "亲，请检查是否安装存储卡!",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case 1:
                if (Util.existSDcard()) {
                    Intent intent = new Intent();
                    String messagepath = StaticFactory.APKCardPathChatImg;
                    File fa = new File(messagepath);
                    if (!fa.exists()) {
                        fa.mkdirs();
                    }
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, AddPicFragment.Album);
                } else {
                    Toast.makeText(getApplicationContext(), "亲，请检查是否安装存储卡!",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    Handler itemHandler = new Handler() {
        public void dispatchMessage(Message msg) {
            AlertDialog dialog;
            switch (msg.what) {
                case DfMessage.TEXT://打开详情
                    startDetail((DfMessage) msg.obj);
                    break;
                case DfMessage.IMAGE://打开图片预览
                    startImage((DfMessage) msg.obj);
                    break;
                case DfMessage.VOICE://播放声音
                    listview.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
                    ChatPlayVoiceManager.getInstance().playVoice((DfMessage) msg.obj);
                    break;
                case DfMessage.VIDEO://播放视频
                    final ChatPlayVideoFragment videoFragment = ChatPlayVideoFragment_.builder().bean((DfMessage) msg.obj).build();
                    videoFragment.show(getSupportFragmentManager(), "dialog");
                    break;
                case HANDLER_COPY://复制文本
                    dialog = new AlertDialog.Builder(ChatActivity.this).setItems(
                            new String[]{"复制文本", "删除消息"},
                            new content_click((DfMessage) msg.obj)).create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                    break;
                case HANDLER_LONGPRESS://长按删除
                    dialog = new AlertDialog.Builder(ChatActivity.this).setItems(
                            new String[]{"删除消息"},
                            new img_click((DfMessage) msg.obj)).create();
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                    break;
                case HANDLER_RESEND:// 消息重发
                    DfMessage dm = (DfMessage) msg.obj;
                    if (dm.getReceiverUid().equals(user.getId())) {
                        reReceiver(dm);//如果是下载失败
                    } else {
                        reSend(dm);//
                    }
                    break;
                case HANDLER_ZHENXINHUA://回复真心话
                    sendZhenXinHuaTextThread(msg.getData().getString("answer"), msg.getData().getString("msgid"));
                    break;
                case HANDLER_DAMAOXIAN://回复大冒险
                    senDaMaoXianTextThread(msg.getData().getString("result"), msg.getData().getString("msgid"));
                    break;
            }
            super.dispatchMessage(msg);
        }

        ;
    };

    void startDetail(DfMessage bean) {
        DfMessage.OtherHelperMessage msg = bean.getOtherHelperContent();
        DateBean db = new DateBean();
        switch (msg.getInfoType()) {
            case 0:
                DongTaiBean dbean = new DongTaiBean();
                dbean.setDyid(msg.getDyid());
                DongTaiDetailActivity_.intent(this).bean(dbean)
                        .init(false).start();
                break;
            case 1:
            case 4:
            case 3:
            case 5:
                db.setDtid(Integer.valueOf(msg.getDtid()));
                db.setUserid(user.getId());
                DateDetailActivity_.intent(this).bean(db).start();
                break;
            case 2:
                db.setDtid(Integer.valueOf(msg.getDtid()));
                db.setUserid(friend.getId());
                DateDetailActivity_.intent(this).bean(db).start();
                break;
            case 6:
            case 7:
                RedLineBean redLineBean = new RedLineBean();
                redLineBean.id = msg.getRtid();
                RedLineDetailActivity_.intent(this).bean(redLineBean).start();
                break;
        }
    }

    /**
     * 重新获取语音
     *
     * @param msg
     */

    public void reReceiver(final DfMessage msg) {
        if (msg.getMsgtype() == DfMessage.VOICE) {
            ChatDownLoadManager.getInstance(this).down(msg);
        }
    }

    public void reSend(final DfMessage msg) {
        new AlertDialog.Builder(this).setMessage("您确定要重发这条信息吗？").setTitle("提示")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        try {
                            messageDao.delete(msg, false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        adapter.remove(msg);
                        adapter.notifyDataSetChanged();
                        switch (msg.getMsgtype()) {
                            case DfMessage.TEXT:
                                sendTextThread(msg.getContent());
                                break;
                            case DfMessage.IMAGE:
                            case DfMessage.VOICE:
                                sendFile(new File(msg.getContent()), null, msg.getMsgtype());
                                break;
                            case DfMessage.VIDEO:
                                sendFile(new File(msg.getContent().split(",")[0]), new File(msg.getContent().split(",")[1]), msg.getMsgtype());
                            case DfMessage.FACE:
                                sendGifThread(msg.getGifContent());
                                break;
                        }
                    }
                }).create().show();
    }

    class content_click implements DialogInterface.OnClickListener {
        DfMessage msg;

        public content_click(DfMessage msg) {
            this.msg = msg;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 0:
                    copy(msg.getContent());
                    break;
                case 1:
                    deleteMsg(msg);
                    break;
            }
        }
    }

    public void deleteMsg(DfMessage msg) {
        try {
            messageDao.delete(msg, true);

            if (msg.getMsgtype() > 0) {
                File file = new File(msg.getContent());
                if (file.exists()) {
                    file.delete();
                }
            }
            if (adapter.getList().size() - 1 > 0) {
                ChatListBean cb = new ChatListBean(user, adapter.getItem(adapter.getList().size() - 1), friend);
                chatListDao.deleteChatList(cb);
                chatListDao.create(cb);
                Intent broad = new Intent(MyLeaveMessageFragment.ADDMSG);
                broad.putExtra("bean", cb);
                sendBroadcast(broad);
            } else {
                ChatListBean cb = new ChatListBean(user, new DfMessage(), friend);
                chatListDao.deleteChatList(cb);
                Intent broad = new Intent(MyLeaveMessageFragment.ADDMSG);
                broad.putExtra("bean", cb);
                sendBroadcast(broad);
                Intent intent = new Intent(MyLeaveMessageFragment.DELMSG);
                intent.putExtra("bean", cb);
                sendBroadcast(intent);
            }

            adapter.remove(msg);
            adapter.notifyDataSetChanged();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void copy(String content) {
        if (SdkVersionHelper.getSdkInt() >= 11) {
            ClipboardManager c = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            c.setPrimaryClip(ClipData.newPlainText(null, content));
        } else {
            android.text.ClipboardManager c = (android.text.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            c.setText(content);
        }

        Toast.makeText(this, "已复制", Toast.LENGTH_SHORT).show();
    }

    class img_click implements DialogInterface.OnClickListener {
        DfMessage msg;

        public img_click(DfMessage msg) {
            this.msg = msg;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            deleteMsg(msg);
        }
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ChatPlayVoiceManager.getInstance().stopArm(null);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        ChatPlayVoiceManager.getInstance().stopArm(null);
        mSensorManager.unregisterListener(this);
    }

    public void startImage(DfMessage msg) {
        List<DfMessage> imgDf = new ArrayList<DfMessage>();
        List<DfMessage> list = adapter.getList();
        for (DfMessage dfMessage : list) {
            if (dfMessage.getMsgtype() == 1) {
                imgDf.add(dfMessage);
            }
        }
        int position = imgDf.indexOf(msg);
        List<ImageBean> beans = new ArrayList<ImageBean>();
        for (DfMessage dfMessage : imgDf) {
            ImageBean ib = new ImageBean();
            ib.imgpath = dfMessage.getContent();
            beans.add(ib);
        }
        ChatPlayVoiceManager.getInstance().stopArm(ChatPlayVoiceManager.getInstance().playingMsg);
        ImageBrowserActivity_.intent(this).mPosition(position).ibl(new ImageBeanList(beans))
                .start();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mSensorManager.registerListener(this, mSensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float range = event.values[0];
        if (range >= mSensor.getMaximumRange()) {
            audioManager.setMode(AudioManager.MODE_NORMAL);
        } else {
            audioManager.setMode(AudioManager.MODE_IN_CALL);
        }
    }

    @UiThread
    void scrollToLast(DfMessage bean) {

        listview.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);

        if (bean != null) {
            adapter.add(bean);
            adapter.notifyDataSetChanged();
        }

        if (adapter.getList().size() > 0) {
            listview.smoothScrollToPosition(adapter.getList().size() - 1);
        }
    }

    @UiThread
    void scrollToPosition(int from) {
        listview.setSelection(from);
    }

    @Override
    public void OnKeyBoardStateChange(int state, int height) {

    }

    @Override
    public void OnSendBtnClick(String msg) {
        sendTextThread(msg);
    }

    @Override
    public void OnSendEmotion(EmoticonActivityListBean.EmoticonZip.EmoticonImageBean bean) {
        sendGifThread(new EmoticonJsonBean(bean.getGifUrl(),
                bean.getFlagName(), bean.getGiffile()));
    }

    @Override
    public void OnMoreItemClick(int position) {
        switch (position) {
            case 0:
                zhenxinhua(0);
                break;
            case 1:
                damaoxian();
                break;
            case 2:
                addClick(1);
                break;
            case 3:
                addClick(0);
                break;
            case 4:
                zhenxinhua(1);
                break;
            case 5:
                //视频录制
                Intent intent = new Intent(this, MediaRecorderActivity.class);
                startActivityForResult(intent, AddPicFragment.RADIO);
                break;
        }
    }

    public void zhenxinhua(int type) {

        ZhenXinHuaTable zt = zhenXinHuaDao.getZhenLastByUserId(user.getId(), friend.getId(), DfMessage.ZHENXINHUA);
        //如果双方都选了，或者超过1分钟
        if (zt == null || (!TextUtils.isEmpty(zt.getSendContent()) && !TextUtils.isEmpty(zt.getReceiverContent())) || zt.getTime() < new Date().getTime() - 60 * 1000) {
            RequestParams requestParams = Util.getAjaxParams(this);
            requestParams.put("otherId", friend.getId());
            requestParams.put("proType", type);
            ac.finalHttp.post(URL.CHAT_ZHENXINHUA, requestParams, new MyJsonHttpResponseHandler(this, getString(R.string.loading)) {
                @Override
                public void onSuccessRetCode(JSONObject jo) throws Throwable {
                    DfMessage defMessage = new Gson().fromJson(
                            jo.optJSONObject(URL.RESPONSE).opt(SocketManage.MESSAGE).toString(), DfMessage.class);
                    defMessage.setDownload(SocketManage.D_downloaded);
                    //保存到数据库
                    ChatListBean cb = new ChatListBean(user, defMessage, friend);
                    messageDao.saveMessage(defMessage, cb);

                    //移动到最后一行
                    scrollToLast(defMessage);
                }
            });
        } else {
            Util.toast(this, "您还有一条真心话未完成");
        }
    }

    public void damaoxian() {
        ZhenXinHuaTable zt = zhenXinHuaDao.getZhenLastByUserId(user.getId(), friend.getId(), DfMessage.DAMAOXIAN);
        //如果双方都选了，或者超过1分钟
        if (zt == null || (!TextUtils.isEmpty(zt.getSendContent()) && !TextUtils.isEmpty(zt.getReceiverContent())) || zt.getTime() < new Date().getTime() - 60 * 1000) {
            RequestParams requestParams = Util.getAjaxParams(this);
            requestParams.put("otherId", friend.getId());
            ac.finalHttp.post(URL.CHAT_DAMAOXIAN, requestParams, new MyJsonHttpResponseHandler(this, getString(R.string.loading)) {
                @Override
                public void onSuccessRetCode(JSONObject jo) throws Throwable {
                    DfMessage defMessage = new Gson().fromJson(
                            jo.optJSONObject(URL.RESPONSE).opt(SocketManage.MESSAGE).toString(), DfMessage.class);
                    defMessage.setDownload(SocketManage.D_downloaded);
                    //保存到数据库
                    ChatListBean cb = new ChatListBean(user, defMessage, friend);
                    messageDao.saveMessage(defMessage, cb);

                    //移动到最后一行
                    scrollToLast(defMessage);
                }
            });
        } else {
            Util.toast(this, "您还有一条大冒险未完成");
        }
    }

}
