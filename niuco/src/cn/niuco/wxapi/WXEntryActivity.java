package cn.niuco.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.niuco.R;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;

import java.sql.SQLException;

import cn.niuco.library.AccountManager;
import cn.niuco.library.DataCenter.http.AsynHttpImpl;
import cn.niuco.library.DataCenter.http.IAsyncHttpResultCallback;
import cn.niuco.library.bean.UserBase;
import cn.niuco.library.bean.UserBaseBean;
import cn.niuco.library.db.bean.UserInfo;
import cn.niuco.library.db.helper.DataBaseHelper;
import cn.niuco.ui.activity.HomeActivity;
import cn.niuco.ui.activity.LoginActivity;

public class WXEntryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//閸樼粯甯�弽鍥暯閺嶏拷
        setContentView(R.layout.activity_login);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }


    private void handleIntent(Intent intent) {
        SendAuth.Resp resp = new SendAuth.Resp(intent.getExtras());
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            AsynHttpImpl.getInstance(WXEntryActivity.this).getWeChatAccessTokenByCodeFun(LoginActivity.WeiXin_APP_ID, LoginActivity.AppSecret, resp.code, new IAsyncHttpResultCallback() {
                @Override
                public void onStart() {
//                    loadingDialog = new LoadingDialogFragment();
//                    loadingDialog.show(getFragmentManager(), "LoadingFragment");
                }

                @Override
                public void onSuccess(Object response) {
                    JSONObject jsonObject = JSON.parseObject(response.toString());
                    AsynHttpImpl.getInstance(WXEntryActivity.this).userLogin(jsonObject.getString("access_token") + "", jsonObject.getString("openid") + "",
                            5, new IAsyncHttpResultCallback() {

                                @Override
                                public void onStart() {
//                                    loadingDialog = new LoadingDialogFragment();
//                                    loadingDialog.show(getFragmentManager(), "LoadingFragment");
                                }

                                @Override
                                public void onSuccess(Object response) {
                                    Log.e("niuco","Wechat onSuccess" + response.toString());

                                    UserBaseBean baseBean = JSON.parseObject(response.toString(), UserBaseBean.class);
                                    if(baseBean.getCode()==200)
                                    {
                                        UserBase userBase = baseBean.getData();
                                        UserInfo user1 = new UserInfo();
                                        user1.setUserID(userBase.getUserID());
                                        user1.setNickname(userBase.getNickName());
                                        user1.setAccount(userBase.getAccount());
                                        user1.setPortrait(userBase.getPortrait());
                                        user1.setToken(userBase.getToken());

                                        DataBaseHelper helper = DataBaseHelper.getHelper(WXEntryActivity.this);
                                        try {
                                            helper.getUserDao().createIfNotExists(user1);
                                            AccountManager.getInstance(WXEntryActivity.this).setLogined(true);
                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }

                                    Intent intent = new Intent(WXEntryActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    ((Activity) LoginActivity.activityContext).finish();
                                    finish();
                                }
                                }

                                @Override
                                public void onFailure(Object response) {
//                                    LogUtil.e("onFailure", "" + response);
//                                    Toast.makeText(WXEntryActivity.this,"onFailure:"+response.toString(),Toast.LENGTH_LONG).show();
//                                    if (loadingDialog.isVisible()) {
//                                        loadingDialog.dismiss();
//                                    }
                                }
                            });
                }

                @Override
                public void onFailure(Object response) {
//                    if (loadingDialog.isVisible()) {
//                        loadingDialog.dismiss();
//                    }
                }
            });
        }else if (resp.errCode == BaseResp.ErrCode.ERR_USER_CANCEL) {
            this.finish();
        }
    }
}