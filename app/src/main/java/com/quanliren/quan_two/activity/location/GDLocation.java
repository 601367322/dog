package com.quanliren.quan_two.activity.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.quanliren.quan_two.application.*;
import com.quanliren.quan_two.bean.Area;
import com.quanliren.quan_two.fragment.ChosePositionFragment;
import com.quanliren.quan_two.util.LogUtil;
import com.quanliren.quan_two.util.Util;

import java.text.DecimalFormat;
import java.util.List;


public class GDLocation implements AMapLocationListener {

    private ILocationImpl locationListener;

    public void setLocationListener(ILocationImpl locationListener) {
        this.locationListener = locationListener;
    }

    private LocationManagerProxy mAMapLocationManager;
    private AppClass_ ac;
    private Context context;

    public GDLocation(Context context, ILocationImpl listener, boolean autoStart) {
        this.ac = (AppClass_) context.getApplicationContext();
        this.context = context;
        this.locationListener = listener;
        if (autoStart){
            startLocation();
        }
    }

    public void startLocation() {
        if (Util.isFastLocation()) {
            if (locationListener != null) {
                locationListener.onLocationSuccess();
            }
            return;
        } else {
            mAMapLocationManager = LocationManagerProxy.getInstance(context);
            mAMapLocationManager.requestLocationData(
                    LocationProviderProxy.AMapNetwork, -1, 0, this);

        }
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

        deactivate();
        if (aLocation == null
                || aLocation.getAMapException().getErrorCode() != 0) {
            if (locationListener != null)
                locationListener.onLocationFail();
            return;
        }
        DecimalFormat df = new DecimalFormat("#.######");
        if (!df.format(aLocation.getLatitude()).equals("0"))
            ac.cs.setLat(df.format(aLocation.getLatitude()));
        if (!df.format(aLocation.getLongitude()).equals("0"))
            ac.cs.setLng(df.format(aLocation.getLongitude()));
        if (aLocation.getDistrict() != null)
            ac.cs.setArea(aLocation.getDistrict());
        String city = aLocation.getCity();
        LogUtil.d("===============city",aLocation.getCity());
        if (city != null) {
            List<Area> areaList = ChosePositionFragment.getAreas();
            Area temp = null;
            for (Area area : areaList) {
                if (city.indexOf(area.name) > -1) {
                    temp = area;
                    break;
                }
            }
            if (temp != null) {
                ac.cs.setLocation(city);
                ac.cs.setLocationID(temp.id);
            }
        }
        Util.locationTime = System.currentTimeMillis();
        if (locationListener != null)
            locationListener.onLocationSuccess();

    }

    public void deactivate() {
        if (mAMapLocationManager != null) {
            mAMapLocationManager.removeUpdates(this);
            mAMapLocationManager.destroy();
        }
        mAMapLocationManager = null;
    }

    public void destory() {
        deactivate();
    }
}
