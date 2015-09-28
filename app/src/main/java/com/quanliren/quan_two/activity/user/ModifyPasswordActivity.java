package com.quanliren.quan_two.activity.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;
import com.loopj.android.http.RequestParams;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.bean.LoginUser;
import com.quanliren.quan_two.bean.MoreLoginUser;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.bean.UserTable;
import com.quanliren.quan_two.db.DBHelper;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OrmLiteDao;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

@EActivity
public class ModifyPasswordActivity extends BaseActivity {

    @ViewById
    EditText oldpassword;
    @ViewById
    EditText password;
    @ViewById
    EditText confirm_password;
    @ViewById
    Button modifyBtn;

    @OrmLiteDao(helper = DBHelper.class, model = MoreLoginUser.class)
    public Dao<MoreLoginUser, Integer> moreLoginUserDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifypassword);
        getSupportActionBar().setTitle("修改密码");
    }

    @Click
    public void modifyBtn(View v) {
        String str_oldpassword = oldpassword.getText().toString().trim();
        String str_password = password.getText().toString().trim();
        String str_confirm_password = confirm_password.getText().toString().trim();

        if (str_password.length() > 16 || str_password.length() < 6) {
            showCustomToast("密码长度为6-16个字符");
            return;
        } else if (!str_password.matches("^[a-zA-Z0-9 -]+$")) {
            showCustomToast("密码中不能包含特殊字符");
            return;
        } else if (!str_confirm_password.equals(str_password)) {
            showCustomToast("确认密码与密码不同");
            return;
        }

        RequestParams ap = getAjaxParams();
        ap.put("oldpwd", str_oldpassword);
        ap.put("pwd", str_password);
        ap.put("repwd", str_confirm_password);

        User lou = new User();
        lou.setMobile(getHelper().getUser().getMobile());
        lou.setPwd(str_password);
        ac.finalHttp.post(URL.MODIFYPASSWORD, ap, new callBack(lou));
    }

    class callBack extends JsonHttpResponseHandler {
        User u;

        public callBack(User u) {
            this.u = u;
        }

        public void onStart() {
            customShowDialog(1);
        }

        public void onFailure() {
            customDismissDialog();
            showIntentErrorToast();
        }


        public void onSuccess(JSONObject jo) {
            customDismissDialog();
            try {
                int status = jo.getInt(URL.STATUS);
                switch (status) {
                    case 0:
                        showCustomToast("修改成功");

                        moreLoginUserDao.delete(moreLoginUserDao.deleteBuilder().where().eq("username", u.getMobile()).prepareDelete());
                        moreLoginUserDao.create(new MoreLoginUser(u.getMobile(), u.getPwd()));

                        User user = new Gson().fromJson(jo.getString(URL.RESPONSE), User.class);
                        LoginUser lu = new LoginUser(user.getId(), u.getMobile(), u.getPwd(), user.getToken());

                        //保存用户
                        userTableDao.deleteById(user.getId());
                        userTableDao.create(new UserTable(user));

                        //保存登陆用户
                        TableUtils.clearTable(getConnectionSource(), LoginUser.class);
                        loginUserDao.create(lu);

                        finish();
                        break;
                    default:
                        showCustomToast(jo.getJSONObject(URL.RESPONSE).getString(URL.INFO));
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
