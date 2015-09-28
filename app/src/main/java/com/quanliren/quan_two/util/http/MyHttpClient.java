package com.quanliren.quan_two.util.http;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;
import com.quanliren.quan_two.util.LogUtil;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

/**
 * Created by Shen on 2015/8/3.
 */
public class MyHttpClient extends AsyncHttpClient {

    @Override
    public RequestHandle post(String url, ResponseHandlerInterface responseHandler) {
        LogUtil.d(url);
        return super.post(url, responseHandler);
    }

    @Override
    public RequestHandle post(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler) {
        LogUtil.d(url + "?" + (params != null ? params.toString() : ""));
        return super.post(context, url, params, responseHandler);
    }

    @Override
    public RequestHandle post(Context context, String url, Header[] headers, HttpEntity entity, String contentType, ResponseHandlerInterface responseHandler) {
        LogUtil.d(url);
        return super.post(context, url, headers, entity, contentType, responseHandler);
    }

    @Override
    public RequestHandle post(Context context, String url, Header[] headers, RequestParams params, String contentType, ResponseHandlerInterface responseHandler) {
        LogUtil.d(url + "?" + (params != null ? params.toString() : ""));
        return super.post(context, url, headers, params, contentType, responseHandler);
    }

    @Override
    public RequestHandle get(String url, ResponseHandlerInterface responseHandler) {
        LogUtil.d(url);
        return super.get(url, responseHandler);
    }

    @Override
    public RequestHandle get(String url, RequestParams params, ResponseHandlerInterface responseHandler) {
        LogUtil.d(url + "?" + (params != null ? params.toString() : ""));
        return super.get(url, params, responseHandler);
    }

    @Override
    public RequestHandle get(Context context, String url, ResponseHandlerInterface responseHandler) {
        LogUtil.d(url);
        return super.get(context, url, responseHandler);
    }

    @Override
    public RequestHandle get(Context context, String url, RequestParams params, ResponseHandlerInterface responseHandler) {
        LogUtil.d(url + "?" + (params != null ? params.toString() : ""));
        return super.get(context, url, params, responseHandler);
    }

    @Override
    public RequestHandle get(Context context, String url, Header[] headers, RequestParams params, ResponseHandlerInterface responseHandler) {
        LogUtil.d(url + "?" + (params != null ? params.toString() : ""));
        return super.get(context, url, headers, params, responseHandler);
    }

    @Override
    public RequestHandle get(Context context, String url, HttpEntity entity, String contentType, ResponseHandlerInterface responseHandler) {
        LogUtil.d(url);
        return super.get(context, url, entity, contentType, responseHandler);
    }
}
