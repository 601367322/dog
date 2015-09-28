package com.quanliren.quan_two.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.quanliren.quan_two.activity.user.*;
import com.quanliren.quan_two.application.AM;

public class Noti extends Activity {
    private static final String TAG = "Noti";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        if (!AM.getActivityManager().contains(
                PropertiesActivity_.class.getName())) {
            Intent intent = new Intent(this, PropertiesActivity_.class);
            startActivity(intent);
        }
        if (getIntent() != null && getIntent().getExtras() != null
                && getIntent().getExtras().containsKey("activity")) {
            Class class1 = (Class) getIntent().getExtras().get("activity");
            Intent i = new Intent(this, class1);
            if (class1.getName().equals(ChatActivity_.class.getName())) {
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("friend",
                        getIntent().getExtras().getSerializable("friend"));
            } else {
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            }
            startActivity(i);
        }
        finish();
    }
}
