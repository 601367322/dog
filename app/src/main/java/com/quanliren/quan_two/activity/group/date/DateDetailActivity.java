package com.quanliren.quan_two.activity.group.date;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.public_comments.RestaurantDetailActivity_;
import com.quanliren.quan_two.activity.user.BlackListFragment;
import com.quanliren.quan_two.activity.user.UserInfoActivity_;
import com.quanliren.quan_two.activity.user.UserOtherInfoActivity_;
import com.quanliren.quan_two.adapter.QuanDetailReplyAdapter;
import com.quanliren.quan_two.adapter.QuanDetailReplyAdapter.IQuanDetailReplyAdapter;
import com.quanliren.quan_two.bean.DateBean;
import com.quanliren.quan_two.bean.DongTaiReplyBean;
import com.quanliren.quan_two.bean.LoginUser;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.custom.CustomRelativeLayout;
import com.quanliren.quan_two.custom.CustomRelativeLayout.OnSizeChangedListener;
import com.quanliren.quan_two.custom.UserLogo;
import com.quanliren.quan_two.custom.UserNickNameRelativeLayout;
import com.quanliren.quan_two.custom.emoji.EmoteView;
import com.quanliren.quan_two.custom.emoji.EmoticonsEditText;
import com.quanliren.quan_two.pull.XListView;
import com.quanliren.quan_two.pull.swipe.SwipeRefreshLayout;
import com.quanliren.quan_two.util.ImageUtil;
import com.quanliren.quan_two.util.LogUtil;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.FragmentById;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@OptionsMenu({R.menu.dongtai_jubao_menu, R.menu.date_detail_menu})
@EActivity(R.layout.date_detail)
public class DateDetailActivity extends BaseActivity implements XListView.IXListViewListener, IQuanDetailReplyAdapter, SwipeRefreshLayout.OnRefreshListener {

    private static final int MANAGE = 1;

    @ViewById
    TextView collect_txt;
    @ViewById
    SwipeRefreshLayout swipe;
    @ViewById
    View bottom_ll;
    @OptionsMenuItem
    MenuItem delete;
    @OptionsMenuItem
    MenuItem jubao;
    @Extra
    DateBean bean;
    @Extra
    String user_id;
    @ViewById
    XListView listview;
    QuanDetailReplyAdapter adapter;
    View convertView;
    @ViewById
    View join;
    @ViewById
    View emoji_ll;
    @ViewById
    View send_ll;
    @ViewById
    TextView join_tv;
    @ViewById
    View favorite;
    @ViewById
    View reply;
    @ViewById
    View reply_ll;
    @ViewById
    EmoticonsEditText reply_content;
    @ViewById
    Button send_btn;
    @ViewById
    ImageView emoji_btn;
    @FragmentById(R.id.chat_eiv_inputview)
    EmoteView gridview;
    @ViewById
    View chat_layout_emote;
    @ViewById
    CustomRelativeLayout crl;
    @ViewById
    View manage;
    @ViewById
    ImageView date_img;
    @Extra
    DongTaiReplyBean selectBean;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        delete.setVisible(false);
        jubao.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @UiThread(delay = 200l)
    public void refresh() {
        swipe.setRefreshing(true);
    }

