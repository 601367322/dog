package com.quanliren.quan_two.db;

import android.content.Context;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.quanliren.quan_two.bean.ChatListBean;
import com.quanliren.quan_two.bean.DfMessage;
import com.quanliren.quan_two.bean.ZhenXinHuaTable;
import com.quanliren.quan_two.util.FileUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Shen on 2015/9/10.
 */
public class MessageDao extends BaseDao<DfMessage, Integer> {

    ChatListDao chatlistDao;
    ZhenXinHuaDao zhenXinHuaDao;

    private static MessageDao instance;

    public static synchronized MessageDao getInstance(Context context) {
        if (instance == null) {
            instance = new MessageDao(context.getApplicationContext());
        }
        return instance;
    }

    public MessageDao(Context context) {
        super(context);

        chatlistDao = ChatListDao.getInstance(context);
        zhenXinHuaDao = ZhenXinHuaDao.getInstance(context);
    }

    public void saveMessage(DfMessage msg) {
        try {
            dao.create(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveMessage(DfMessage msg, ChatListBean cb) {
        try {
            dao.create(msg);

            //更新聊天列表
            chatlistDao.deleteChatList(cb);
            chatlistDao.create(cb);

            //真心话/大冒险
            switch (msg.getMsgtype()) {
                case DfMessage.ZHENXINHUA:
                case DfMessage.DAMAOXIAN:
                    ZhenXinHuaTable zxh = new ZhenXinHuaTable(msg.getMsgid(), msg.getSendUid(), msg.getReceiverUid(), "", "", new Date().getTime(), msg.getUserid(), msg.getMsgtype());
                    zhenXinHuaDao.create(zxh);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<DfMessage> getMsgList(String userId, String friendId, int maxid) {
        QueryBuilder<DfMessage, Integer> qb = null;
        try {
            qb = dao.queryBuilder();
            Where<DfMessage, Integer> where = qb.where();
            where.and(
                    where.eq("userid", userId),
                    where.or(where.eq("sendUid", friendId),
                            where.eq("receiverUid", friendId)));
            if (maxid > -1) {
                where.and().lt("id", maxid);
            }
            qb.limit(15l);
            qb.orderBy("id", false);
            return dao.query(qb.prepare());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void updateMsgReaded(String ids) {
        try {
            dao.update(dao.updateBuilder()
                    .updateColumnValue("isRead", 1).where()
                    .in("id", ids).prepareUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getUnReadCount(String userId, String friendId) {
        try {
            QueryBuilder<DfMessage, Integer> qb = dao.queryBuilder();
            Where where = qb.where();
            where.and(where.eq("userid", userId), where.eq("receiverUid", userId), where.eq("sendUid", friendId), where.eq("isRead", 0));
            return (int) qb.countOf();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void deleteAllMessageByFriendId(String userId, String friendId) {
        try {
            QueryBuilder<DfMessage, Integer> queryBuilder = dao.queryBuilder();
            Where<DfMessage, Integer> queryWhere = queryBuilder.where();
            queryWhere.and(queryWhere.eq("userid", userId),queryWhere.or(queryWhere.eq("sendUid", friendId), queryWhere.eq("receiverUid", friendId)));
            List<DfMessage> msgs = dao.query(queryBuilder.prepare());
            for (int i = 0; i < msgs.size(); i++) {
                //删除对应文件
                switch (msgs.get(i).getMsgtype()) {
                    case DfMessage.IMAGE:
                    case DfMessage.VOICE:
                        FileUtil.deleteFile(msgs.get(i).getContent());
                        break;
                    case DfMessage.VIDEO:
                        String[] str = msgs.get(i).getContent().split(",");
                        for (int j = 0; j < str.length; j++) {
                            FileUtil.deleteFile(str[j]);
                        }
                        break;
                }
            }
            dao.delete(msgs);

            zhenXinHuaDao.deleteAllMessageByFriendId(userId, friendId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(DfMessage msg, boolean isDeleteFile) {
        super.delete(msg);
        zhenXinHuaDao.deleteZhen(msg.getMsgid());//删除真心话记录
        try {
            //删除真心话答复记录
            DeleteBuilder<DfMessage, Integer> db = dao.deleteBuilder();
            final Where<DfMessage, Integer> where = db.where();
            where.eq("userid", msg.getUserid()).and().eq("z_msgid", msg.getMsgid());
            dao.delete(db.prepare());

            //删除对应文件
            if (isDeleteFile) {
                switch (msg.getMsgtype()) {
                    case DfMessage.IMAGE:
                    case DfMessage.VOICE:
                        FileUtil.deleteFile(msg.getContent());
                        break;
                    case DfMessage.VIDEO:
                        String[] str = msg.getContent().split(",");
                        for (int i = 0; i < str.length; i++) {
                            FileUtil.deleteFile(str[i]);
                        }
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DfMessage getMessageByMsgId(String msgId) {
        try {
            List<DfMessage> list = dao.queryForEq("msgid", msgId);
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public DfMessage getLastMessage(String userId, String friendId) {
        try {
            QueryBuilder<DfMessage, Integer> queryBuilder = dao.queryBuilder();
            Where<DfMessage, Integer> queryWhere = queryBuilder.where();
            queryWhere.and(queryWhere.eq("userid", userId),queryWhere.or(queryWhere.eq("sendUid", friendId), queryWhere.eq("receiverUid", friendId)));
            queryBuilder.orderBy("id", false);
            queryBuilder.limit(1l);
            return dao.queryForFirst(queryBuilder.prepare());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
