package com.quanliren.quan_two.activity.group;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.group.quan.QuanListFragment;
import com.quanliren.quan_two.activity.user.BlackListFragment;
import com.quanliren.quan_two.activity.user.UserInfoActivity_;
import com.quanliren.quan_two.activity.user.UserOtherInfoActivity_;
import com.quanliren.quan_two.adapter.QuanDetailReplyAdapter;
import com.quanliren.quan_two.adapter.QuanDetailReplyAdapter.IQuanDetailReplyAdapter;
import com.quanliren.quan_two.bean.DongTaiBean;
import com.quanliren.quan_two.bean.DongTaiReplyBean;
import com.quanliren.quan_two.bean.LoginUser;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.custom.CustomRelativeLayout;
import com.quanliren.quan_two.custom.DongTaiZanLinearLayout;
import com.quanliren.quan_two.custom.UserNickNameRelativeLayout;
import com.quanliren.quan_two.custom.emoji.EmoteView;
import com.quanliren.quan_two.custom.emoji.EmoticonsEditText;
import com.quanliren.quan_two.pull.XListView;
import com.quanliren.quan_two.pull.swipe.SwipeRefreshLayout;
import com.quanliren.quan_two.util.ImageUtil;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@EActivity(R.layout.quan_detail)
@OptionsMenu({R.menu.quan_detail, R.menu.dongtai_jubao_menu})
public class DongTaiDetailActivity extends BaseActivity implements
        IQuanDetailReplyAdapter, XListView.IXListViewListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = "DongTaiDetailActivity";

    @ViewById
    XListView listview;
    @ViewById
    SwipeRefreshLayout swipe;
    @ViewById
    EmoticonsEditText reply_content;
    @ViewById
    ImageView emoji_btn;
    @FragmentById(R.id.chat_eiv_inputview)
    EmoteView gridview;
    @ViewById
    View chat_layout_emote;
    @ViewById
    Button send_btn;

    @Extra
    DongTaiBean bean;

    //    View headView, line;
    View headView;
    ImageView userlogo, img_content;
    UserNickNameRelativeLayout nickname_rl;
    QuanDetailReplyAdapter adapter;
    //    GridView gridView;
