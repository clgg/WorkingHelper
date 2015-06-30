package cn.niuco.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;
import com.niuco.R;
import com.alibaba.fastjson.JSON;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;

import cn.niuco.library.AccountManager;
import cn.niuco.library.DataCenter.http.AsynHttpImpl;
import cn.niuco.library.DataCenter.http.IAsyncHttpResultCallback;
import cn.niuco.library.bean.UserBase;
import cn.niuco.library.bean.UserBaseBean;
import cn.niuco.library.db.bean.UserInfo;
import cn.niuco.library.db.helper.DataBaseHelper;

public class LoginActivity extends Activity implements View.OnClickListener{
    private ImageView qqLoginImageView;
    private ImageView weiBoLoginImageView;
    private ImageView weiXinLoingImageView;
    private View loginView;
    private static final int HOME_PAGE = 3;
    private static final int LOGIN = 1;
    public static Tencent mTencent;
    private SsoHandler mSsoHandler;

    private WeiboAuth mWeiboAuth;
    private Oauth2AccessToken mAccessToken;

    private String QQ_APP_ID = "1103396097";
    private String Sina_APP_ID = "2533714381";
    public static String WeiXin_APP_ID = "wxff6808717a529f78";

    private String REDIRECT_URL = "http://e.weibo.com/lenovo";

    private String SCOPE = "email,direct_messages_read,direct_messages_write,friendships_groups_read,friendships_groups_write,statuses_to_me_read,follow_app_official_microblog,invitation_write";
    public static String AppSecret = "4512f9b1c4bc2acd63c8baa019c8b0e7";
    private IWXAPI api;

    public static Context activityContext;

