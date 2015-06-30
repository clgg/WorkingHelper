package cn.niuco.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.niuco.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

import cn.niuco.library.DataCenter.http.AsynHttpURL;
import cn.niuco.library.Utils.Utils;
import cn.niuco.library.bean.UserInfoBean;
import cn.niuco.library.bean.UserInfodataBean;

/**
 * Created by 1973 on 2015/5/7.
 */
public class SetUserInfoActivity extends SherlockActivity{
    ImageView iv_head,iv_gender,iv_male,iv_female;
    TextView tv,tv_nickname,tv_des;
    EditText et_nickname,et_des;
    Button bt_updata;
    String url;
    ImageLoader loader;
    DisplayImageOptions options;
    int gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setuserinfo);
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
        et_des= (EditText) findViewById(R.id.et_des);
        et_nickname= (EditText) findViewById(R.id.et_nickname);
        bt_updata= (Button) findViewById(R.id.bt_updata);
        iv_male= (ImageView) findViewById(R.id.iv_male);
        iv_female= (ImageView) findViewById(R.id.iv_female);
        iv_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender=1;
                setgender(1);
            }
        });
        iv_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender=2;
                setgender(2);

            }
        });
        bt_updata.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v) {
            updata();
        }
    });
}

    private void updata() {
        String nickname=et_nickname.getText().toString();
        String des=et_des.getText().toString();
        JSONObject jsonObject  = new JSONObject();
        jsonObject.put("nickName", nickname);
        jsonObject.put("portrait","http://www.niuco.cn/images/header-9.png");
        jsonObject.put("description", des);
        jsonObject.put("gender",gender);

        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity(jsonObject.toString(),"utf-8");
            Log.e("niuco",jsonObject.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        }

        AsyncHttpClient client=new AsyncHttpClient();
        client.setTimeout(5000);
        client.addHeader("token", AsynHttpURL.TOKEN);
        client.post(SetUserInfoActivity.this,"http://zy.lenovomm.com"+"/data/user/update",stringEntity,"application/json",new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("updata","success:"+statusCode+new String(responseBody));
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("updata","failure:"+statusCode+new String(responseBody));
            }
        });
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
                et_nickname.setHint(databean.nickname);
                et_des.setHint(databean.description);
                Utils.displayImage2Circle(iv_head,databean.portrait);
                setgender(databean.gender);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("asfjklasjdfkj22222",""+statusCode);
            }
        });
    }

    private void setgender(int gender) {
        if(gender==1) {
            iv_female.setImageResource(R.drawable.darkfemale);
            iv_male.setImageResource(R.drawable.male);
        }else{
            iv_female.setImageResource(R.drawable.female);
            iv_male.setImageResource(R.drawable.darkmale);
        }
    }

    private void initActionBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ActionBar.LayoutParams lp=new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        View custom= LayoutInflater.from(this).inflate(R.layout .actionbar,null);
        getSupportActionBar().setCustomView(custom,lp);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        tv= (TextView)this.getSupportActionBar().getCustomView().findViewById(R.id.title_tv);
        tv.setText("资料编辑");
    }
}
