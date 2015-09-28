package com.quanliren.quan_two.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.util.LogUtil;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import butterknife.Bind;

public class NearNoRelationAdapter extends BaseAdapter<User> {
    public NearNoRelationAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseHolder getHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public int getConvertView(int position) {
            return R.layout.nearby_item;
    }

    public class ViewHolder extends BaseHolder {

        @Bind(R.id.userlogo)
        ImageView userlogo;
        @Bind(R.id.name)
        TextView nickname;
        @Bind(R.id.opera_false)
        TextView opera_false;
        @Bind(R.id.opera)
        Button opera;
        public ViewHolder(View view) {
            super(view);
        }

        @Override
        public void bind(final User user, int position) {
            ImageLoader.getInstance().displayImage(
                    user.getAvatar() + StaticFactory._320x320, userlogo,
                    ac.options_userlogo);
            if(user.getNickname()!=null&&!"".equals(user.getNickname())){
                nickname.setText(user.getNickname());
            }
            if(user.getIsAtten()==0){
                opera.setVisibility(View.VISIBLE);
                opera.setText("关注");
                opera_false.setVisibility(View.GONE);
                opera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(opera.getId()!=0){
                            new AlertDialog.Builder(context)
                                    .setTitle("提示")
                                    .setMessage("您确定要关注TA吗?")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {
                                            RequestParams ap = ((BaseActivity)context).getAjaxParams();
                                            ap.put("otherid", user.getId());
                                            ac.finalHttp.post(URL.CONCERN, ap, new JsonHttpResponseHandler(){
                                                @Override
                                                public void onStart() {
                                                    super.onStart();
                                                    ((BaseActivity) context).customShowDialog(1);
                                                }

                                                @Override
                                                public void onFailure() {
                                                    super.onFailure();
                                                    ((BaseActivity) context).customDismissDialog();
                                                    ((BaseActivity) context).showIntentErrorToast();
                                                }

                                                @Override
                                                public void onSuccess(JSONObject jo) {
                                                    super.onSuccess(jo);
                                                    ((BaseActivity) context).customDismissDialog();
                                                    try {
                                                        int status = jo.getInt(URL.STATUS);
                                                        switch (status) {
                                                            case 0:
                                                                opera_false.setVisibility(View.VISIBLE);
                                                                opera.setVisibility(View.GONE);
                                                                user.setIsAtten(1);
                                                                break;
                                                            default:
                                                                ((BaseActivity) context).showFailInfo(jo);
                                                                break;
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                        }
                                    })
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface arg0, int arg1) {
                                        }
                                    }).create().show();
                        }
                    }
                });
            }else{
                opera_false.setVisibility(View.VISIBLE);
                opera.setVisibility(View.GONE);
            }

        }

    }
}
