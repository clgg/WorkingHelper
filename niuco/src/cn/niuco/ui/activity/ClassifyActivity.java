package cn.niuco.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.niuco.R;


import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cn.niuco.library.DataCenter.http.AsynHttpURL;
import cn.niuco.library.bean.ClassifyBean;
import cn.niuco.library.bean.ClassifyProductBean;
import cn.niuco.ui.adapter.ClassifyProductListAdapter;


public class ClassifyActivity extends SherlockActivity implements View.OnClickListener{
    Button bt_1,bt_2,bt_3,bt_4;
    PullToRefreshListView lv_refresh;
    StringEntity stringEntity;
    ListView mylv;
    ClassifyProductListAdapter productListAdapter;
    ArrayList<ClassifyBean> arrayList;
    ArrayList<ClassifyBean> newarrayList;
    private int currentPageNum = 1;
    private int currentRowNum = 5;
    int mun=1;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify);
        initview();
        setlistview();
        // request(url + productId);

    }

    private void setlistview() {
        lv_refresh.setMode(PullToRefreshBase.Mode.BOTH);
        lv_refresh.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                AsyncHttpClient client = new AsyncHttpClient();
                client.setTimeout(10000);
                client.addHeader("token", AsynHttpURL.TOKEN);
                String listurl = AsynHttpURL.CATEGORY_LIST;
                Log.e("usercomment", "start" + listurl);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("cid",mun);
                Log.e("cid",""+mun);
                try {
                    StringEntity stringEntity=new StringEntity(jsonObject.toString(),"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                client.post(ClassifyActivity.this,listurl,stringEntity,"application/json", new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.e("usercomment:", "code:" + statusCode);
                        Log.e("usercomment", new String(responseBody));
                        ClassifyProductBean product = JSON.parseObject(new String(responseBody), ClassifyProductBean.class);
                        arrayList =product.data;
                        productListAdapter = new ClassifyProductListAdapter(ClassifyActivity.this,arrayList);
                        mylv.setAdapter(productListAdapter);
                        productListAdapter.notifyDataSetChanged();
                        lv_refresh.onRefreshComplete();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if (statusCode == 200) {
                            Log.e("usercomment", "failure" + statusCode + new String(responseBody));
                            lv_refresh.onRefreshComplete();
                        }
                    }
                });
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (!arrayList.isEmpty()) {
                    currentPageNum = (arrayList.size() / currentRowNum) + 1;
                } else {
                    currentPageNum = 1;
                }
                AsyncHttpClient client = new AsyncHttpClient();
                client.setTimeout(5000);
                client.addHeader("token", AsynHttpURL.TOKEN);
                String newurl = AsynHttpURL.CATEGORY_LIST;
                Log.e("usercomment", "start" + newurl);
                client.post(ClassifyActivity.this, newurl, new RequestParams(), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.e("uprefresh", "" + new String(responseBody));
                        ClassifyProductBean product = JSON.parseObject(new String(responseBody), ClassifyProductBean.class);
                        arrayList.addAll(product.data);
                        ClassifyProductListAdapter newproductAdapter = new ClassifyProductListAdapter(ClassifyActivity.this,newarrayList);

                        newproductAdapter.notifyDataSetChanged();
                        lv_refresh.onRefreshComplete();


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.e("uprefreshfailure", "" + statusCode);
                    }
                });
            }
        });
        lv_refresh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent();
                intent.setClass(ClassifyActivity.this,productdetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("product_id",arrayList.get(position-1).id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        mylv=lv_refresh.getRefreshableView();
        lv_refresh.setRefreshing(false);


    }


    private void initview() {
        lv_refresh = (PullToRefreshListView) findViewById(R.id.lv_refresh);
        bt_1= (Button) findViewById(R.id.bt_1);
        bt_2= (Button) findViewById(R.id.bt_2);
        bt_3= (Button) findViewById(R.id.bt_3);
        bt_4= (Button) findViewById(R.id.bt_4);
        bt_1.setOnClickListener(this);
        bt_2.setOnClickListener(this);
        bt_3.setOnClickListener(this);
        bt_4.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = getSupportMenuInflater();
        inflator.inflate(R.menu.user_head_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_1:
                mun=1;
                lv_refresh.setRefreshing(false);
                break;
            case R.id.bt_2:
                mun=2;
                lv_refresh.setRefreshing(false);
                break;
            case R.id.bt_3:
                mun=3;
                lv_refresh.setRefreshing(false);
                break;
            case R.id.bt_4:
                mun=4;
                lv_refresh.setRefreshing(false);
                break;
        }

    }
}