//    QuanPicAdapter picadapter;
    TextView time, signature, reply_btn,reply_tv, location;
    LayoutParams lp;
    View content_rl,show;
    DongTaiZanLinearLayout zan_ll;
    int imgWidth;
    @ViewById
    View bottom_ll;
    @ViewById
    CustomRelativeLayout crl;
    @OptionsMenuItem
    MenuItem delete;
    @OptionsMenuItem
    MenuItem jubao;
    @Extra
    boolean init = true;
    @Override
    public void init() {
        super.init();
        swipe.setOnRefreshListener(this);

        listview.addHeaderView(headView = LayoutInflater.from(this).inflate(
                R.layout.quan_detail_head, listview, false));
        listview.addFooterView(new View(this));

        List<DongTaiReplyBean> list = new ArrayList<DongTaiReplyBean>();
        listview.setAdapter(adapter = new QuanDetailReplyAdapter(this));
        adapter.setListener(this);
        listview.setXListViewListener(this);

        findheadview();

        crl.setOnSizeChangedListener(new CustomRelativeLayout.OnSizeChangedListener() {

            @Override
            public void open(int height) {

                setselection();
            }

            @Override
            public void close() {
            }
        });
        bottom_ll.setTranslationY(ImageUtil.dip2px(this, 50));

        refresh();
    }

    @UiThread(delay = 200l)
    public void refresh() {
        swipe.setRefreshing(true);
    }

    @Extra
    DongTaiReplyBean selectBean;

    @UiThread
    void setselection() {
        if (selectBean != null) {
            if (adapter.getList().indexOf(selectBean) == -1) {
                List<DongTaiReplyBean> list = adapter.getList();
                boolean b = false;
                for (DongTaiReplyBean dongTaiReplyBean : list) {
                    if (selectBean.getId() != null && selectBean.getId().equals(dongTaiReplyBean.getId())) {
                        selectBean = dongTaiReplyBean;
                        b = true;
                    }
                }
                if (b) {
                    listview.setSelection(adapter.getList().indexOf(selectBean) + 1);
                }
            } else {
                listview.setSelection(adapter.getList().indexOf(selectBean) + 1);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        delete.setVisible(false);
        jubao.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Click(R.id.left_icon)
    public void finishAcy() {
        scrollToFinishActivity();
    }

    public void isMy() {
        if (bean != null) {
            if (bean.getUserid() != null && bean.getUserid().equals(ac.getLoginUserId())) {
                delete.setVisible(true);
                jubao.setVisible(false);
            } else {
                jubao.setVisible(true);
                delete.setVisible(false);
            }
        }
    }

    public void findheadview() {
//        gridView = (GridView) headView.findViewById(R.id.pic_gridview);
        userlogo = (ImageView) headView.findViewById(R.id.userlogo);
        img_content = (ImageView) headView.findViewById(R.id.img_content);
        nickname_rl = (UserNickNameRelativeLayout) headView.findViewById(R.id.nickname_rl);
        signature = (TextView) headView.findViewById(R.id.signature);
        time = (TextView) headView.findViewById(R.id.time);
        imgWidth = (getResources().getDisplayMetrics().widthPixels - ImageUtil.dip2px(this, 88)) / 3;
//        line = headView.findViewById(R.id.line);
//        picadapter = new QuanPicAdapter(this, new ArrayList<String>(), imgWidth);
//        gridView.setAdapter(picadapter);

        lp = new LayoutParams(
                LayoutParams.FILL_PARENT, imgWidth);
        lp.addRule(RelativeLayout.BELOW, R.id.signature);
        location = (TextView) headView.findViewById(R.id.location);
        reply_btn = (TextView) headView.findViewById(R.id.reply_btn);
        reply_tv = (TextView) headView.findViewById(R.id.reply_tv);
//        gridView.setLayoutParams(lp);
        content_rl = headView.findViewById(R.id.content_rl);
        show = headView.findViewById(R.id.show);
        zan_ll = (DongTaiZanLinearLayout) headView.findViewById(R.id.zan_ll);

        content_rl.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                contentClick(null);
            }
        });

        reply_content.setOnEditorActionListener(editListener);
        reply_content.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean b) {
                if (b) {
                    chat_layout_emote.setVisibility(View.GONE);
                }
            }
        });
        reply_content.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                chat_layout_emote.setVisibility(View.GONE);
                emoji_btn.setImageResource(R.drawable.chat_face_btn_normal);
            }
        });
        reply_btn.setVisibility(View.GONE);
        gridview.setEditText(reply_content);
        headView.setVisibility(View.GONE);
    }

    public void setHeadSource() {
        headView.setVisibility(View.VISIBLE);
        if (bean.getImglist() == null || bean.getImglist().size() == 0) {
            show.setVisibility(View.VISIBLE);
            img_content.setImageResource(R.drawable.dongtai_content_default);
        } else {
            show.setVisibility(View.GONE);
            ImageLoader.getInstance().displayImage(bean.getImglist().get(0).imgpath+StaticFactory._640x640, img_content,ac.options_dongtai, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    show.setVisibility(View.VISIBLE);
                }
            });
        }


        ImageLoader.getInstance().displayImage(
                bean.getAvatar() + StaticFactory._160x160, userlogo,
                ac.options_userlogo);
        nickname_rl.setUser(bean.getNickname(), Integer.parseInt(bean.getSex()), bean.getAge(), bean.getIsvip());
        time.setText(Util.getTimeDateStr(bean.getCtime()));
        if (bean.getContent().trim().length() > 0) {
            signature.setVisibility(View.VISIBLE);
            signature.setText(bean.getContent());
        } else {
            signature.setVisibility(View.GONE);
        }
        if (bean.getFontColor() == 0) {
            signature.setTextColor(ac.getResources().getColor(R.color.white));
            signature.setShadowLayer(1, 1, 1, ac.getResources().getColor(R.color.black));
        } else {
            signature.setTextColor(ac.getResources().getColor(R.color.black));
            signature.setShadowLayer(1, 1, 1, ac.getResources().getColor(R.color.white));
        }
        userlogo.setOnClickListener(userlogoClick);
        nickname_rl.setOnClickListener(userlogoClick);

        if (bean.getLatitude() != null && bean.getLongitude() != null && !"".equals(bean.getLatitude()) && !"".equals(bean.getLongitude())
                && !ac.cs.getLat().equals("")) {
            location.setText(Util.getDistance(
                    Double.valueOf(ac.cs.getLng()),
                    Double.valueOf(ac.cs.getLat()), Double.valueOf(bean.getLongitude()),
                    Double.valueOf(bean.getLatitude()))
                    + "km");
        } else {
            location.setText("");
        }

        if (Integer.parseInt(bean.getCnum()) == 0) {
            reply_tv.setVisibility(View.GONE);
        } else {
            reply_tv.setVisibility(View.VISIBLE);
            reply_tv.setText("共 " + bean.getCnum() + " 条评论：");
        }
        zan_ll.setBean(bean);
    }

    void isEmpty() {
        if (adapter.getList().size() == 0) {
            reply_tv.setVisibility(View.GONE);
        }else{
            reply_tv.setVisibility(View.VISIBLE);
        }
    }

    OnClickListener userlogoClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent i = new Intent(DongTaiDetailActivity.this, bean.getUserid()
                    .equals(ac.getLoginUserId()) ? UserInfoActivity_.class
                    : UserOtherInfoActivity_.class);
            i.putExtra("userId", bean.getUserid());
            startActivity(i);
        }
    };

    @OptionsItem(R.id.delete)
    public void rightClick() {
        new android.support.v7.app.AlertDialog.Builder(this).setTitle("提示").setMessage("您确定要删除这条动态吗？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestParams ap = getAjaxParams();
                        ap.put("dyid", bean.getDyid() + "");
                        ac.finalHttp.post(URL.DELETE_DONGTAI, ap,
                                new JsonHttpResponseHandler() {
                                    @Override
                                    public void onStart() {
                                        customShowDialog("正在删除");
                                    }

                                    @Override
                                    public void onSuccess(JSONObject jo) {
                                        customDismissDialog();
                                        try {
                                            int status = jo.getInt(URL.STATUS);
                                            switch (status) {
                                                case 0:
                                                    showCustomToast("删除成功");
                                                    Intent i = new Intent();
                                                    i.putExtra("bean", bean);
                                                    setResult(QuanListFragment.QuanDetailResult_delete, i);
                                                    finish();
                                                    break;
                                                default:

                                                    showFailInfo(jo);
                                                    break;
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFailure() {
                                        customDismissDialog();
                                        showIntentErrorToast();
                                    }
                                });
                    }
                }).create().show();
    }

    @OptionsItem(R.id.jubao)
    public void jubao() {
        if (bean == null) {
            return;
        }
        LoginUser my = getHelper().getUser();
        if (my == null) {
            startLogin();
            return;
        }
        if (my.getId().equals(bean.getUserid())) {
            showCustomToast("这是自己的哟~");
            return;
        }
        new android.support.v7.app.AlertDialog.Builder(DongTaiDetailActivity.this)
                .setTitle("提示")
                .setMessage("您确定要举报这条动态吗？")
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialogChoiceReason();
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        }).create().show();
    }

    public void dialogChoiceReason() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setItems(
                        new String[]{"骚扰信息", "个人资料不当", "盗用他人资料", "垃圾广告",
                                "色情相关"},
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                RequestParams ap = getAjaxParams();
                                ap.put("otherid", bean.getUserid());
                                ap.put("type", which + "");
                                ap.put("ptype", 1 + "");
                                ap.put("tid", bean.getDyid());
                                ac.finalHttp.post(URL.JUBAO, ap,
                                        addBlackCallBack);
                            }
                        }).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    JsonHttpResponseHandler addBlackCallBack = new JsonHttpResponseHandler() {
        public void onStart() {
            customShowDialog("正在发送请求");
        }

        public void onSuccess(JSONObject jo) {
            try {
                int status = jo.getInt(URL.STATUS);
                switch (status) {
                    case 0:
                        showCustomToast("举报成功");
                        Intent i = new Intent(BlackListFragment.ADDEBLACKLIST);
                        i.putExtra("bean", bean);
                        sendBroadcast(i);
                        break;
                    default:
                        showFailInfo(jo);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                customDismissDialog();
            }
        }

        public void onFailure() {
            customDismissDialog();
            showIntentErrorToast();
        }

    };
    JsonHttpResponseHandler callBack = new JsonHttpResponseHandler() {

        public void onFailure() {
            swipe.setRefreshing(false);
            listview.stop();
            showIntentErrorToast();
        }

        public void onSuccess(JSONObject jo) {
            try {
                int status = jo.getInt(URL.STATUS);
                switch (status) {
                    case 0:
                        if (isFirst) {
                            bean = new Gson().fromJson(jo.getString(URL.RESPONSE),
                                    new TypeToken<DongTaiBean>() {
                                    }.getType());
                            crl.setVisibility(View.VISIBLE);
                            setHeadSource();
                            adapter.setList(bean.getCommlist());
                            if (adapter.getList().size() < 20) {
                                p = "-1";
                            } else {
                                p = "1";
                            }
                        } else {
                            JSONObject jj = jo.getJSONObject(URL.RESPONSE);
                            p = jj.getString("p");
                            List<DongTaiReplyBean> commlist = new Gson().fromJson(jj.getString("commlist"),
                                    new TypeToken<ArrayList<DongTaiReplyBean>>() {
                                    }.getType());
                            crl.setVisibility(View.VISIBLE);
                            adapter.add(commlist);
                        }
                        adapter.notifyDataSetChanged();
                        bottom_ll.animate().translationY(0);
                        if (selectBean != null) {
                            contentClick(selectBean);
                        }
                        if ("-1".equals(p)) {
                            listview.setPage(-1);
                        } else {
                            listview.setPage(1);
                        }
                        break;
                    default:
                        crl.setVisibility(View.GONE);
                        showFailInfo(jo);
                        break;
                }
                isMy();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                swipe.setRefreshing(false);
                listview.stop();
            }
        }

    };

    @Click
    void send_btn() {
        if (getHelper().getUser() == null) {
            startLogin();
            return;
        }

        String content = reply_content.getText().toString().trim();
        if (content.length() == 0) {
            showCustomToast("请输入内容");
            return;
        }
        RequestParams ap = getAjaxParams();
        ap.put("dyid", bean.getDyid() + "");
        ap.put("content", content);

        DongTaiReplyBean replayBean = new DongTaiReplyBean();

        Object obj = reply_content.getTag();
        if (obj != null) {
            DongTaiReplyBean rb = (DongTaiReplyBean) obj;
            ap.put("replyuid", rb.getUserid());
            replayBean.setReplyuid(rb.getUserid());
            replayBean.setReplyuname(rb.getNickname());
        }
        User user = getHelper().getUserInfo();
        ap.put("nickname", user.getNickname());
        replayBean.setContent(content);
        replayBean.setAvatar(user.getAvatar());
        replayBean.setNickname(user.getNickname());
        replayBean.setUserid(user.getId());
        replayBean.setCtime(Util.fmtDateTime.format(new Date()));

        ac.finalHttp.post(URL.REPLY_DONGTAI, ap, new replyCallBack(replayBean));

        reply_content.setText("");
        reply_content.setHint("");
        reply_content.setTag(null);

        //点击发送，关闭键盘
        Util.closeInput(this);
        if (chat_layout_emote.getVisibility() == View.VISIBLE) {
            chat_layout_emote.setVisibility(View.GONE);
        }
        emoji_btn.setImageResource(R.drawable.chat_face_btn_normal);
    }

    OnEditorActionListener editListener = new OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
            if (arg1 == EditorInfo.IME_ACTION_SEND) {
                send_btn();
            }
            return false;
        }
    };

    @Override
    public void onRefresh() {
        p = "1";
        isFirst = true;
        RequestParams rp = getAjaxParams();
        if (bean != null) {
            rp.put("dyid", bean.getDyid() + "");
        }
        ac.finalHttp.post(URL.GETDONGTAI_DETAIL, rp, callBack);

    }

    @Override
    public void onLoadMore() {
        isFirst = false;
        RequestParams rp = getAjaxParams();
        if (Integer.valueOf(p) > 0) {
            rp.put("dyid", bean.getDyid() + "");
            rp.put("p", p);
            ac.finalHttp.post(URL.GETDONGTAI_DETAIL_OTHER,
                    rp, callBack);
        }

    }

    class replyCallBack extends JsonHttpResponseHandler {
        DongTaiReplyBean replayBean;

        public replyCallBack(DongTaiReplyBean replayBean) {
            this.replayBean = replayBean;
        }

        public void onStart() {
            bean.getCommlist().add(0, replayBean);
            adapter.notifyDataSetChanged();
        }

        public void onSuccess(JSONObject jo) {
            try {
                int status = jo.getInt(URL.STATUS);
                switch (status) {
                    case 0:
                        showCustomToast("回复成功");
                        String id = jo.getJSONObject(URL.RESPONSE).getString("id");
                        replayBean.setId(id);
                        bean.setCnum(String.valueOf(Integer.valueOf(bean.getCnum()) + 1));
                        reply_tv.setVisibility(View.VISIBLE);
                        reply_tv.setText("共 " + bean.getCnum() + " 条评论：");
                        selectBean = null;
                        adapter.notifyDataSetChanged();
                        setselection();
                        break;
                    default:
                        showFailInfo(jo);
                        bean.getCommlist().remove(replayBean);
                        adapter.notifyDataSetChanged();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void onFailure() {
            bean.getCommlist().remove(replayBean);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void contentClick(DongTaiReplyBean bean) {
        LoginUser user = getHelper().getUser();
        if (user == null) {
            startLogin();
            return;
        }
        if (bean != null && !bean.getUserid().equals(user.getId())) {
            reply_content.setTag(bean);
            reply_content.setHint("回复 " + bean.getNickname() + " :");
            selectBean = bean;
        } else {
            reply_content.setTag(null);
            reply_content.setHint("");
            selectBean = null;
        }

        showKeyBoard();
        reply_content.requestFocus();
    }

    @Override
    public void deleteContentCick(final DongTaiReplyBean bean2) {
        LoginUser user = getHelper().getUser();
        if (user == null) {
            startLogin();
            return;
        }
        if (bean2 != null && bean2.getUserid().equals(user.getId())) {
            final RequestParams rp = getAjaxParams();
            rp.put("replyid", bean2.getId());
            new android.support.v7.app.AlertDialog.Builder(this).setTitle("提示").setMessage("您确定要删除这条评论吗？")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ac.finalHttp.post(URL.D_DT_DYREPLY, rp,
                                    new JsonHttpResponseHandler() {
                                        @Override
                                        public void onStart() {
                                            customShowDialog("正在删除");
                                        }

                                        @Override
                                        public void onSuccess(JSONObject jo) {
                                            customDismissDialog();
                                            try {
                                                int status = jo.getInt(URL.STATUS);
                                                switch (status) {
                                                    case 0:
                                                        showCustomToast("删除成功");
                                                        List<DongTaiReplyBean> beans = adapter.getList();
                                                        int position = -1;
                                                        for (DongTaiReplyBean b : beans) {
                                                            if (b.getId() == (bean2.getId())) {
                                                                position = beans.indexOf(b);
                                                                break;
                                                            }
                                                        }
                                                        bean.setCnum(String.valueOf(Integer.valueOf(bean.getCnum()) - 1));
                                                        reply_tv.setText("共 " + bean.getCnum() + " 条评论：");
                                                        if (position != -1) {
                                                            adapter.remove(position);
                                                            adapter.notifyDataSetChanged();
                                                            if (adapter.getCount() == 0) {
                                                                listview.setVisibility(View.VISIBLE);
                                                                swipe.setRefreshing(true);
                                                            }
                                                        }
                                                        isEmpty();
                                                        break;
                                                    default:
                                                        showFailInfo(jo);
                                                        break;
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        @Override
                                        public void onFailure() {
                                            customDismissDialog();
                                            showIntentErrorToast();
                                        }
                                    });
                        }
                    }).create().show();
        }

    }

    @Override
    public void logoCick(DongTaiReplyBean bean) {
        Intent i = new Intent(this, bean.getUserid()
                .equals(ac.getLoginUserId()) ? UserInfoActivity_.class
                : UserOtherInfoActivity_.class);
        i.putExtra("userId", bean.getUserid());
        startActivity(i);
    }

    @Click(R.id.emoji_btn)
    public void add_emoji_btn(View v) {
        InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (chat_layout_emote.getVisibility() == View.VISIBLE) {
            chat_layout_emote.setVisibility(View.GONE);
            reply_content.requestFocus();
            //打开软键盘
            mInputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            emoji_btn.setImageResource(R.drawable.chat_face_btn_normal);
        } else {
            chat_layout_emote.setVisibility(View.VISIBLE);
            // 隐藏软键盘
            mInputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            emoji_btn.setImageResource(R.drawable.chat_borad_btn_normal);

        }

    }

    public void onBackPressed() {
        if (chat_layout_emote.getVisibility() == View.VISIBLE) {
            chat_layout_emote.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }

    }

    boolean isFirst = true;
    String p = "1";

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            /*隐藏软键盘*/
            Util.closeInput(this);
            if (chat_layout_emote.getVisibility() == View.VISIBLE) {
                chat_layout_emote.setVisibility(View.GONE);
            }
            emoji_btn.setImageResource(R.drawable.chat_face_btn_normal);
            return true;
        }
        emoji_btn.setImageResource(R.drawable.chat_face_btn_normal);
        return super.dispatchKeyEvent(event);
    }

}
