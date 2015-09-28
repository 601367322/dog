package com.quanliren.quan_two.activity.public_comments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.location.GDLocation;
import com.quanliren.quan_two.activity.location.ILocationImpl;
import com.quanliren.quan_two.adapter.RestaAdapter;
import com.quanliren.quan_two.adapter.RestaurantListAdapter;
import com.quanliren.quan_two.bean.Area;
import com.quanliren.quan_two.bean.BusinessBean;
import com.quanliren.quan_two.pull.XListView;
import com.quanliren.quan_two.pull.swipe.SwipeRefreshLayout;
import com.quanliren.quan_two.util.LogUtil;
import com.quanliren.quan_two.util.URL;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@OptionsMenu(R.menu.bussiness_menu)
@EActivity(R.layout.restaurant_list)
public class RestaurantListActivity extends BaseActivity implements
        XListView.IXListViewListener, ILocationImpl,RestaurantListAdapter.BusinessListener,SwipeRefreshLayout.OnRefreshListener {

    @ViewById
    EditText search_business;
    @ViewById
    TextView all_business;
    @ViewById
    TextView search_restarent;
    @ViewById
    TextView all_food;
    @ViewById
    TextView default_sort;
    @ViewById
    SwipeRefreshLayout swipe;
    @ViewById
    XListView listview_bus;
    @ViewById
    ListView listview_1;
    @ViewById
    ListView listview_2;
    @ViewById
    ListView fillter_food;
    @ViewById
    ListView fillter_priority;
    @ViewById
    View fillter_lll;
    @ViewById
    View select_bg;
    @OptionsMenuItem(R.id.location)
    MenuItem location_menu;
    @Extra
    String category;
    String cityId;
    RestaurantListAdapter adapter;
    GDLocation location;
    private String city= "";
    private String region="";
    private String keyword="";
    int sort=1;
    private List<BusinessBean> businesses;

    List<String> districStr;
    List<String> neighborhoodStr;
    List<String> foodStr;
    List<City> cityList;
    List<Category> categoryList;

    String[] priorityArray={"默认排序","按星级排序","按评价排序","按环境排序","按服务排序","按点评数量排序","按距离排序","人均价格低排序","人均价格高排序"};
    List<String> priority;
    RestaAdapter district_ada;
    RestaAdapter neighborhood_ada;
    RestaAdapter priority_ada;
    RestaAdapter food_ada;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        location_menu.setTitle(ac.cs.getLocation().replace("市",""));
        return super.onCreateOptionsMenu(menu);
    }

    @AfterViews
    void initView(){
        swipe.setOnRefreshListener(this);
        cityId=ac.cs.getLocationID();
        setLeftIcon(R.drawable.actionbar_homeasup_indicator);
        setRightTitleTxt(ac.cs.getLocation().replace("市",""));
        city=ac.cs.getLocation().replace("市","");
        if("美食".equals(category)){
            all_food.setText("全部美食");
        }else if("电影".equals(category)){
            all_food.setText("电影");
        }
        priority= Arrays.asList(priorityArray);
        fillter_lll.setVisibility(View.GONE);
        fillter_food.setVisibility(View.GONE);
        districStr=new ArrayList<String>();
        neighborhoodStr=new ArrayList<String>();
        foodStr=new ArrayList<String>();
        district_ada=new RestaAdapter(this,districStr);
        neighborhood_ada=new RestaAdapter(this,neighborhoodStr);
        priority_ada=new RestaAdapter(this,priority);
        food_ada=new RestaAdapter(this,foodStr);
        listview_1.setAdapter(district_ada);
        listview_2.setAdapter(neighborhood_ada);
        fillter_food.setAdapter(food_ada);
        fillter_priority.setAdapter(priority_ada);
        location = new GDLocation(getApplicationContext(), this, false);
        businesses=new ArrayList<BusinessBean>();
        adapter=new RestaurantListAdapter(this,businesses,this);
        listview_bus.setAdapter(adapter);
        listview_bus.setXListViewListener(this);
//        Message msg2 = handler.obtainMessage();
//        msg2.what = 2;
//        msg2.sendToTarget();
//        Message msg3 = handler.obtainMessage();
//        msg3.what = 3;
//        msg3.sendToTarget();
        new Thread(){
            @Override
            public void run() {
                getDistrictsListData();
            }
        }.start();
        new Thread(){
            @Override
            public void run() {
                getCategoryListData();
            }
        }.start();
//        thread2.start();
//        thread3.start();
        refresh();
    }

    @UiThread(delay = 200)
    public void refresh(){
        swipe.setRefreshing(true);
    }



    @Click
    void select_bg(){
        fillter_lll.setVisibility(View.GONE);
        fillter_food.setVisibility(View.GONE);
        fillter_priority.setVisibility(View.GONE);
        select_bg.setVisibility(View.GONE);
    }
    @Click(R.id.left)
    void clickLeft(){
        finish();
        overridePendingTransition(0, 0);
    }
    @OptionsItem(R.id.location)
    void clickRight(){
        isFirst=true;
        region="";
        ChoseLocationActivity_.intent(this).startForResult(55);

    }
    @OnActivityResult(55)
    void onLocationResult(int result,Intent data) {
        if (result == 11) {
            Area a = (Area) data.getSerializableExtra("area");
            if(location_menu!=null){
                location_menu.setTitle(a.name.replace("市", ""));
            }
            city=a.name.replace("市", "");
            cityId=a.id+"";
            all_business.setText("全部商圈");
            districStr.clear();
            neighborhoodStr.clear();

            new Thread(){
                @Override
                public void run() {
                    getDistrictsListData();
                }
            }.start();
            foodStr.clear();
            new Thread(){
                @Override
                public void run() {
                    getCategoryListData();
                }
            }.start();
            swipe.setRefreshing(true);
        }
    }

    @Click
    void all_food(){
        if("电影".equals(category)){
            return;
        }
        fillter_lll.setVisibility(View.GONE);
        fillter_priority.setVisibility(View.GONE);
        if(fillter_food.getVisibility()==View.GONE){
            fillter_food.setVisibility(View.VISIBLE);
            select_bg.setVisibility(View.VISIBLE);
        }else{
            fillter_food.setVisibility(View.GONE);
            select_bg.setVisibility(View.GONE);
        }

    }
    boolean isClick=false;
    @Click
    void search_restarent(){
        p=1;
        isClick=true;
        swipe.setRefreshing(true);
    }
    @Click
    void default_sort(){
        fillter_lll.setVisibility(View.GONE);
        fillter_food.setVisibility(View.GONE);
        if(fillter_priority.getVisibility()==View.GONE){
            fillter_priority.setVisibility(View.VISIBLE);
            select_bg.setVisibility(View.VISIBLE);
        }else{
            fillter_priority.setVisibility(View.GONE);
            select_bg.setVisibility(View.GONE);
        }
    }
    @ItemClick
    void fillter_food(int position){
        if(position==0){
            category="美食";
        }else{
            category=foodStr.get(position);
        }
        all_food.setText(foodStr.get(position));
        fillter_food.setVisibility(View.GONE);
        select_bg.setVisibility(View.GONE);
        swipe.setRefreshing(true);
    }
    @ItemClick
    void fillter_priority(int position){
        p=1;
        sort=position+1;
        default_sort.setText(priority.get(position));
        fillter_priority.setVisibility(View.GONE);
        select_bg.setVisibility(View.GONE);
        swipe.setRefreshing(true);
    }
    @ItemClick
    void listview_bus(int position) {
        if(position>0){
            RestaurantDetailActivity_.intent(this).extra("business_url",((BusinessBean)adapter.getList().get(position-1)).getBusiness_url()).start();
        }
    }
    @Click
    void all_business(){
        fillter_priority.setVisibility(View.GONE);
        fillter_food.setVisibility(View.GONE);
        if(fillter_lll.getVisibility()==View.GONE){
            fillter_lll.setVisibility(View.VISIBLE);
            select_bg.setVisibility(View.VISIBLE);
        }else{
            fillter_lll.setVisibility(View.GONE);
            select_bg.setVisibility(View.GONE);
        }
    }
    @ItemClick
    void listview_1(int position){
            p=1;
            neighborhoodStr.clear();
            neighborhoodStr.addAll(cityList.get(0).getDistricts().get(position).getNeighborhoods());
            neighborhood_ada.notifyDataSetChanged();

    }
    @ItemClick
    void listview_2(int position){
        isFirst=false;
        region=neighborhoodStr.get(position);
        all_business.setText(neighborhoodStr.get(position));
        fillter_lll.setVisibility(View.GONE);
        select_bg.setVisibility(View.GONE);
        swipe.setRefreshing(true);

    }
    int p=1;
    boolean isFirst=true;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                businesses= (List<BusinessBean>) msg.obj;
                LogUtil.d("============businesses",businesses.toString());
                if(p==1){
                    adapter.setList(businesses);
                }else{
                    adapter.addNewsItems(businesses);
                }
                if(businesses.size()<20){
                    listview_bus.setPage(-1);
                }else{
                    listview_bus.setPage(p);
                }
                if(isClick){
                    adapter.setList(businesses);
                }
                adapter.notifyDataSetChanged();
                if(isClick){
                    isClick=false;
                }else{
                    p+=1;
                }
            }else if(msg.what==2){
                district_ada.notifyDataSetChanged();
                neighborhood_ada.notifyDataSetChanged();
            }else if(msg.what==3){
                food_ada.notifyDataSetChanged();
            }else if(msg.what==9){
                listview_bus.stop();
                swipe.setRefreshing(false);
            }
        }
    };
    void getBusinessListData(){
        String apiUrl = "http://api.dianping.com/v1/business/find_businesses";
        String appKey = URL.appKey;
        String secret = URL.secret;

        Map<Object, Object> paramMap = new HashMap<Object, Object>();
        if(isFirst&&city.equals(ac.cs.getLocation().replace("市",""))){
            paramMap.put("latitude", ac.cs.getLat());
            paramMap.put("longitude", ac.cs.getLng());
        }
        paramMap.put("city",city);
        if(!isFirst){
            paramMap.put("region", region);
        }
        paramMap.put("category", category);
//        paramMap.put("keyword", keyword);
//        paramMap.put("platform", 2);
        if(isClick){
            paramMap.put("keyword", search_business.getText().toString());
            paramMap.put("page", 1);
            paramMap.remove("latitude");
            paramMap.remove("longitude");
        }else{
            paramMap.put("page", p);
        }

        paramMap.put("sort", sort);
        paramMap.put("limit", 40);
        paramMap.put("format", "json");
        String requestResult = DemoApiTool.requestApi(this,apiUrl, appKey, secret, paramMap);
        LogUtil.d("===========requestResult", requestResult);
        try {
            JSONObject json=new JSONObject(requestResult);
            if("OK".equals(json.getString("status"))) {
                businesses = new Gson().fromJson(json.getString("businesses"),
                        new TypeToken<ArrayList<BusinessBean>>() {
                        }.getType());
                Message msg = handler.obtainMessage();
                msg.what = 1;
                msg.obj = businesses;
                msg.sendToTarget();
            }

        }catch (JSONException e){
        }finally {
            Message msg=handler.obtainMessage();
            msg.what=9;
            msg.sendToTarget();

        }
    }
    void getDistrictsListData(){
        String apiUrl = "http://api.dianping.com/v1/metadata/get_regions_with_businesses";
        String appKey = URL.appKey;
        String secret = URL.secret;

        Map<Object, Object> paramMap = new HashMap<Object, Object>();
        paramMap.put("city", city);
        paramMap.put("format", "json");
        String requestResult = DemoApiTool.requestApi(this,apiUrl, appKey, secret, paramMap);
        LogUtil.d("===========citys", requestResult);
        try {
            JSONObject json=new JSONObject(requestResult);
            if("OK".equals(json.getString("status"))){
                cityList=new Gson().fromJson(json.getString("cities"),
                        new TypeToken<ArrayList<City>>() {
                        }.getType());
                for(City.District district:cityList.get(0).getDistricts()){
                    districStr.add(district.getDistrict_name());

                }
                LogUtil.d("===============districStr",districStr.toString());

                LogUtil.d("===============district_ada",district_ada.toString());
                neighborhoodStr.addAll(cityList.get(0).getDistricts().get(0).getNeighborhoods());
                LogUtil.d("===============neighborhoodStr",neighborhoodStr.toString());
                Message msg=handler.obtainMessage();
                msg.what=2;
                msg.sendToTarget();

                }

        }catch (JSONException e){
        }finally {

        }
    }
    void getCategoryListData(){
        String apiUrl = "http://api.dianping.com/v1/metadata/get_categories_with_businesses";
        String appKey = URL.appKey;
        String secret = URL.secret;

        Map<Object, Object> paramMap = new HashMap<Object, Object>();
        paramMap.put("city",city);
        paramMap.put("format", "json");
        String requestResult = DemoApiTool.requestApi(this,apiUrl, appKey, secret, paramMap);
        LogUtil.d("===========citys", requestResult);
        try {
            JSONObject json=new JSONObject(requestResult);
            if("OK".equals(json.getString("status"))){
                categoryList=new Gson().fromJson(json.getString("categories"),
                        new TypeToken<ArrayList<Category>>() {
                        }.getType());
                for(Category category:categoryList){
                    if("美食".equals(category.getCategory_name())){
                        for(Category.Subcategory subcategory:category.getSubcategories()){
                            foodStr.add(subcategory.getCategory_name());
                        }
                        foodStr.add(0,"全部美食");

                        Message msg=handler.obtainMessage();
                        msg.what=3;
                        msg.sendToTarget();
                        return;
                    }
                }

              }

        }catch (JSONException e){
        }finally {

        }
    }

    @Override
    public void returnResult(Object bean) {
        BusinessBean bb=(BusinessBean)bean;
        bb.setCityId(cityId);
        Intent intent=new Intent();
        intent.putExtra("bean", bb);
        setResult(33,intent);
        finish();
    }

    @Override
    public void onRefresh() {
        p=1;
        location.startLocation();


    }

    @Override
    public void onLoadMore() {
        onLocationSuccess();
    }

    @Override
    public void onLocationSuccess() {
//        Message msg1 = handler.obtainMessage();
//        msg1.what = 1;
//        msg1.sendToTarget();
        new Thread(){
            @Override
            public void run() {
                getBusinessListData();
            }
        }.start();
//        getBusinessListData();
    }

    @Override
    public void onLocationFail() {
        showCustomToast("定位失败");
        listview_bus.stop();
        swipe.setRefreshing(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        location.destory();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}

class City{
    String city_name;
    List<District> districts;
//    List<District> districtList;
    public City(){}
    public City(String city_name,List<District> districts){
        this.city_name=city_name;
        this.districts=districts;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }
    class District{
        String id;
        String district_name;
        List<String> neighborhoods;
        public District(){

        }
        public District(String id,String district_name,List<String> neighborhoods){
            this.id=id;
            this.district_name=district_name;
            this.neighborhoods=neighborhoods;
        }

        public List<String> getNeighborhoods() {
            return neighborhoods;
        }

        public String getId() {
            return id;
        }


        public void setId(String id) {
            this.id = id;
        }

        public void setDistrict_name(String district_name) {
            this.district_name = district_name;
        }

        public String getDistrict_name() {
            return district_name;
        }
    }
}
class Category{
    String category_name;
    List<Subcategory> subcategories;
    public Category(){}
    public Category(String category_name,List<Subcategory> subcategories){
        this.category_name=category_name;
        this.subcategories=subcategories;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }

    class Subcategory{
        String category_name;
        List<String> subcategories;
        public Subcategory(){}
        public Subcategory(String category_name,List<String> subcategories){
            this.category_name=category_name;
            this.subcategories=subcategories;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public List<String> getSubcategories() {
            return subcategories;
        }

        public void setSubcategories(List<String> subcategories) {
            this.subcategories = subcategories;
        }
    }
}
