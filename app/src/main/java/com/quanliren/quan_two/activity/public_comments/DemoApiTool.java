package com.quanliren.quan_two.activity.public_comments;

import android.content.Context;

import com.loopj.android.http.TextHttpResponseHandler;
import com.quanliren.quan_two.application.AppClass;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Android版本API工具
 * <p>
 *
 * @author : xiaopeng.li
 *         <p>
 * @version 1.0 2013-1-23
 * @since dianping-java-samples 1.0
 */
public class DemoApiTool
{

    /**
     * 获取请求字符串
     *
     * @param appKey
     * @param secret
     * @param paramMap
     * @return
     */
    public static String getQueryString(String appKey, String secret, Map<Object, Object> paramMap)
    {
        String sign = sign(appKey, secret, paramMap);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("appkey=").append(appKey).append("&sign=").append(sign);
        for (Entry<Object, Object> entry : paramMap.entrySet())
        {
            stringBuilder.append('&').append(entry.getKey()).append('=').append(entry.getValue());
        }
        String queryString = stringBuilder.toString();
        return queryString;
    }

    /**
     * 获取请求字符串，参数值进行UTF-8处理
     *
     * @param appKey
     * @param secret
     * @param paramMap
     * @return
     */
    public static String getUrlEncodedQueryString(String appKey, String secret, Map<Object, Object> paramMap)
    {
        String sign = sign(appKey, secret, paramMap);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("appkey=").append(appKey).append("&sign=").append(sign);
        for (Entry<Object, Object> entry : paramMap.entrySet())
        {
            try
            {
                stringBuilder.append('&').append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue().toString(),
                        "UTF-8"));
            }
            catch (UnsupportedEncodingException e)
            {
            }
        }
        String queryString = stringBuilder.toString();
        return queryString;
    }

    /**
     * 请求API
     *
     * @param apiUrl
     * @param appKey
     * @param secret
     * @param paramMap
     * @return
     */
    public static String requestApi(Context context,String apiUrl, String appKey, String secret, Map<Object, Object> paramMap)
    {

        String queryString = getQueryString(appKey, secret, paramMap);
        Temp temp = new Temp();
        ((AppClass) (context.getApplicationContext())).asyncHttpClient.get(apiUrl + "?" + queryString, new MyTextHttp(temp));
        return temp.str;

    }

    static class Temp{
        String str;
    }

    static class MyTextHttp extends TextHttpResponseHandler{

        Temp temp;

        public MyTextHttp(Temp temp){
            this.temp = temp;
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            temp.str = "";
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            temp.str = responseString;
        }
    }

    /**
     * 签名
     *
     * @param appKey
     * @param secret
     * @param paramMap
     * @return
     */
    public static String sign(String appKey, String secret, Map<Object, Object> paramMap)
    {
        // 参数名排序
        String[] keyArray = paramMap.keySet().toArray(new String[0]);
        Arrays.sort(keyArray);

        // 拼接参数
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(appKey);
        for (String key : keyArray)
        {
            stringBuilder.append(key).append(paramMap.get(key));
        }

        stringBuilder.append(secret);
        String codes = stringBuilder.toString();

        // SHA-1签名
        // For Android
        String sign = new String(Hex.encodeHex(DigestUtils.sha(codes))).toUpperCase();

        return sign;
    }
}