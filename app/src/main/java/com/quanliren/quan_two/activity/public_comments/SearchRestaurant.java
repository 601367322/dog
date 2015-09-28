package com.quanliren.quan_two.activity.public_comments;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.util.LogUtil;
import com.quanliren.quan_two.util.URL;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.shop_select)
public class SearchRestaurant extends BaseActivity {
    @ViewById
    TextView btn_title_city;
    @ViewById
    GridView grid_hot_regions;
    @ViewById
    GridView grid_cuisine;

    private static final int CITYLIST = 2;
    private String city="北京";
    private String category="美食";
    private List<String> neighborhoodList=new ArrayList<String>();
    private List<String> categoryList=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getNeighborhoodsData();
        getCategoriesData();
    }
    @AfterViews
    void initView(){
        grid_hot_regions.setAdapter(new MyAdapter(this, neighborhoodList));
        grid_cuisine.setAdapter(new MyAdapter(this, categoryList));
    }
    void getNeighborhoodsData(){
        String apiUrl = "http://api.dianping.com/v1/metadata/get_regions_with_businesses";
        String appKey = URL.appKey;
        String secret = URL.secret;

        Map<Object, Object> paramMap = new HashMap<Object, Object>();
        paramMap.put("city", city);
        paramMap.put("format", "json");
        String requestResult = DemoApiTool.requestApi(this,apiUrl, appKey, secret, paramMap);
        LogUtil.d("===========requestResult",requestResult);
        try {
            JSONObject json=new JSONObject(requestResult);
            if("OK".equals(json.getString("status"))){
                JSONArray cities=json.getJSONArray("cities");
                for (int i=0;i<cities.length();i++){
                    if(city.equals(cities.getJSONObject(i).getString("city_name"))){
                        JSONArray districts=cities.getJSONObject(i).getJSONArray("districts");
                        for(int j=0;j<districts.length();j++){
                            ArrayList<String> neighborhoods= new Gson().fromJson(
                                    districts.getJSONObject(j).getString("neighborhoods"),
                                    new TypeToken<ArrayList<String>>() {
                                    }.getType());
                            neighborhoodList.addAll(neighborhoods);
                        }
                    }
                }
            }
        }catch (JSONException e){
        }
    }
    void getCategoriesData(){
        String apiUrl = "http://api.dianping.com/v1/metadata/get_categories_with_businesses";
        String appKey = URL.appKey;
        String secret = URL.secret;

        Map<Object, Object> paramMap = new HashMap<Object, Object>();
        paramMap.put("city", city);
        paramMap.put("format", "json");
        String requestResult = DemoApiTool.requestApi(this,apiUrl, appKey, secret, paramMap);
        try {
            JSONObject json=new JSONObject(requestResult);
            if("OK".equals(json.getString("status"))){
                JSONArray categories=json.getJSONArray("categories");
                for (int i=0;i<categories.length();i++){
                    if(category.equals(categories.getJSONObject(i).getString("category_name"))){
                        JSONArray subcategories=categories.getJSONObject(i).getJSONArray("subcategories");
                        for(int j=0;j<subcategories.length();j++){
                            String category_name=subcategories.getJSONObject(j).getString("category_name");
                            categoryList.add(category_name);
                        }
                    }
                }
            }
        }catch (JSONException e){
        }
    }

    class MyAdapter extends BaseAdapter{
        Context context;
        List<String> list;
        public MyAdapter(Context context, List<String> list) {
            this.context=context;
            this.list = list;
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv=new TextView(context);
            tv.setBackgroundColor(getResources().getColor(R.color.white));
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(16);
            tv.setText(list.get(position));
            return tv;
        }
    }
}
