package com.quanliren.quan_two.activity.redline;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.adapter.base.BaseAdapter;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.custom.UserLogo;
import com.quanliren.quan_two.custom.UserNickNameRelativeLayout;
import com.quanliren.quan_two.util.StaticFactory;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.Util;
import com.quanliren.quan_two.util.http.MyJsonHttpResponseHandler;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Shen on 2015/7/1.
 */
public class RedLineDetailAdapter extends BaseAdapter<User> {

    public RedLineBean getRedLineBean() {
        return redLineBean;
    }

    public void setRedLineBean(RedLineBean redLineBean) {
        this.redLineBean = redLineBean;
    }

    private RedLineBean redLineBean;

    public RedLineDetailAdapter(Context context) {
        super(context);
    }

    @Override
    public BaseHolder getHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    public int getConvertView(int position) {
        return R.layout.red_line_detail_list_item;
    }

    public class ViewHolder extends BaseHolder {

        @Bind(R.id.userlogo)
        UserLogo userlogo;
        @Bind(R.id.nick_ll)
        UserNickNameRelativeLayout nickLl;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.message)
        TextView message;
        @Bind(R.id.delete_user)
        View delete_user;

        public ViewHolder(View view) {
            super(view);
        }


        @Override
        public void bind(User bean, int position) {
            if(bean.getAvatar()==null){
                return;
            }
            ImageLoader.getInstance().displayImage(bean.getAvatar() + StaticFactory._160x160, userlogo, ac.options_userlogo);
            userlogo.setUser(bean);
            nickLl.setUser(bean);
            time.setText(Util.getTimeDateStr(bean.getCreate_time()));
            if (TextUtils.isEmpty(bean.getMessage())) {
                message.setText(R.string.lazy);
            } else {
                message.setText(bean.getMessage());
            }
            if (bean.isTimeOut()) {
                delete_user.setTag(bean);
                delete_user.setVisibility(View.VISIBLE);
            } else {
                delete_user.setVisibility(View.GONE);
            }
        }

        @OnClick(R.id.delete_user)
        public void delteUser(View view) {
            final User bean = (User) view.getTag();
            RequestParams params = Util.getAjaxParams(context);
            params.put("transId", bean.getId());
            params.put("treadId", redLineBean.id);
            ac.finalHttp.post(URL.RED_LINE_CLOSE, params, new MyJsonHttpResponseHandler(context, "正在删除") {
                @Override
                public void onSuccessRetCode(JSONObject jo) throws Throwable {
                    remove(bean);
                    notifyDataSetChanged();
                }
            });
        }

    }
}
