package com.quanliren.quan_two.db;

import android.content.Context;

import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.quanliren.quan_two.bean.ZhenXinHuaTable;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shen on 2015/9/10.
 */
public class ZhenXinHuaDao extends BaseDao<ZhenXinHuaTable, Integer> {

    public ZhenXinHuaDao(Context context) {
        super(context);
    }

    private static ZhenXinHuaDao instance;

    public static synchronized ZhenXinHuaDao getInstance(Context context) {
        if (instance == null) {
            instance = new ZhenXinHuaDao(context.getApplicationContext());
        }
        return instance;
    }

    public ZhenXinHuaTable getZhenByMsgId(String userId, String msgId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("msgId", msgId);
        List<ZhenXinHuaTable> list = dao.queryForFieldValues(map);
        return (list == null || list.size() == 0) ? null : list.get(0);
    }

    public ZhenXinHuaTable getZhenByMsgId(String userId, String msgId, int state) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("msgId", msgId);
        map.put("state", state);
        List<ZhenXinHuaTable> list = dao.queryForFieldValues(map);
        return (list == null || list.size() == 0) ? null : list.get(0);
    }

    public ZhenXinHuaTable getZhenLastByUserId(String userId, String friendId, int msgType) {
        try {
            QueryBuilder builder = dao.queryBuilder();
            Where where = builder.where();
            where.and(where.eq("userId", userId), where.eq("msgType", msgType), where.or(where.eq("sendUid", friendId), where.eq("receiverUid", friendId)));
            builder.orderBy("id", false);
            return dao.queryForFirst(builder.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteAllMessageByFriendId(String userId, String friendId) {
        try {
            DeleteBuilder<ZhenXinHuaTable, Integer> db = dao.deleteBuilder();
            final Where<ZhenXinHuaTable, Integer> where = db.where();
            where.eq("userId", userId).and().or(where.eq("sendUid", friendId), where.eq("receiverUid", friendId));
            dao.delete(db.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setContent(ZhenXinHuaTable zt, String userid, String content) {
        if (zt != null) {
            if (zt.getSendUid().equals(userid)) {
                zt.setSendContent(content);
            } else {
                zt.setReceiverContent(content);
            }
            dao.update(zt);
        }
    }

    public void deleteZhen(String msgId) {
        try {
            DeleteBuilder<ZhenXinHuaTable, Integer> db = dao.deleteBuilder();
            final Where<ZhenXinHuaTable, Integer> where = db.where();
            where.eq("msgId", msgId);
            dao.delete(db.prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