    @Override
    public void deleteContentCick(final DongTaiReplyBean bean2) {
        if (getHelper().getUser() == null) {
            startLogin();
            return;
        }
        if (bean2 != null
                && bean2.getUserid().equals(getHelper().getUser().getId())) {
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

                            ac.finalHttp.post(URL.D__DATE_DAREPLY, rp,
                                    new JsonHttpResponseHandler() {
                                        @Override
                                        public void onStart() {
                                            customShowDialog("正在删除评论");
                                        }

                                        @Override
                                        public void onSuccess(JSONObject jo) {
                                            customDismissDialog();
                                            try {
                                                int status = jo.getInt(URL.STATUS);
                                                switch (status) {
                                                    case 0:
                                                        showCustomToast("评论删除成功");
                                                        List<DongTaiReplyBean> beans = adapter.getList();
                                                        int position = -1;
                                                        for (DongTaiReplyBean b : beans) {
                                                            if (b.getId() == (bean2.getId())) {
                                                                position = beans.indexOf(b);
                                                                break;
                                                            }
                                                        }
                                                        bean.setCnum(bean.getCnum() - 1);
                                                        setHeadSource(bean);
                                                        if (position != -1) {
                                                            adapter.remove(position);
                                                            adapter.notifyDataSetChanged();
                                                            if (adapter.getCount() == 0) {
                                                                listview.setVisibility(View.VISIBLE);
                                                                swipe.setRefreshing(true);
                                                            }
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

    @AfterViews
    void initView() {
        swipe.setOnRefreshListener(this);
        crl.setOnSizeChangedListener(new OnSizeChangedListener() {

            @Override
            public void open(int height) {
                setselection();
            }

            @Override
            public void close() {
            }
        });

        bottom_ll.setTranslationY(ImageUtil.dip2px(this, 50));
        reply_ll.setVisibility(View.GONE);
        reply_ll.setTranslationY(ImageUtil.dip2px(this, 50));

        View view = new View(this);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, ImageUtil.dip2px(this, 58));
        view.setLayoutParams(lp);
        listview.addFooterView(view);
        listview.addHeaderView(convertView = View.inflate(this, R.layout.date_item_new, null));
        if (bean != null) {
            crl.setVisibility(View.VISIBLE);
            setHeadSource(bean);
        }
        listview.setAdapter(adapter = new QuanDetailReplyAdapter(this));
        adapter.setListener(this);
        listview.setXListViewListener(this);
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

        gridview.setEditText(reply_content);

        refresh();
    }

    @Click
    void send_ll() {
        return;
    }

    @Click
    void emoji_ll() {
        return;
    }

    @Click
    void right_title() {
        if ("举报".equals(right_title.getText())) {
            jubao();
        } else if ("结束".equals(right_title.getText())) {
            rightClick();
        } else {
            return;
        }
    }

    @Click
    void send_btn() {
//        String content = Util.FilterEmoji(reply_content.getText().toString().trim());
        String content = reply_content.getText().toString().trim();
        if (content.length() == 0) {
            showCustomToast("请输入内容");
            return;
        }
        RequestParams ap = getAjaxParams();
        ap.put("dtid", bean.getDtid() + "");
        ap.put("content", content);

        DongTaiReplyBean replayBean = new DongTaiReplyBean();
        ap.put("nickname", getHelper().getUserInfo().getNickname());
        Object obj = reply_content.getTag();
        if (obj != null) {
            DongTaiReplyBean rb = (DongTaiReplyBean) obj;
            ap.put("replyuid", rb.getUserid());
            replayBean.setReplyuid(rb.getUserid());
            replayBean.setReplyuname(rb.getNickname());
        }
        ap.put("nickname", getHelper().getUserInfo().getNickname());
        replayBean.setContent(content);
        User user = getHelper().getUserInfo();
        replayBean.setAvatar(user.getAvatar());
        replayBean.setNickname(user.getNickname());
        replayBean.setUserid(user.getId());
        replayBean.setCtime(Util.fmtDateTime.format(new Date()));
        LogUtil.d("===============nickname", ap.toString());
        ac.finalHttp.post(URL.REPLY_DATE, ap, new replyCallBack(replayBean));

        reply_content.clearFocus();
        reply_content.setText("");
        reply_content.setHint("");
        reply_content.setTag(null);
        closeInput();
        chat_layout_emote.setVisibility(View.GONE);
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
                        bean.setCnum(bean.getCnum() + 1);
                        selectBean = null;
                        setselection();
                        setHeadSource(bean);
                        setData(bean);
                        adapter.setList(bean.getCommlist());
                        adapter.notifyDataSetChanged();
                        emoji_btn.setImageResource(R.drawable.chat_face_btn_normal);
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
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    boolean isFirst = true;
    String p = "1";
    JsonHttpResponseHandler callBack = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(JSONObject response) {
            try {
                int status = response.getInt(URL.STATUS);
                switch (status) {
                    case 0:
                        crl.setVisibility(View.VISIBLE);
                        if (isFirst) {
                            DateBean db = new Gson().fromJson(
                                    response.getString(URL.RESPONSE),
                                    new TypeToken<DateBean>() {
                                    }.getType());
                            if (db != null) {
                                bean = db;
                            }
                            setHeadSource(db);
                            setData(db);
                            adapter.setList(db.getCommlist());
                            if (adapter.getList().size() < 20) {
                                p = "-1";
                            } else {
                                p = "1";
                            }
                        } else {
                            JSONObject jj = response.getJSONObject(URL.RESPONSE);
                            p = jj.getString("p");
                            List<DongTaiReplyBean> commlist = new Gson().fromJson(jj.getString("commlist"),
                                    new TypeToken<ArrayList<DongTaiReplyBean>>() {
                                    }.getType());
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
                        isMy();
                        break;
                    default:
                        crl.setVisibility(View.VISIBLE);
                        showFailInfo(response);
                        jubao.setVisible(false);
                        delete.setVisible(false);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                listview.stop();
                swipe.setRefreshing(false);
            }
        }

        @Override
        public void onFailure() {
            listview.stop();
            swipe.setRefreshing(false);
            showIntentErrorToast();
        }
    };

    public void isMy() {
        if (bean != null) {
            if (bean.getUserid().equals(ac.getLoginUserId())) {
                if (bean.getDtstate() != 0) {
                    delete.setVisible(false);
                } else {
                    delete.setVisible(true);
                }
                join.setVisibility(View.GONE);
                jubao.setVisible(false);
                favorite.setVisibility(View.GONE);
                manage.setVisibility(View.VISIBLE);
            } else {
                setRightTitleTxt("举报");
                delete.setVisible(false);
                jubao.setVisible(true);
                join.setVisibility(View.VISIBLE);
                favorite.setVisibility(View.VISIBLE);
                manage.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onRefresh() {
        RequestParams rp = getAjaxParams();
        if (bean != null) {
            rp.put("dtid", bean.getDtid());
        }
        isFirst = true;
        ac.finalHttp.post(URL.DATE_DETAIL, rp, callBack);
    }

    @Override
    public void onLoadMore() {
        isFirst = false;
        RequestParams rp = getAjaxParams();
        if (bean != null) {
            rp.put("dtid", bean.getDtid());
        }
        if (Integer.valueOf(p) > 0) {
            rp.put("p", p);
            ac.finalHttp.post(URL.DATE_DETAIL_OTHER,
                    rp, callBack);
        }
    }

    class ViewHolder {
        UserLogo userlogo;
        TextView location_tv, coin, place_tv, pla,
                sex_tv, money_tv, time_tv, remark_tv, bm_people_num,
                reply_tv, date_head_text, pub_time;
        UserNickNameRelativeLayout nick_ll;
        View content_rl;
        ImageView img_state, date_selected, resta_detail;
    }

    public void setHeadSource(final DateBean db) {
        ViewHolder holder;
        if (convertView.getTag() == null) {
            holder = new ViewHolder();
            holder.userlogo = (UserLogo) convertView.findViewById(R.id.userlogo);
            holder.nick_ll = (UserNickNameRelativeLayout) convertView.findViewById(R.id.nick_ll);
            holder.resta_detail = (ImageView) convertView.findViewById(R.id.resta_detail);
            holder.location_tv = (TextView) convertView
                    .findViewById(R.id.location_tv);
            holder.coin = (TextView) convertView.findViewById(R.id.dog_food);
            holder.pla = (TextView) convertView.findViewById(R.id.pla);
            holder.place_tv = (TextView) convertView
                    .findViewById(R.id.place_tv);
            holder.place_tv.setSingleLine(false);
            holder.sex_tv = (TextView) convertView.findViewById(R.id.sex_tv);
            holder.pub_time = (TextView) convertView.findViewById(R.id.pub_time);
            holder.money_tv = (TextView) convertView
                    .findViewById(R.id.money_tv);
            holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
            holder.remark_tv = (TextView) convertView
                    .findViewById(R.id.remark_tv);
            holder.remark_tv.setSingleLine(false);
            holder.bm_people_num = (TextView) convertView
                    .findViewById(R.id.bm_people_num);
            holder.reply_tv = (TextView) convertView
                    .findViewById(R.id.reply_tv);
            holder.date_head_text = (TextView) convertView.findViewById(R.id.date_head_text);
            holder.content_rl = convertView.findViewById(R.id.top);
            holder.img_state = (ImageView) convertView.findViewById(R.id.img_state);
            holder.date_selected = (ImageView) convertView.findViewById(R.id.date_selected);
            holder.date_selected.setVisibility(View.GONE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.pub_time.setText(Util.getTimeDateStr(db.getPubtime()));
        holder.content_rl.setOnClickListener(content);
        ImageLoader.getInstance().displayImage(
                db.getAvatar() + StaticFactory._160x160, holder.userlogo, ac.options_userlogo);
        if (db.getBusurl() != null && !"".equals(db.getBusurl())) {
            holder.resta_detail.setVisibility(View.VISIBLE);
            holder.resta_detail.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    RestaurantDetailActivity_.intent(DateDetailActivity.this).extra("business_url", db.getBusurl()).start();
                }
            });

        } else {
            holder.resta_detail.setVisibility(View.GONE);
        }
        if (bean.getAge() == null || "".equals(bean.getAge())) {
            holder.nick_ll.setUser(db.getNickname(), bean.getSex(), "0", bean.getIsvip());
        } else {
            holder.nick_ll.setUser(db.getNickname(), bean.getSex(), bean.getAge(), bean.getIsvip());
        }

        holder.userlogo.setOnClickListener(userlogo);
        if (ac.getLoginUserId() != null && !"".equals(ac.getLoginUserId()) && ac.getLoginUserId().equals(db.getCuserId())) {
            holder.date_selected.setVisibility(View.VISIBLE);
        } else {
            holder.date_selected.setVisibility(View.GONE);
        }
        holder.resta_detail.setVisibility(View.GONE);
        switch (db.getDtype()) {
            case 1:
                holder.resta_detail.setVisibility(View.VISIBLE);
                holder.img_state.setImageResource(R.drawable.date_eat);
                holder.date_head_text.setText("约人吃饭");
                holder.resta_detail.setImageResource(R.drawable.eat_selector);
                holder.pla.setText("餐厅：");
                break;
            case 2:
                holder.resta_detail.setVisibility(View.VISIBLE);
                holder.img_state.setImageResource(R.drawable.date_movie);
                holder.date_head_text.setText("约看电影");
                holder.resta_detail.setImageResource(R.drawable.cinema_selector);
                holder.pla.setText("影院：");
                break;
            case 5:
                holder.resta_detail.setVisibility(View.GONE);
                holder.img_state.setImageResource(R.drawable.date_love);
                switch (Integer.valueOf(db.getAim())) {
                    case 0:
                        holder.date_head_text.setText("临时情侣(安慰老妈)");
                        break;
                    case 1:
                        holder.date_head_text.setText("临时情侣(狙击相亲)");
                        break;
                    case 2:
                        holder.date_head_text.setText("临时情侣(充场面)");
                        break;
                    case 3:
                        holder.date_head_text.setText("临时情侣(试着交往)");
                        break;
                }
                holder.pla.setText("城市：");
                break;
        }
        if (Integer.valueOf(db.getCnum()) <= 0) {
            holder.reply_tv.setText("0 条评论");
        } else {
            holder.reply_tv.setText(db.getCnum() + " 条评论");
        }
        if (db.getApplynum() > 0) {
            holder.bm_people_num.setText(db.getApplynum() + " 人报名");
        } else {
            holder.bm_people_num.setText("0 人报名");
        }

        if (db.getDistance() != null && !"".equals(db.getDistance())) {
            holder.location_tv.setText(Util.getDistance(db.getDistance()));
        } else if (db.getLatitude() != 0 && db.getLongitude() != 0
                && !ac.cs.getLat().equals("")) {
            holder.location_tv.setText(Util.getDistance(
                    Double.valueOf(ac.cs.getLng()),
                    Double.valueOf(ac.cs.getLat()), db.getLongitude(),
                    db.getLatitude())
                    + "km");
        }
        switch (db.getCtype()) {

            case 0:
                if(db.getCoin()>0){
                    holder.coin.setText(db.getCoin()+"");
                }else{
                    holder.coin.setText("0");
                }
                break;
            case 2:
                holder.coin.setText(db.getCoin()+"");
                break;
            case 1:
                holder.coin.setText(db.getCoin()+"");
                break;
        }
        holder.place_tv.setText(db.getAddress());

        if("1".equals(db.getObjsex())){
            holder.sex_tv.setText("帅哥");
        }else if("0".equals(db.getObjsex())){
            holder.sex_tv.setText("美女");
        }else if("2".equals(db.getObjsex())){
            holder.sex_tv.setText("不限");
        }
        if("0".equals(db.getWhopay())){
            holder.money_tv.setText("AA制");
        }else if("1".equals(db.getWhopay())){
            holder.money_tv.setText("我消费");
        }

        holder.time_tv.setText(db.getDtime());
        if(db.getRemark()==null||db.getRemark().equals("")){
            holder.remark_tv.setText("无");
        }else {
            holder.remark_tv.setText( db.getRemark());
        }



    }
    void setData(DateBean db)throws ParseException{
        Date date=Util.fmtDateTime1.parse(db.getDtime());

        boolean timeOut=false;

        if(date.getTime()<=new Date().getTime()){
            timeOut=true;
        }
        if (db.getDtstate() == 1 || db.getIsapply() == 1||timeOut) {
            if (db.getDtstate() == 1) {
                join_tv.setText("已结束");
                join.setSelected(false);

            } else if (db.getIsapply() == 1) {
                join_tv.setText("已报名");
                join.setSelected(true);
            }else if(timeOut){
                join_tv.setText("已过期");
                join.setSelected(false);
                join.setEnabled(false);
                date_img.setImageResource(R.drawable.overdue);
            }
        }else if(db.getDtstate() == 0){
            if(timeOut){
                join_tv.setText("已过期");
                date_img.setImageResource(R.drawable.overdue);
                join.setSelected(false);
                join.setEnabled(false);
                if(bean.getUserid().equals(ac.getLoginUserId())){
                    setRightTitleTxt("");
                }
            }
        }

        if (db.getIscollect() == 1) {
            collect_txt.setText("已收藏");
            favorite.setSelected(true);
        } else {
            collect_txt.setText("收藏");
            favorite.setSelected(false);
        }

    }

    OnClickListener content = new OnClickListener() {

        @Override
        public void onClick(View v) {
            contentClick(null);
        }
    };

    OnClickListener userlogo = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent i = new Intent(DateDetailActivity.this, bean.getUserid()
                    .equals(ac.getLoginUserId()) ? UserInfoActivity_.class
                    : UserOtherInfoActivity_.class);
            i.putExtra("userId", bean.getUserid());
            startActivity(i);
        }
    };

    @Click
    void join() {
        if (isFastDoubleClick()) {
            return;
        }
        if (getHelper().getUser() == null) {
            startLogin();
            return;
        }
        if (bean.getUserid().equals(ac.getLoginUserId())) {
            showCustomToast("这是自己的哟~");
            return;
        }
        if (!"2".equals(bean.getObjsex())) {
            if ("0".equals(bean.getObjsex()) && ("男".equals(getHelper().getUserInfo().getSex()) || "1".equals(getHelper().getUserInfo().getSex() + ""))) {
                showCustomToast("您不符合条件哦~");
                return;
            } else if ("1".equals(bean.getObjsex()) && ("女".equals(getHelper().getUserInfo().getSex()) || "0".equals(getHelper().getUserInfo().getSex() + ""))) {
                showCustomToast("您不符合条件哦~");
                return;
            }
        }
        Date date = null;
        boolean timeOut = false;
        try {
            date = Util.fmtDateTime1.parse(bean.getDtime());
            if (date.getTime() <= new Date().getTime()) {
                timeOut = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if ("报名".equals(join_tv.getText())) {
            RequestParams rp = getAjaxParams();
            rp.put("dtid", bean.getDtid() + "");
            rp.put("nickname", getHelper().getUserInfo().getNickname());
            ac.finalHttp.post(URL.DATE_APPLY, rp,
                    new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            try {
                                int status = response.getInt(URL.STATUS);
                                switch (status) {
                                    case 0:
                                        showCustomToast("报名成功");
                                        bean.setApplynum(bean.getApplynum() + 1);
                                        ((ViewHolder) (convertView.getTag())).bm_people_num
                                                .setText(bean.getApplynum() + " 人报名");
                                        join_tv.setText("已报名");
                                        join.setSelected(true);
                                        break;
                                    default:
                                        showFailInfo(response);
                                        join_tv.setText("报名");
                                        join.setSelected(false);
                                        break;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure() {
//                            doFail();
                            join_tv.setText("报名");
                            join.setSelected(false);
                        }
                    });
        } else if (bean.getDtstate() != 1 && !timeOut && "已报名".equals(join_tv.getText())) {
            remove_date();
        } else {
            return;
        }
    }

    @Click
    void favorite() {
        if (getHelper().getUser() == null) {
            startLogin();
            return;
        }
        if (bean.getUserid().equals(ac.getLoginUserId())) {
            showCustomToast("这是自己的哟~");
            return;
        }
        RequestParams rp = getAjaxParams();
        rp.put("dtid", bean.getDtid());
        rp.put("type", bean.getIscollect());
        ac.finalHttp.post(URL.DATE_COLLECT, rp, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                customShowDialog("正在发送请求");
            }

            @Override
            public void onSuccess(JSONObject response) {
                try {
                    int status = response.getInt(URL.STATUS);
                    switch (status) {
                        case 0:
                            if (bean.getIscollect() == 0) {
                                bean.setIscollect(1);
                                collect_txt.setText("已收藏");
                                favorite.setSelected(true);
                                showCustomToast("收藏成功");
                                Intent i = new Intent();
                                i.putExtra("bean", bean);
                                setResult(5, i);
                            } else {
                                bean.setIscollect(0);
                                collect_txt.setText("收藏");
                                favorite.setSelected(false);
                                showCustomToast("取消收藏成功");
                                Intent i = new Intent();
                                i.putExtra("bean", bean);
                                setResult(4, i);
                            }
                            break;
                        default:
                            showFailInfo(response);
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    customDismissDialog();
                }
            }

            @Override
            public void onFailure() {
                customDismissDialog();
                showIntentErrorToast();
            }
        });
    }

    AtomicBoolean show = new AtomicBoolean(false);

    @Click
    void reply() {
        if (getHelper().getUser() == null) {
            startLogin();
            return;
        }
        selectBean = null;
        if (show.compareAndSet(false, true) || reply_ll.getVisibility() == View.GONE) {
            reply_ll.animate().translationY(0)
                    .setListener(new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationStart(Animator animation) {
                            reply_ll.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            reply_content.requestFocus();
                            showKeyBoard();
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        if (chat_layout_emote.getVisibility() == View.VISIBLE) {
            reply_content.requestFocus();
            chat_layout_emote.setVisibility(View.GONE);
            showKeyBoard();
            return;
        } else if (show.compareAndSet(true, false)) {
            reply_ll.animate()
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            reply_ll.clearAnimation();
                            reply_ll.setVisibility(View.GONE);
                        }
                    }).translationY(ImageUtil.dip2px(this, 50));
            return;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            /*隐藏软键盘*/
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(DateDetailActivity.this.getCurrentFocus().getWindowToken(), 0);
            }
            if (chat_layout_emote.getVisibility() == View.VISIBLE) {
                chat_layout_emote.setVisibility(View.GONE);
            }
            emoji_btn.setImageResource(R.drawable.chat_face_btn_normal);
            return true;
        }
        emoji_btn.setImageResource(R.drawable.chat_face_btn_normal);
        return super.dispatchKeyEvent(event);
    }

    @Click(R.id.emoji_btn)
    public void add_emoji_btn(View v) {
        InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (chat_layout_emote.getVisibility() == View.VISIBLE) {
            chat_layout_emote.setVisibility(View.GONE);
            reply_content.requestFocus();
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            mInputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            emoji_btn.setImageResource(R.drawable.chat_face_btn_normal);
        } else {
            chat_layout_emote.setVisibility(View.VISIBLE);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            // 隐藏软键盘
            mInputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            emoji_btn.setImageResource(R.drawable.chat_borad_btn_normal);
        }
    }

    @Override
    public void contentClick(DongTaiReplyBean bean) {
        if (getHelper().getUser() == null) {
            startLogin();
            return;
        }
        if (show.compareAndSet(false, true)) {
            reply_ll.animate().translationY(0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            reply_ll.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            reply_content.requestFocus();
                            if (!crl.isOpen)
                                showKeyBoard();
                        }
                    });
        } else {
            reply_content.requestFocus();
            if (!crl.isOpen)
                showKeyBoard();
        }
        if (bean != null
                && !bean.getUserid().equals(getHelper().getUser().getId())) {
            reply_content.setTag(bean);
            reply_content.setHint("回复 " + bean.getNickname() + " :");
            selectBean = bean;
        } else {
            reply_content.setTag(null);
            reply_content.setHint("");
            selectBean = null;
        }
    }

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
    public void logoCick(DongTaiReplyBean bean) {
        Intent i = new Intent(this, bean.getUserid()
                .equals(ac.getLoginUserId()) ? UserInfoActivity_.class
                : UserOtherInfoActivity_.class);
        i.putExtra("userId", bean.getUserid());
        startActivity(i);
    }

    @OptionsItem(R.id.delete)
    public void rightClick() {
        new AlertDialog.Builder(this).setTitle("提示").setMessage("您确定要结束这条约会吗？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestParams ap = getAjaxParams();
                        ap.put("dtid", bean.getDtid() + "");
                        ac.finalHttp.post(URL.DELETE_DATE, ap,
                                new JsonHttpResponseHandler() {
                                    @Override
                                    public void onStart() {
                                        customShowDialog("正在结束约会");
                                    }

                                    @Override
                                    public void onSuccess(JSONObject jo) {
                                        customDismissDialog();
                                        try {
                                            int status = jo.getInt(URL.STATUS);
                                            switch (status) {
                                                case 0:
                                                    showCustomToast("约会结束成功");
                                                    Intent i = new Intent();
                                                    i.putExtra("bean", bean);
                                                    setResult(2, i);
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

    ;

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
        new AlertDialog.Builder(DateDetailActivity.this)
                .setTitle("提示")
                .setMessage("您确定要举报该约会吗？")
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

    ;

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
                                ap.put("ptype", 2 + "");
                                ap.put("tid", bean.getDtid());
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

        ;

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

        ;

        public void onFailure() {
            customDismissDialog();
            showIntentErrorToast();
        }

        ;
    };

    void remove_date() {
        new AlertDialog.Builder(this).setTitle("提示").setMessage("您确定要放弃这次约会吗？")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RequestParams ap = getAjaxParams();
                        ap.put("dtid", bean.getDtid() + "");
                        ap.put("nickname", getHelper().getUserInfo().getNickname() + "");
                        ac.finalHttp.post(URL.CANCLEAPPLY, ap,
                                new JsonHttpResponseHandler() {
                                    @Override
                                    public void onStart() {
                                    }

                                    @Override
                                    public void onSuccess(JSONObject jo) {
                                        try {
                                            int status = jo.getInt(URL.STATUS);
                                            switch (status) {
                                                case 0:
                                                    showCustomToast("操作成功");
                                                    bean.setApplynum(bean.getApplynum() - 1);
                                                    bean.setIsapply(0);
                                                    join_tv.setText("报名");
                                                    join.setSelected(false);
                                                    setHeadSource(bean);
                                                    setData(bean);
                                                    adapter.setList(bean.getCommlist());
                                                    adapter.notifyDataSetChanged();
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

    @Click(R.id.left_icon)
    public void finishAcy() {
        scrollToFinishActivity();
    }

    ;

    @Click
    void manage() {
        DateApplyManageActivity_.intent(this).bean(bean).startForResult(MANAGE);
    }

    @OnActivityResult(MANAGE)
    void onManageResult(int result) {
        if (result == 1) {
            bean.setDtstate(1);
        }
    }

    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

}
