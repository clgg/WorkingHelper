package cn.niuco.library.DataCenter.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

/**
 * Created by 1973 on 2015/4/29.
 */
public class HttpImpl {
    public static void Post(String url,RequestParams params, final IAsyncHttpReultCallback callback){
        AsyncHttpClient client=new AsyncHttpClient();
        client.setTimeout(5000);
        client.addHeader("token",AsynHttpURL.TOKEN);
        client.post(url,params,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String body=new String(responseBody);
                callback.onSuccess(body);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                callback.onFailure(statusCode);
            }
        });
    }
}
