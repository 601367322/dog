package com.quanliren.quan_two.util.http;

import com.quanliren.quan_two.util.LogUtil;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by BingBing on 2014/9/17.
 */
public class JsonHttpResponseHandler extends com.loopj.android.http.JsonHttpResponseHandler {

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        if (response != null) {
            LogUtil.d(response.toString());
        }
        onSuccess(response);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        if(throwable!=null){
            LogUtil.d(throwable.toString());
        }
        onFailure();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        if(throwable!=null){
            LogUtil.d(throwable.toString());
        }
        onFailure();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        if(throwable!=null){
            LogUtil.d(throwable.toString());
        }
        onFailure();
    }

    public void onSuccess(JSONObject response) {

    }

    public void onFailure() {

    }
}