    private void regToWx() {
        api = WXAPIFactory.createWXAPI(this, WeiXin_APP_ID, true);
        api.registerApp(WeiXin_APP_ID);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        activityContext = this;
        mTencent = Tencent.createInstance(QQ_APP_ID, this.getApplicationContext());
        mWeiboAuth = new WeiboAuth(this, Sina_APP_ID, REDIRECT_URL, SCOPE);
        regToWx();
        initView();

//        DataBaseHelper helper = DataBaseHelper.getHelper(LoginActivity.this);
//        UserInfo u1 = new UserInfo();
//        u1.setUserID(110);
//        List<UserInfo> users = null;
//        try {
//            users = helper.getUserDao().queryForAll();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        Log.e("niuco", users.get(0).getNickname());


//        if (UserInfoDataHelper.checkLogin(LoginActivity.this)) {
//            loginView.setVisibility(View.GONE);
//            new Thread() {
//                public void run() {
//                    try {
//                        Thread.sleep(2000);
//                        // 鍙互杩涜閫昏緫鍒ゆ柇
//                        mHandler.sendEmptyMessage(HOME_PAGE);
//
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }.start();
//        }else{
            loginView.setVisibility(View.VISIBLE);
//        }
    }

    private Handler mHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            Intent mIntent = null;
            switch (msg.what) {
                case HOME_PAGE:
                    mIntent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(mIntent);
                    break;
                default:
                    break;
            }

            finish();
        }
    };

    public void initView() {
        qqLoginImageView = (ImageView) findViewById(R.id.qq_login);
        weiBoLoginImageView = (ImageView) findViewById(R.id.weibo_login);
        weiXinLoingImageView = (ImageView) findViewById(R.id.weixin_login);
        loginView = (View) findViewById(R.id.loginView);
        qqLoginImageView.setOnClickListener(this);
        weiBoLoginImageView.setOnClickListener(this);
        if (!api.isWXAppInstalled()) {
            weiXinLoingImageView.setClickable(false);
            weiXinLoingImageView.setImageResource(R.drawable.wechat_highlight);
        } else {
            weiXinLoingImageView.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.qq_login:
                QQLogin();
                break;
            case R.id.weibo_login:
                SinaLoginSSO();
                break;
            case R.id.weixin_login:
                WeiXinLogin();
                break;
        }
    }

    public void QQLogin() {
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "ALL", new QQLoginListener());
        }
    }

    public void SinaLoginSSO() {
        mSsoHandler = new SsoHandler(LoginActivity.this, mWeiboAuth);
        mSsoHandler.authorize(new WeiBoLoginListener());
    }

    public void WeiXinLogin() {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        api.sendReq(req);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    class WeiBoLoginListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle bundle) {
            mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
            if (mAccessToken.isSessionValid()) {
                AsynHttpImpl.getInstance(LoginActivity.this).userLogin(mAccessToken.getToken(),
                        mAccessToken.getUid(),4,
                        new IAsyncHttpResultCallback() {
                            @Override
                            public void onStart() {
                                Toast.makeText(LoginActivity.this, "onStart", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onSuccess(Object response) {
                                Toast.makeText(LoginActivity.this, "onSuccess", Toast.LENGTH_LONG).show();
                                Log.e("niuco", "onSuccess" + response);

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

                                    DataBaseHelper helper = DataBaseHelper.getHelper(LoginActivity.this);
                                    try {
                                        helper.getUserDao().createIfNotExists(user1);
                                        AccountManager.getInstance(LoginActivity.this).setLogined(true);
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }else{
                                    Log.e("niuco", "baseBean getCode" + baseBean.getCode());
                                }

                            }

                            @Override
                            public void onFailure(Object response) {
                                Toast.makeText(LoginActivity.this, "onFailure", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
        }

        @Override
        public void onCancel() {
        }
    }

    private class QQLoginListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                return;
            }
            JSONObject jsonResponse = (JSONObject)(response) ;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                Log.e("niuco","null");
                return;
            }

            try {
                AsynHttpImpl.getInstance(LoginActivity.this).userLogin(jsonResponse.getString("access_token"),
                        jsonResponse.getString("openid"), 3,new IAsyncHttpResultCallback() {
                            @Override
                            public void onStart() {
                                Log.e("niuco","QQonStart");
                            }

                            @Override
                            public void onSuccess(Object response) {
                                Log.e("niuco",""+response.toString());
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

                                    DataBaseHelper helper = DataBaseHelper.getHelper(LoginActivity.this);
                                    try {
                                        helper.getUserDao().createIfNotExists(user1);
                                        AccountManager.getInstance(LoginActivity.this).setLogined(true);

                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }

                                 }else{
                                    Log.e("niuco", "baseBean getCode" + baseBean.getCode());
                                }
                            }

                            @Override
                            public void onFailure(Object response) {
                                Log.e("niuco","QQonFailure"+response.toString());
                            }
                        });
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            JSONObject jsonResponse = (JSONObject) response;
//            if (null != jsonResponse && jsonResponse.length() == 0) {
////				Toast.makeText(LoginActivity.this, "鏉╂柨娲栨稉铏光敄 閻ц缍嶆径杈Е", Toast.LENGTH_SHORT).show();
//                return;
//            }

            // 鐠囬攱鐪癚Q閸氬氦绻戦崶鐐垫畱token缂佹挻鐎?
            // "ret":0,
            // "pay_token":"xxxxxxxxxxxxxxxx",
            // "pf":"openmobile_android",
            // "expires_in":"7776000",
            // "openid":"xxxxxxxxxxxxxxxxxxx",
            // "pfkey":"xxxxxxxxxxxxxxxxxxx",
            // "msg":"sucess",
            // "access_token":"xxxxxxxxxxxxxxxxxxxxx"

            // client_id = dc039f07e003da02938a5bc4605b5acc
            // 娑撳搫鎸勯崪姘瀻缂侊拷娑旀劗鏌夌捄锟介惃鍒ient_id
//            try {
//                ConfigApi.registeAcctount(LoginActivity.this, jsonResponse.getString("access_token"),
//                        jsonResponse.getString("openid"), jsonResponse.getString("expires_in"),
//                        "dc039f07e003da02938a5bc4605b5acc", "qq2", "nickName", "portrait", "gender",
//                        new IAsyncHttpResultCallback() {
//
//                            @Override
//                            public void onStart() {
//                                loadingDialog = new LoadingDialogFragment();
//                                loadingDialog.show(getFragmentManager(), "LoadingFragment");
//                            }
//
//                            @Override
//                            public void onSuccess(Object response) {
//
//                                Log.e("onSuccess", "" + response);
//                                BaseBean baseBean = JSON.parseObject(response.toString(), BaseBean.class);
//                                UserInfo userIn = baseBean.getUserInfo();
//                                userIn.setLoginType(FitUserType.QQ);
//                                UserInfoDataHelper.saveUserInfo(userIn, LoginActivity.this);
////								Toast.makeText(LoginActivity.this, "onSuccess:" + response.toString(),
////										Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(LoginActivity.this, MainEntryActivity.class);
//
//                                if(loadingDialog.isVisible())
//                                {
//                                    loadingDialog.dismiss();
//                                }
//                                startActivity(intent);
//                                finish();
//                            }
//
//                            @Override
//                            public void onFailure(Object response) {
//                                Log.e("onFailure", "" + response);
////								Toast.makeText(LoginActivity.this, "onFailure:" + response.toString(),
////										Toast.LENGTH_LONG).show();
//                                if(loadingDialog.isVisible())
//                                {
//                                    loadingDialog.dismiss();
//                                }
//                            }
//                        });
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
////			Toast.makeText(LoginActivity.this, "閻ц缍嶉幋鎰:" + response.toString(), Toast.LENGTH_SHORT).show();
//            Log.e("閻ц缍嶉幋鎰:", response.toString());
        }

        @Override
        public void onError(UiError e) {
//			Toast.makeText(LoginActivity.this, "onError", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
//			Toast.makeText(LoginActivity.this, "onCancel", Toast.LENGTH_SHORT).show();
        }

    }

}
