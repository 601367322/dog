package com.quanliren.quan_two.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.quanliren.quan_two.activity.R;
import com.quanliren.quan_two.activity.base.BaseActivity;
import com.quanliren.quan_two.bean.User;
import com.quanliren.quan_two.util.Constants;
import com.quanliren.quan_two.util.URL;
import com.quanliren.quan_two.util.http.JsonHttpResponseHandler;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

@EActivity(R.layout.search_friend)
public class SearchFriendActivity extends BaseActivity{

    @ViewById
    EditText search_input;
    @ViewById
    Button search_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

    }
    @Override
    public void init() {
        super.init();
        getSupportFragmentManager().beginTransaction().replace(R.id.content,NearNoRelationFragment_.builder().build()).commitAllowingStateLoss();
    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        getSupportActionBar().setTitle("添加好友");
    }

    JsonHttpResponseHandler callBack = new JsonHttpResponseHandler() {

        public void onStart() {
            customShowDialog("正在查找");
        }

        ;

        @Override
        public void onFailure() {
            customDismissDialog();
            showIntentErrorToast();
        }

        public void onSuccess(JSONObject jo) {
            try {
                int status = jo.getInt(URL.STATUS);
                switch (status) {
                    case 0:
                        if (jo.isNull(URL.RESPONSE)) {
                            showCustomToast("未查到好友");
                        } else {
                            User temp = new Gson().fromJson(jo.getString(URL.RESPONSE), new TypeToken<User>() {
                            }.getType());
                            if (temp != null) {
                                Intent i = new Intent(SearchFriendActivity.this, temp.getId()
                                        .equals(ac.getLoginUserId()) ? UserInfoActivity_.class
                                        : UserOtherInfoActivity_.class);
                                i.putExtra("userId", temp.getId());
                                startActivity(i);
                            }
                        }
                        break;
                    default:
                        showFailInfo(jo);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                customDismissDialog();
            }
        }

        ;
    };

    @Click
    void search_btn() {
        if (search_input.getText().toString().trim().equals("")) {
            showCustomToast("请输入好友ID");
            return;
        }
        RequestParams rp = getAjaxParams();
        rp.put("keyword", search_input.getText().toString().trim());
        ac.finalHttp.post(URL.SEARCH_FRIEND, rp, callBack);
    }

    private final UMSocialService mController = UMServiceFactory
            .getUMSocialService(Constants.DESCRIPTOR);
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

}
