package com.quanliren.quan_two.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.quanliren.quan_two.bean.DfMessage;
import com.quanliren.quan_two.db.DBHelper;
import com.quanliren.quan_two.util.BroadcastUtil;
import com.quanliren.quan_two.util.EmojiFilter;
import com.quanliren.quan_two.util.LogUtil;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

public class QuanPushService extends Service {
    private static final String TAG = "QuanPushService";
    private static final long KEEP_ALIVE_INTERVAL = 1000 * 15;

    private ConnectionThread mConnection;

    Dao<DfMessage, Integer> messageDao;

    public IBinder onBind(Intent intent) {
        // Log.d(TAG, "IBinder onBind(Intent intent)");
        return stub;
    }

    public void onCreate() {
        super.onCreate();
        try {
            messageDao = OpenHelperManager.getHelper(this, DBHelper.class)
                    .getDao(DfMessage.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(broadcast = new ChatBroadcast(), filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            // Log.d(TAG, intent.getAction());
            if (intent.getAction().equals(BroadcastUtil.ACTION_CONNECT)) {
                startConnection();
            } else if (intent.getAction()
                    .equals(BroadcastUtil.ACTION_RECONNECT)) {
                reconnectIfNecessary();
            } else if (intent.getAction()
                    .equals(BroadcastUtil.ACTION_KEEPALIVE)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    startKeepAlives();
                }
                keepAlive();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    ChatBroadcast broadcast;

    class ChatBroadcast extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                Intent i = new Intent(BroadcastUtil.ACTION_CHECKCONNECT);
                sendBroadcast(i);
            }
        }
    }

    public void onDestroy() {
        LogUtil.d(TAG, "onDestroy");
        super.onDestroy();
        unregisterReceiver(broadcast);
        stopConnection();
        stopKeepAlives();
    }

    private IQuanPushService.Stub stub = new IQuanPushService.Stub() {

        @Override
        public void sendMessage(String str) throws RemoteException {
            if (mConnection != null)
                mConnection.write(str);
        }

        @Override
        public boolean getServerSocket() throws RemoteException {
            return mConnection == null ? false : true;
        }

        @Override
        public void closeAll() throws RemoteException {
            // Log.d(TAG, "closeAll()");
            stopConnection();
            cancleAllAlarm();
        }
    };

    public void cancleAllAlarm() {
        Util.canalAlarm(getApplicationContext(),
                BroadcastUtil.ACTION_CHECKCONNECT);
    }

    private void startKeepAlives() {
        // Util.setAlarmTime(this, System.currentTimeMillis() +
        // KEEP_ALIVE_INTERVAL,BroadcastUtil.ACTION_KEEPALIVE,(int)(KEEP_ALIVE_INTERVAL));

        Intent i = new Intent(BroadcastUtil.ACTION_KEEPALIVE);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmMgr.setWindow(AlarmManager.RTC, System.currentTimeMillis()
                    + KEEP_ALIVE_INTERVAL, KEEP_ALIVE_INTERVAL, pi);
        } else {
            alarmMgr.setRepeating(AlarmManager.RTC, System.currentTimeMillis()
                    + KEEP_ALIVE_INTERVAL, KEEP_ALIVE_INTERVAL, pi);
        }

    }

    private void stopKeepAlives() {
        // Util.canalAlarm(getApplicationContext(),
        // BroadcastUtil.ACTION_KEEPALIVE);
        Intent i = new Intent(BroadcastUtil.ACTION_KEEPALIVE);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        AlarmManager alarmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmMgr.cancel(pi);
    }

    public class ConnectionThread extends Thread {
        private final Socket mSocket;
        private final String mHost;
        private final int mPort;
        private ClientHandlerWord handler;

        public ConnectionThread(String host, int port) {
            mHost = host;
            mPort = port;
            handler = new ClientHandlerWord(getApplicationContext());
            mSocket = new Socket();
        }

        public boolean isConnected() {
            return mSocket.isConnected();
        }

        public void run() {
            Socket s = mSocket;

            try {
                s.connect(new InetSocketAddress(mHost, mPort), 20000);

                startKeepAlives();

                BufferedReader in = new BufferedReader(new InputStreamReader(
                        s.getInputStream()));

                handler.sessionConnected(mConnection);

                String str = null;
                while ((str = in.readLine()) != null) {
                    Log.d(TAG, "receive------------" + str);
                    if (str.equals("#") || str.equals("*")) {
                        continue;
                    }
                    try {
                        handler.messageReceived(mConnection, str);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                Log.d(TAG, "Unexpected I/O error: " + e.toString());
            } finally {
                stopKeepAlives();
                try {
                    if (!s.isClosed())
                        s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mConnection = null;
                Log.d(TAG, "DisConnect");
            }
        }

        public void sendKeepAlive() throws IOException {
            new writeThread("#").start();
        }

        class writeThread extends Thread {
            private String str;

            public writeThread(String str) {
                // TODO Auto-generated constructor stub
                this.str = str;
            }

            @Override
            public void run() {
                threadWrite(str);
            }
        }

        public void write(String str) {
            new writeThread(str).start();
        }

        public synchronized void threadWrite(String str) {
            try {
                str = EmojiFilter.filterEmoji(str);
                Socket s = mSocket;
                LogUtil.d(str.getBytes().length + "-------" + str);

                try {
                    JSONObject jo = new JSONObject(str);
                    if (jo.has(SocketManage.ORDER) && jo.getString(SocketManage.ORDER).equals(SocketManage.ORDER_SENDMESSAGE)) {
                        String messageId = jo
                                .getString(SocketManage.MESSAGE_ID);
                        List<DfMessage> msgs = messageDao.queryForEq(SocketManage.MESSAGE_ID,
                                messageId);
                        if (msgs != null && msgs.size() > 0) {
                            DfMessage temp = msgs.get(0);
                            if (temp.getDownload() == SocketManage.D_downloaded) {
                                return;
                            }
                        }
                    }
                } catch (Exception e) {
                }

                s.getOutputStream().write((str + "\n").getBytes());

            } catch (Exception e) {
                e.printStackTrace();
                if (mConnection != null) {
                    mConnection.abort();
                    mConnection = null;
                }
            }
        }

        public void abort() {
            try {
                mSocket.shutdownOutput();
                mSocket.shutdownInput();
                mSocket.close();
            } catch (IOException e) {
            }

            while (true) {
                try {
                    join();
                    break;
                } catch (InterruptedException e) {
                }
            }
        }
    }

    private synchronized void startConnection() {

        stopConnection();

        mConnection = new ConnectionThread(URL.IP, URL.SOCKET);
        mConnection.start();
    }

    private synchronized void stopConnection() {

        if (mConnection != null) {
            mConnection.abort();
            mConnection = null;
        }
    }

    private synchronized void reconnectIfNecessary() {
        if (mConnection == null) {
            mConnection = new ConnectionThread(URL.IP, URL.SOCKET);
            mConnection.start();
        }
    }

    private synchronized void keepAlive() {
        try {
            if (mConnection != null)
                mConnection.sendKeepAlive();
            else
                reconnectIfNecessary();
        } catch (IOException e) {
        }
    }

}
