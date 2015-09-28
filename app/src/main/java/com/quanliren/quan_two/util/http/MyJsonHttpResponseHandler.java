package com.quanliren.quan_two.util.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.quanliren.quan_two.activity.user.LoginActivity_;
import com.quanliren.quan_two.application.AM;
import com.quanliren.quan_two.bean.LoginUser;
import com.quanliren.quan_two.db.DBHelper;
import com.quanliren.quan_two.util.LogUtil;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import org.apache.http.Header;

/**
 * Created by mr.shen on 2015/5/16.
 */
public abstract class MyJsonHttpResponseHandler extends JsonHttpResponseHandler {

    String progress = null;
    Context context = null;
    ProgressDialog dialog = null;
    DialogInterface.OnCancelListener listener;

    public MyJsonHttpResponseHandler(Context context) {
        this(context, null);
    }

    public MyJsonHttpResponseHandler(Context context, String progress) {
        this.context = context;
        this.progress = progress;
    }

    public MyJsonHttpResponseHandler(Context context, String progress, DialogInterface.OnCancelListener listener) {
        this.context = context;
        this.progress = progress;
        this.listener = listener;
    }

    @Override
    public void onStart() {
        if (progress != null && context != null) {
            dialog = Util.progress(context, progress);
            if (listener != null) {
                dialog.setOnCancelListener(listener);
            }
        }
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        super.onSuccess(statusCode, headers, response);
        LogUtil.d(response.toString());
        onSuccess(response);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        super.onFailure(statusCode, headers, responseString, throwable);
        onFailure();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        onFailure();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        onFailure();
    }

    public void onSuccess(JSONObject jo) {
        try {
            if (jo.has(URL.STATUS)) {
                int retCode = jo.optInt(URL.STATUS);
                switch (retCode) {
                    case 0:
                        onSuccessRetCode(jo);
                        break;
                    case 1:
                        onFailRetCode(jo);
                        break;
                    case 2:
                        onFailRetCode(jo);
                        DBHelper.clearTable(context, LoginUser.class);
                        AM.getActivityManager().popAllActivity();
                        LoginActivity_.intent(context).start();
                        break;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            onFailure();
        }
    }

    public abstract void onSuccessRetCode(JSONObject jo) throws Throwable;

    public void onFailRetCode(JSONObject jo) {
        if (jo.has(URL.RESPONSE) && context != null) {
            if (jo.optJSONObject(URL.RESPONSE).has(URL.INFO)) {
                Util.toast(context, jo.optJSONObject(URL.RESPONSE).optString(
                        URL.INFO));
            }
        }
    }

    public void onFailure() {
        Util.toastError(context);
    }

    @Override
    public void onFinish() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
