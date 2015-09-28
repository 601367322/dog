package com.quanliren.quan_two.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.quanliren.quan_two.bean.CacheBean;
import com.quanliren.quan_two.bean.ChatListBean;
import com.quanliren.quan_two.bean.DfMessage;
import com.quanliren.quan_two.bean.DongTaiBeanTable;
import com.quanliren.quan_two.bean.FilterBean;
import com.quanliren.quan_two.bean.LoginUser;
import com.quanliren.quan_two.bean.MoreLoginUser;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.bean.UserTable;
import com.quanliren.quan_two.bean.VersionBean;
import com.quanliren.quan_two.bean.ZhenXinHuaTable;
import com.quanliren.quan_two.bean.emoticon.EmoticonActivityListBean.EmoticonZip;
import com.quanliren.quan_two.util.LogUtil;

import java.sql.SQLException;
import java.util.List;

public class DBHelper extends OrmLiteSqliteOpenHelper {


    private static final String DATABASE_NAME = "toutou.db";
    private static final int DATABASE_VERSION = 8;

    private RuntimeExceptionDao<LoginUser, String> loginUserDao = null;
    private RuntimeExceptionDao<UserTable, String> userTableDao = null;
    private RuntimeExceptionDao<CacheBean, String> cacheBeanDao = null;
    private RuntimeExceptionDao<DfMessage, String> messageDao = null;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    /**
     * 创建SQLite数据库
     */
    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, LoginUser.class);
            TableUtils.createTable(connectionSource, UserTable.class);
            TableUtils.createTable(connectionSource, DfMessage.class);
            TableUtils.createTable(connectionSource, CacheBean.class);
            TableUtils.createTable(connectionSource, ChatListBean.class);
            TableUtils.createTable(connectionSource, DongTaiBeanTable.class);
            TableUtils.createTable(connectionSource, MoreLoginUser.class);
            TableUtils.createTable(connectionSource, VersionBean.class);
            TableUtils.createTable(connectionSource, EmoticonZip.class);
            TableUtils.createTable(connectionSource, FilterBean.class);
            TableUtils.createTable(connectionSource, ZhenXinHuaTable.class);
        } catch (SQLException e) {
            LogUtil.e(DBHelper.class.getName(), "Unable to create datbases");
        }
    }

    /**
     * 更新SQLite数据库
     */
    @Override
    public void onUpgrade(
            SQLiteDatabase sqliteDatabase,
            ConnectionSource connectionSource,
            int oldVer,
            int newVer) {
        try {
            switch (oldVer) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    updateDatabase_7();
                case 7:
                    updateDatabase_8(sqliteDatabase);
                    break;
            }//chmod 777 /data /data/data /data/data/com.quanliren.quan_two.activity/*
        } catch (SQLException e) {
            LogUtil.e(DBHelper.class.getName(),
                    "Unable to upgrade database from version " + oldVer + " to new "
                            + newVer);
        }
    }

    public void updateDatabase_7() throws SQLException {
        TableUtils.createTable(connectionSource, FilterBean.class);
    }

    public void updateDatabase_8(SQLiteDatabase db) throws SQLException {
        TableUtils.createTable(connectionSource, ZhenXinHuaTable.class);

        //更新消息表，去除user_id字段
        db.execSQL("CREATE TABLE temp (content VARCHAR , ctime VARCHAR , download INTEGER , id INTEGER PRIMARY KEY AUTOINCREMENT , isRead INTEGER , msgid VARCHAR , msgtype INTEGER , nickname VARCHAR , receiverUid VARCHAR , sendUid VARCHAR , timel INTEGER , userid VARCHAR , userlogo VARCHAR , z_msgid VARCHAR , play_state INTEGER );");
        db.execSQL("INSERT INTO temp select content,ctime,download,id,isRead,msgid,msgtype,nickname,receiverUid,sendUid,timel,userid,userlogo,'',0 from DfMessage;");
        db.execSQL("drop table DfMessage;");
        db.execSQL("alter table temp rename to DfMessage;");
        db.execSQL("CREATE INDEX DfMessage_ctime_idx ON DfMessage ( ctime );");
        db.execSQL("CREATE INDEX DfMessage_download_idx ON DfMessage ( download );");
        db.execSQL("CREATE INDEX DfMessage_isRead_idx ON DfMessage ( isRead );");
        db.execSQL("CREATE INDEX DfMessage_msgid_idx ON DfMessage ( msgid );");
        db.execSQL("CREATE INDEX DfMessage_msgtype_idx ON DfMessage ( msgtype );");
        db.execSQL("CREATE INDEX DfMessage_receiverUid_idx ON DfMessage ( receiverUid );");
        db.execSQL("CREATE INDEX DfMessage_sendUid_idx ON DfMessage ( sendUid );");
        db.execSQL("CREATE INDEX DfMessage_userid_idx ON DfMessage ( userid );");
    }

    public RuntimeExceptionDao<LoginUser, String> getLoginUserDao() {
        try {
            if (loginUserDao == null) {
                loginUserDao = getRuntimeExceptionDao(LoginUser.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginUserDao;
    }

    public RuntimeExceptionDao<DfMessage, String> getDfMessageDao() {
        try {
            if (messageDao == null) {
                messageDao = getRuntimeExceptionDao(DfMessage.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageDao;
    }


    public RuntimeExceptionDao<UserTable, String> getUserTableDao() {
        try {
            if (userTableDao == null) {
                userTableDao = getRuntimeExceptionDao(UserTable.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userTableDao;
    }

    public LoginUser getUser() {
        List<LoginUser> users = getLoginUserDao().queryForAll();
        if (users == null || users.size() == 0) {
            return null;
        } else {
            return users.get(0);
        }
    }

    public User getUserInfo() {
        LoginUser u = getUser();
        UserTable user = null;
        if (u != null) {
            user = getUserTableDao().queryForId(u.getId());
            if (user != null) {
                return user.getUser();
            }
        }
        return null;
    }

    public static void clearTable(Context context, Class clazz) {
        try {
            ConnectionSource source = OpenHelperManager.getHelper(context, DBHelper.class).getConnectionSource();
            TableUtils.clearTable(source, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <D extends RuntimeExceptionDao<T, ?>, T> D getDao_(Context context, Class<T> clazz) {
        return OpenHelperManager.getHelper(context, DBHelper.class).getRuntimeExceptionDao(clazz);
    }
}
