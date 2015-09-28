package com.quanliren.quan_two.db;

import android.content.Context;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.quanliren.quan_two.bean.ChatListBean;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shen on 2015/9/10.
 */
public class ChatListDao extends BaseDao<ChatListBean, Integer> {

    private static ChatListDao instance;

    MessageDao messageDao;

    public static synchronized ChatListDao getInstance(Context context) {
        if (instance == null) {
            instance = new ChatListDao(context.getApplicationContext());
        }
        return instance;
    }

    public ChatListDao(Context context) {
        super(context);
    }

    public void deleteChatList(ChatListBean cb) {
        deleteChatList(cb.getUserid(), cb.getFriendid());
    }

    public void deleteChatList(String userId, String friendId) {
        //更新聊天列表
        try {
            DeleteBuilder<ChatListBean, Integer> db = dao.deleteBuilder();
            db.where().eq("userid", userId).and().eq("friendid", friendId);
            dao.delete(db.prepare());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteChatListAll(ChatListBean cb) {
        deleteChatList(cb);
        if (messageDao == null) {
            messageDao = MessageDao.getInstance(context);
        }
        messageDao.deleteAllMessageByFriendId(cb.getUserid(), cb.getFriendid());
    }

    public List<ChatListBean> getChatList(String userId) {
        try {
            return dao.query(dao.queryBuilder().orderBy("id", false).where().eq("userid", userId).prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
