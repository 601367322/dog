package com.quanliren.quan_two.activity.redline;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.near.NearPeopleActivity;
import com.quanliren.quan_two.activity.near.NearPeopleActivity_;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.application.AM;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.custom.UserLogo;
import com.quanliren.quan_two.custom.UserNickNameRelativeLayout;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.MyJsonHttpResponseHandler;

import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by Shen on 2015/7/13.
 */
public class RedLinePublishUserListAdapter extends BaseAdapter<User> {

    RedLineBean redLineBean;

    public RedLinePublishUserListAdapter(Context context, RedLineBean bean) {
        super(context);
        this.redLineBean = bean;
    }

    @Override
    public BaseHolder getHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public int getConvertView(int position) {
        switch (getItemViewType(position)) {
            case 0:
                return R.layout.red_line_publish_user_list_item;
            case 1:
                return R.layout.red_line_publish_user_list_empty;
            case 2:
                return R.layout.red_line_publish_user_list_title;
        }
        return 0;
    }

    public class ViewHolder extends BaseHolder {

        @Nullable
        @Bind(R.id.userlogo)
        UserLogo userlogo;
        @Nullable
        @Bind(R.id.nick_ll)
        UserNickNameRelativeLayout nick_ll;
        @Nullable
        @Bind(R.id.done)
        TextView done;
        @Nullable
        @Bind((R.id.go_near))
        TextView go_near;

        public ViewHolder(View view) {
            super(view);
        }

        @Override
        public void bind(User bean, int position) {
            switch (getItemViewType(position)) {
                case 0:
                    ImageLoader.getInstance().displayImage(
                            bean.getAvatar() + StaticFactory._160x160, userlogo,
                            ac.options_userlogo);
                    userlogo.setUser(bean);
                    nick_ll.setUser(bean);
                    done.setTag(bean);
                    done.setOnClickListener(doneClick);
                    if (bean.getSex().equals("0")) {
                        done.setText(R.string.send_to_she);
                    } else {
                        done.setText(R.string.send_to_he);
                    }
                    break;
                case 1:
                    go_near.setOnClickListener(nearClick);
                    break;
            }
        }

        View.OnClickListener doneClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final User user = (User) view.getTag();

                final Dialog dialog = new Dialog(context, R.style.red_line_dialog);
                View convertView = LayoutInflater.from(context).inflate(R.layout.red_line_set_message_dialog, null);
                final EditText editText = (EditText) convertView.findViewById(R.id.edittext);
                TextView button = (TextView) convertView.findViewById(R.id.ok);
                convertView.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                ImageLoader.getInstance().displayImage(user.getAvatar() + StaticFactory._320x320, (ImageView) convertView.findViewById(R.id.userlogo), ac.options_userlogo);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String message = editText.getText().toString();
                        if (message.length() == 0) {
                            Util.toast(context, context.getString(R.string.please_set_message));
                            return;
                        }
                        dialog.dismiss();
                        RequestParams params = Util.getAjaxParams(context);
                        if (redLineBean != null) {
                            params.put("treadId", redLineBean.id);
                        }
                        params.put("transId", user.getId());
                        params.put("message", message);
                        ac.finalHttp.post(URL.PUBLISH_REDLINE, params, new MyJsonHttpResponseHandler(context, context.getString(R.string.loading), null) {
                            @Override
                            public void onSuccessRetCode(JSONObject jo) throws Throwable {
                                AM.getActivityManager().popActivity(RedLinePublishUserListActivity_.class);
                                RedLineBean bean = new RedLineBean();
                                bean.id = jo.optJSONObject(URL.RESPONSE).optInt("id");
                                RedLineDetailActivity_.intent(context).bean(bean).start();
                            }
                        });
                    }
                });
                dialog.setContentView(convertView);

                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//                lp.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.85);
                dialog.getWindow().setAttributes(lp);
                dialog.show();
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        Util.showKeyBoard(context);
                    }
                }, 2);
            }
        };

        View.OnClickListener nearClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NearPeopleActivity_.intent(context).listType(NearPeopleActivity.NEARLISTTYPE.NEAR).start();
            }
        };
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        User user = getItem(position);
        if (user.getId().equals("-1")) {
            return 1;
        } else if (user.getId().equals("-2")) {
            return 2;
        }else{
            return 0;
        }
    }
}
