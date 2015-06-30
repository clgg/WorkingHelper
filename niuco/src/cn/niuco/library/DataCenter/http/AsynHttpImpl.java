package cn.niuco.library.DataCenter.http;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

public class AsynHttpImpl {
    public static final int SOCKET_CONNECTION_TIMEOUT = 10 * 1000;
    private static AsyncHttpClient client = null;
    private static AsynHttpImpl mInstance;
    private static Context mContext;

    private AsynHttpImpl(Context context) {
        if (context == null) {
        }
    }

    public static AsynHttpImpl getInstance(Context context) {
        mContext = context;
        if (mInstance == null) {
            client = new AsyncHttpClient();
            client.setTimeout(SOCKET_CONNECTION_TIMEOUT);
            mInstance = new AsynHttpImpl(context);
        }
        return mInstance;
    }

    public void getProductList(int pageNum, int rowNum, final IAsyncHttpResultCallback callback) {
        final RequestParams requestParams = new RequestParams();

        final String URL = AsynHttpURL.GET_PRODUCT_LIST + "/" + pageNum + "/" + rowNum;
        client.post(URL, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                callback.onStart();
                Log.e("niuco", "onStart" + URL);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                callback.onSuccess(result);
                Log.e("niuco", "onSuccess" + result);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String result = String.valueOf(statusCode);
                callback.onFailure(result);
                Log.e("niuco", "onFailure" + statusCode);
            }

        });

    }

    public void userLogin(String accessToken, String scope, int authType, final IAsyncHttpResultCallback callback) {
        JSONObject jsonObject  = new JSONObject();
        jsonObject.put("accessToken", accessToken);
        jsonObject.put("scope", scope);
        jsonObject.put("authType", authType);

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(jsonObject.toString());
            Log.e("niuco",jsonObject.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String URL = AsynHttpURL.USER_LOGIN + "/";

        client.post(mContext, URL, stringEntity, "application/json", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                callback.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e("niuco", "onSuccess" + statusCode);
                String result = new String(responseBody);
                callback.onSuccess(result);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String result = String.valueOf(statusCode);
                callback.onFailure(result);
                Log.e("niuco", "onFailure" + statusCode);
            }
        });

    }

    public void getWeChatAccessTokenByCodeFun(String appID, String secret, String code, final IAsyncHttpResultCallback callback) {
        final RequestParams requestParams = new RequestParams();
        requestParams.put("appid", appID);
        requestParams.put("secret", secret);
        requestParams.put("code", code);
        requestParams.put("grant_type", "authorization_code");

        final String URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
        client.post(URL, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                callback.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                callback.onSuccess(new String(responseBody));

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                callback.onFailure(new String(responseBody));
            }
        });

    }


}
