package cn.niuco.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
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
import cn.niuco.ui.activity.LoginActivity;

/**
 * Created by 1973 on 2015/3/24.
 */
public class Fragment3 extends SherlockFragment{
    private Button bt_login;
    private LinearLayout ll_userinfo,ll_login;
    ImageView iv_head,iv_gender;
    TextView tv,tv_nickname,tv_des;
    String url="http://zy.lenovomm.com"+"/data/user/info/186926";
    ImageLoader loader;
    DisplayImageOptions options;
    SharedPreferences sp;
    Boolean loginsata;

    private boolean isShow = false;
    public Fragment3(){}
    public static Fragment3 newInstance(){
        return new Fragment3();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        sp=getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
        loginsata=sp.getBoolean("loginstatu",false);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3,container,false);
        ll_userinfo= (LinearLayout) view.findViewById(R.id.ll_userinfo);
        ll_login= (LinearLayout) view.findViewById(R.id.ll_login);
        bt_login= (Button) view.findViewById(R.id.bt_login);
        iv_head= (ImageView)view.findViewById(R.id.iv_head);
        tv_nickname= (TextView) view.findViewById(R.id.tv_nickname);
        tv_des= (TextView) view.findViewById(R.id.tv_des);
        iv_gender= (ImageView) view.findViewById(R.id.iv_gender);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
            }
        });
        if (loginsata){
            ll_userinfo.setVisibility(View.VISIBLE);
            ll_login.setVisibility(View.GONE);
            requestinfo();
        }else{
            ll_userinfo.setVisibility(View.GONE);
            ll_login.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void requestinfo() {
        loader= ImageLoader.getInstance();
        loader.init(ImageLoaderConfiguration.createDefault(getActivity()));
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
                Log.d("asfjklasjdfkj", new String(responseBody));
                UserInfoBean userinfo = JSON.parseObject(new String(responseBody), UserInfoBean.class);
                String data= userinfo.data;
                UserInfodataBean databean= JSON.parseObject(data,UserInfodataBean.class);
                tv_nickname.setText(databean.nickname);
                tv_des.setText(databean.description);
                Utils.displayImage2Circle(iv_head, databean.portrait);



            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("asfjklasjdfkj22222",""+statusCode);
            }
        });
    }



}
