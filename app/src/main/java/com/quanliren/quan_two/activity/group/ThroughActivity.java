package com.quanliren.quan_two.activity.group;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.SupportMapFragment;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.activity.base.BaseApi;
import com.quanliren.quan_two.activity.group.date.DateListActivity_;
import com.quanliren.quan_two.activity.group.date.DateListApi;
import com.quanliren.quan_two.activity.near.NearPeopleActivity;
import com.quanliren.quan_two.activity.near.NearPeopleActivity_;
import com.quanliren.quan_two.activity.near.NearPeopleApi;
import com.quanliren.quan_two.activity.user.UserInfoActivity_;
import com.quanliren.quan_two.activity.user.UserOtherInfoActivity_;
import com.quanliren.quan_two.application.AppClass;
import com.quanliren.quan_two.bean.DateBean;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.custom.ThroughImageView;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.MyJsonHttpResponseHandler;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@EActivity
@OptionsMenu(R.menu.through_map_menu)
public class ThroughActivity extends BaseActivity implements LocationSource,
        AMapLocationListener, AMap.OnCameraChangeListener, GeocodeSearch.OnGeocodeSearchListener,AMap.OnMarkerClickListener {
    public static final LatLng BEIJING = new LatLng(39.908691, 116.397506);// 北京市经纬度

    private static final String MAP_FRAGMENT_TAG = "map";
    AMap amap;
    private OnLocationChangedListener mListener;
    private LocationManagerProxy mAMapLocationManager;
    private SupportMapFragment map;

    public enum THROUGHTYPE {
        PEOPLE, DATE
    }

    @Extra
    public THROUGHTYPE throughtype = THROUGHTYPE.PEOPLE;

    @ViewById
    TextView tv_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.through_map);

        CameraPosition LUJIAZUI = new CameraPosition.Builder().target(BEIJING)
                .zoom(16).bearing(0).tilt(0).build();
        AMapOptions aOptions = new AMapOptions();
        aOptions.camera(LUJIAZUI);
        if (map == null) {
            map = SupportMapFragment.newInstance(aOptions);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction();
            fragmentTransaction.add(R.id.map, map, MAP_FRAGMENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();

        }

    }

    @AfterViews
    void initView() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (amap == null) {
            amap = map.getMap();// amap对象初始化成功
            setUpMap();
        }
        getSupportActionBar().setTitle(R.string.vip_through);
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mAMapLocationManager == null) {
            mAMapLocationManager = LocationManagerProxy.getInstance(this);
            mAMapLocationManager.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 0, this);
        }
    }

    private void setUpMap() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.location_marker));
        myLocationStyle.radiusFillColor(0x1902bce4);
        myLocationStyle.strokeColor(0x3302bce4);
        myLocationStyle.strokeWidth(1);
        amap.setMyLocationStyle(myLocationStyle);
        amap.setOnMarkerClickListener(this);
        amap.setLocationSource(this);
        amap.getUiSettings().setMyLocationButtonEnabled(true);
        amap.setMyLocationEnabled(true);
        amap.setOnCameraChangeListener(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLocationChanged(AMapLocation aLocation) {
        // TODO Auto-generated method stub
        if (mListener != null && aLocation != null && aLocation.getAMapException().getErrorCode() == 0) {
            mListener.onLocationChanged(aLocation);
            isLocationFinish.compareAndSet(false, true);
            onCameraChangeFinish(null);
        }
        deactivate();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        deactivate();
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mAMapLocationManager != null) {
            mAMapLocationManager.removeUpdates(this);
            mAMapLocationManager.destroy();
        }
        mAMapLocationManager = null;
    }

    @OptionsItem
    void ok() {
        LatLng mTarget = amap.getCameraPosition().target;
        if (throughtype == THROUGHTYPE.PEOPLE) {
            NearPeopleActivity_.intent(this).listType(NearPeopleActivity.NEARLISTTYPE.THROUGH).lat(mTarget.latitude).lng(mTarget.longitude).start();
        } else {
            DateListActivity_.intent(this).listType(NearPeopleActivity.NEARLISTTYPE.THROUGH).lat(mTarget.latitude).lng(mTarget.longitude).start();
        }
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        tv_position.setVisibility(View.GONE);
    }

    private LatLng searchLL = null;

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        if (amap == null) {
            return;
        }
        GeocodeSearch geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        LatLng mTarget = amap.getCameraPosition().target;
        searchLL = mTarget;
        LatLonPoint lp = new LatLonPoint(mTarget.latitude, mTarget.longitude);
        RegeocodeQuery query = new RegeocodeQuery(lp, 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);

        getUserList();
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (amap == null || amap.getCameraPosition() == null) {
            return;
        }
        LatLng mTarget = amap.getCameraPosition().target;
        if (mTarget.longitude == searchLL.longitude && mTarget.latitude == searchLL.latitude) {
            if (rCode == 0) {
                if (result != null && result.getRegeocodeAddress() != null
                        && result.getRegeocodeAddress().getFormatAddress() != null) {
                    String addressName = result.getRegeocodeAddress().getFormatAddress();
                    tv_position.setVisibility(View.VISIBLE);
                    tv_position.setText(addressName);
                } else {
                    tv_position.setVisibility(View.GONE);
                }
            } else {
            }
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }


    List<Marker> markers = new ArrayList<>();
    AtomicBoolean isLoading = new AtomicBoolean(false);
    AtomicBoolean isLocationFinish = new AtomicBoolean(false);

    public void getUserList() {
        if (!isLocationFinish.get()) {
            return;
        }
        if (isLoading.get()) {
            return;
        }
        isLoading.compareAndSet(false, true);
        for (Marker marker : markers) {
            marker.remove();
            marker.destroy();
        }
        markers.clear();

        BaseApi api = null;
        if (throughtype == THROUGHTYPE.PEOPLE) {
            api = new NearPeopleApi(this);
        } else {
            api = new DateListApi(this, NearPeopleActivity.NEARLISTTYPE.THROUGH);
        }
        api.setPage(0);
        LatLng mTarget = amap.getCameraPosition().target;
        api.getParams().put("longitude", mTarget.longitude);
        api.getParams().put("latitude", mTarget.latitude);
        ac.finalHttp.post(api.getUrl(), api.getParams(), new MyJsonHttpResponseHandler(this) {
            @Override
            public void onSuccessRetCode(JSONObject jo) throws Throwable {
                if (throughtype == THROUGHTYPE.PEOPLE) {
                    List<User> list = Util.jsonToList(jo.optJSONObject(URL.RESPONSE).optString(URL.LIST), User.class);
                    String loginUserId = ac.getLoginUserId();
                    for (User user : list) {
                        if (!user.getId().equals(loginUserId) && user.getLatitude() != 0 && user.getLongitude() != 0) {
                            addStoreMarket(user);
                        }
                    }
                } else {
                    List<DateBean> list = Util.jsonToList(jo.optJSONObject(URL.RESPONSE).optString(URL.LIST), DateBean.class);
                    for (DateBean date : list) {
                        if (date.getLatitude() != 0 && date.getLongitude() != 0) {
                            addStoreMarket(date);
                        }
                    }
                }

            }

            @Override
            public void onFinish() {
                super.onFinish();
                isLoading.compareAndSet(true, false);
            }
        });

    }

    ThroughImageView tiv;

    public void addStoreMarket(final Object object) {

        final DateBean datebean = object instanceof DateBean ? (DateBean) object : null;
        final User userbean = object instanceof User ? (User) object : null;

        ImageLoader.getInstance().loadImage(datebean == null ? userbean.getAvatar() : datebean.getAvatar() + StaticFactory._160x160, ac.options_userlogo, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if(loadedImage == null){
                    return;
                }
                if (tiv == null) {
                    tiv = new ThroughImageView(ThroughActivity.this);
                }
                MarkerOptions markerOption = new MarkerOptions();
                markerOption.position(datebean == null ? new LatLng(userbean.getLatitude(), userbean.getLongitude()) : new LatLng(datebean.getLatitude(), datebean.getLongitude()));
                markerOption.draggable(true);
                tiv.setmBitmap(loadedImage);
                markerOption.icon(BitmapDescriptorFactory.fromBitmap(tiv.getmBitmap()));
                Marker marker = amap.addMarker(markerOption);
                marker.setObject(object);
                markers.add(marker);
            }
        });

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Object object = marker.getObject();
        DateBean datebean = object instanceof DateBean ? (DateBean) object : null;
        User userbean = object instanceof User ? (User) object : null;
        if(datebean != null){
            userbean = new User();
            userbean.setId(datebean.getUserid());
        }
        if (userbean != null) {
            if (userbean.getId().equals(((AppClass) this.getApplicationContext()).getLoginUserId())) {
                UserInfoActivity_.intent(this).start();
            } else {
                UserOtherInfoActivity_.intent(this).userId(userbean.getId()).start();
            }
        }
        return true;
    }
}
