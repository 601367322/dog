package com.quanliren.quan_two.fragment.message;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.quanliren.quan_two.activity.PropertiesActivity;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.user.ChatActivity_;
import com.quanliren.quan_two.adapter.LeaveMessageAdapter;
import com.quanliren.quan_two.bean.ChatListBean;
import com.quanliren.quan_two.bean.LoginUser;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.db.ChatListDao;
import com.quanliren.quan_two.db.MessageDao;
import com.quanliren.quan_two.fragment.base.MenuFragmentBase;
import com.quanliren.quan_two.fragment.impl.LoaderImpl;
import com.quanliren.quan_two.pull.XListView;
import com.quanliren.quan_two.pull.swipe.SwipeRefreshLayout;
import com.quanliren.quan_two.util.LogUtil;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EFragment
public class MyLeaveMessageFragment extends MenuFragmentBase implements XListView.IXListViewListener, SwipeRefreshLayout.OnRefreshListener, LoaderImpl {

    public static final String TAG = "MyLeaveMessageActivity";
    public static final String REFEREMSGCOUNT = "com.quanliren.quan_two.MyLeaveMessageActivity.REFEREMSGCOUNT";
    public static final String ADDMSG = "com.quanliren.quan_two.MyLeaveMessageActivity.ADDMSG";
    public static final String DELMSG = "com.quanliren.quan_two.MyLeaveMessageActivity.DELMSG";
    @ViewById
    XListView listview;
    @ViewById
    SwipeRefreshLayout swipe;

    ChatListDao chatListDao;

    MessageDao messageDao;

    LeaveMessageAdapter adapter;
    int p = 0;
    LoginUser user;
    @ViewById
    View empty_message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        user = getHelper().getUser();

        messageDao = MessageDao.getInstance(ac);

        chatListDao = ChatListDao.getInstance(ac);
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.my_leavemessage_list, null);
        } else {
            ViewParent parent = view.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(view);
            }
        }
        return view;
    }

    public void initAdapter() {
        swipe.setOnRefreshListener(this);
        adapter = new LeaveMessageAdapter(getActivity());
        listview.setAdapter(adapter);
        listview.setXListViewListener(this);
        listview.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                ChatListBean mlb = adapter.getItem(position);
                deleteChatListAlert(mlb);
                return true;
            }
        });
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                ChatListBean bean = adapter.getItem(position);
                User user = new User(bean.getFriendid(), bean.getUserlogo(), bean.getNickname());
                ChatActivity_.intent(MyLeaveMessageFragment.this).friend(user).start();
            }
        });
        swipeRefresh();
    }

    @Receiver(actions = {MyLeaveMessageFragment.DELMSG})
    public void receive(Intent i) {

        String action = i.getAction();
        if (action.equals(MyLeaveMessageFragment.DELMSG)) {
            ChatListBean bean = (ChatListBean) i.getSerializableExtra("bean");
            LogUtil.d("================cbbean", bean.getContent());
            try {
                chatListDao.deleteChatListAll(bean);

                int position = adapter.getList().indexOf(bean);

                adapter.remove(position);
                adapter.notifyDataSetChanged();

                Intent intent = new Intent(PropertiesActivity.PROPERTIESACTIVITY_NEWMESSAGE);
                getActivity().sendBroadcast(intent);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @UiThread(delay = 200)
    public void swipeRefresh() {
        swipe.setRefreshing(true);
    }

    @Override
    public void onRefresh() {
        findList();
    }

    @Background
    void findList() {
        try {
            final List<ChatListBean> list = chatListDao.getChatList(user.getId());
            if (list != null && list.size() > 0) {
                for (ChatListBean c : list) {
                    c.setMsgCount(messageDao.getUnReadCount(user.getId(), c.getFriendid()));
                }
            }
            notifyData(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @UiThread
    void notifyData(List<ChatListBean> list) {
        if (list != null && list.size() > 0) {
            empty_message.setVisibility(View.GONE);
            adapter.setList(list);
        } else {
            empty_message.setVisibility(View.VISIBLE);
        }

        if (adapter.getList() != null && adapter.getList().size() > 0) {
            empty_message.setVisibility(View.GONE);
        } else {
            empty_message.setVisibility(View.VISIBLE);
        }
        adapter.notifyDataSetChanged();
        swipe.setRefreshing(false);
    }

    @Override
    public void onLoadMore() {
    }

    public void deleteChatListAlert(final ChatListBean bean) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity()).setItems(new String[]{"删除"}, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("你确定要删除这条记录吗？").setPositiveButton("确定", new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    deleteChatListBeanBackGround(bean);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).setNegativeButton("取消", new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        }).create().show();
                        break;
                }
            }
        }).create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    @Background
    public void deleteChatListBeanBackGround(ChatListBean bean){
        chatListDao.deleteChatListAll(bean);
        deleteChatListUI(bean);
    }

    @UiThread
    public void deleteChatListUI(ChatListBean bean){
        final int position = adapter.getList().indexOf(bean);

        adapter.remove(position);
        adapter.notifyDataSetChanged();

        if (adapter.getList() != null && adapter.getList().size() > 0) {
            empty_message.setVisibility(View.GONE);
        } else {
            empty_message.setVisibility(View.VISIBLE);
        }

        Intent intent = new Intent(PropertiesActivity.PROPERTIESACTIVITY_NEWMESSAGE);
        getActivity().sendBroadcast(intent);
    }

    @Override
    public void refresh() {
        if (getActivity() != null && init.compareAndSet(false, true)) {
            empty_message.setVisibility(View.GONE);
            initAdapter();
        }
    }

    @Receiver(actions = {REFEREMSGCOUNT, ADDMSG})
    public void receiver(Intent i) {
        empty_message.setVisibility(View.GONE);
        try {
            String action = i.getAction();
            if (action.equals(REFEREMSGCOUNT)) {
                if (adapter == null) {
                    return;
                }
                List<ChatListBean> list = adapter.getList();
                for (ChatListBean messageListBean : list) {
                    if (messageListBean.getFriendid().equals(i.getStringExtra("id"))) {
                        messageListBean.setMsgCount(messageDao.getUnReadCount(user.getId(), messageListBean.getFriendid()));
                    }
                }
                adapter.notifyDataSetChanged();
            } else if (action.equals(ADDMSG)) {
                ChatListBean bean = (ChatListBean) i.getExtras().getSerializable("bean");

                bean.setMsgCount(messageDao.getUnReadCount(user.getId(), bean.getFriendid()));

                ChatListBean temp = null;
                List<ChatListBean> list = adapter.getList();
                for (ChatListBean messageListBean : list) {
                    if (messageListBean.getFriendid().equals(bean.getFriendid())) {
                        temp = messageListBean;
                    }
                }
                if (temp != null) {
                    adapter.remove(temp);
                }
                adapter.add(0, bean);
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
