package com.quanliren.quan_two.activity.public_comments;

import android.os.Bundle;

import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.fragment.ChosePositionFragment;

import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.chose_position_actvitiy)
public class ChoseLocationActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportFragmentManager().beginTransaction().replace(R.id.content, new ChosePositionFragment()).commitAllowingStateLoss();
	}

}

