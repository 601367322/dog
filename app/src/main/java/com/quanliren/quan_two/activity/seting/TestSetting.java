package com.quanliren.quan_two.activity.seting;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.util.Util;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Created by Shen on 2015/7/29.
 */
@OptionsMenu(R.menu.edit_info_menu)
@EActivity(R.layout.activity_test_setting)
public class TestSetting extends BaseActivity {

    @ViewById
    AutoCompleteTextView ip, port, socket;
    @ViewById
    SwitchCompat log_switch;

    @Override
    public void init() {
        super.init();

        String[] ipArr = {"192.168.1.22", "www.bjqlr.cn"};
        String[] portArr = {"8081", "8082", "80"};
        String[] socketArr = {"30003", "30004"};
        initTextView(ipArr, ip);
        initTextView(portArr, port);
        initTextView(socketArr, socket);

        ip.setText(Util.getPropertiesValue("ip"));
        port.setText(Util.getPropertiesValue("port"));
        socket.setText(Util.getPropertiesValue("socket"));
        if (Boolean.parseBoolean(Util.getPropertiesValue("debug"))) {
            log_switch.setChecked(true);
        } else {
            log_switch.setChecked(false);
        }
    }

    public void initTextView(String[] data, AutoCompleteTextView view) {
        ArrayAdapter ipAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        view.setAdapter(ipAdapter);
        view.setThreshold(1);
        view.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((AutoCompleteTextView) v).showDropDown();
        }
    };

    @OptionsItem
    public void save() {
        String ip_str = ip.getText().toString();
        String port_str = port.getText().toString();
        String socket_str = socket.getText().toString();
        boolean logEnable = log_switch.isChecked();

        Properties prop = new Properties();
        prop.put("ip", ip_str);
        prop.put("port", port_str);
        prop.put("socket", socket_str);
        prop.put("debug", String.valueOf(logEnable));

        saveChangeData(prop);
    }

    private void saveChangeData(Properties prop) {

        if (Util.checkSaveLocationExists()) {

            File file = new File(Util.getBaseDir(this));
            if (!file.exists()) {
                file.mkdirs();
            }
            try {
                FileOutputStream fos = new FileOutputStream(Util.getBaseDir(this) + Util.getPathName(), false);
                prop.store(fos, null);
            } catch (Exception e) {
                e.printStackTrace();

            }
            Util.toast(this, "保存成功，请清除数据重新启动");
            showInstalledAppDetails(this, getPackAgeName());

        } else {
            Util.toast(this, "保存失败，无SD卡");
        }
    }

    private String getPackAgeName() {
        String packageNames = "";
        try {
            PackageInfo info = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            packageNames = info.packageName;
        } catch (PackageManager.NameNotFoundException e) {

            packageNames = "";
            e.printStackTrace();
        }
        return packageNames;
    }

    private void showInstalledAppDetails(Context context, String packageName) {

        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);
        context.startActivity(intent);
    }
}
