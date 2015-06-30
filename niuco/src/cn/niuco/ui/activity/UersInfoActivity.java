package cn.niuco.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.niuco.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.Header;

import cn.niuco.library.Utils.Utils;
import cn.niuco.library.bean.UserInfoBean;
import cn.niuco.library.bean.UserInfodataBean;

/**
 * Created by 1973 on 2015/5/7.
 */
public class UersInfoActivity extends SherlockActivity{
    ImageView iv_head,iv_gender;
    TextView tv,tv_nickname,tv_des;
    String url;
    ImageLoader loader;
    DisplayImageOptions options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        url="http://zy.lenovomm.com"+"/data/user/info/186926";
        initview();
        initActionBar();
        requestinfo();

    }

    private void initview() {
        iv_head= (ImageView)findViewById(R.id.iv_head);
        tv_nickname= (TextView) findViewById(R.id.tv_nickname);
        tv_des= (TextView) findViewById(R.id.tv_des);
        iv_gender= (ImageView) findViewById(R.id.iv_gender);
    }

    private void requestinfo() {
        loader=ImageLoader.getInstance();
        loader.init(ImageLoaderConfiguration.createDefault(this));
        options= new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_launcher)
                .showImageOnLoading(R.drawable.ic_launcher)
                        //.showImageForEmptyUri(R.drawable.pic_success)
                        //.showImageOnFail(R.drawable.pic_success)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        AsyncHttpClient client=new AsyncHttpClient();
        client.setTimeout(5000);
        //client.addHeader("token",AsynHttpURL.TOKEN);
        client.post(url,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("asfjklasjdfkj",new String(responseBody));
                UserInfoBean userinfo =JSON.parseObject(new String(responseBody), UserInfoBean.class);
                String data= userinfo.data;
                UserInfodataBean databean= JSON.parseObject(data,UserInfodataBean.class);
                tv_nickname.setText(databean.nickname);
                tv_des.setText(databean.description);
                Utils.displayImage2Circle(iv_head,databean.portrait);



            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("asfjklasjdfkj22222",""+statusCode);
            }
        });
    }

    private void initActionBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ActionBar.LayoutParams lp=new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        View custom= LayoutInflater.from(this).inflate(R.layout .actionbar,null);
        getSupportActionBar().setCustomView(custom,lp);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        tv= (TextView)this.getSupportActionBar().getCustomView().findViewById(R.id.title_tv);
        tv.setText("我的主页");
    }
}
