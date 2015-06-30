package cn.niuco.ui.activity;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.niuco.R;

import org.apache.http.Header;

import cn.niuco.library.DataCenter.http.AsynHttpURL;

/**
 * Created by 1973 on 2015/4/27.
 */
public class PKlistActivity extends SherlockFragmentActivity{
    TextView tv,tv2;
    Button bt1,bt2,bt3,bt4,bt5;
    ListView lv_pk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pklist);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        initview();
        initActionBar();
        request();
    }
    private void request() {
        AsyncHttpClient client=new AsyncHttpClient();
        client.setTimeout(5000);
        client.addHeader("token", AsynHttpURL.TOKEN);
        client.post(PKlistActivity.this,AsynHttpURL.PK_LIST+"/"+"1"+"/"+"5",new RequestParams(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e("pklist",""+new String(responseBody));
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("pklist","failure"+statusCode);
            }
        });
    }

    private void initview() {
        lv_pk= (ListView) findViewById(R.id.lv_pk);
    }
    private void initActionBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ActionBar.LayoutParams lp=new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        View custom= LayoutInflater.from(this).inflate(R.layout .actionbar,null);
        getSupportActionBar().setCustomView(custom,lp);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        tv= (TextView)this.getSupportActionBar().getCustomView().findViewById(R.id.title_tv);
        tv.setText("Niuco");
    }
}
