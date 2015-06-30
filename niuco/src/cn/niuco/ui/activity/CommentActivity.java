package cn.niuco.ui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
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
import cn.niuco.library.bean.CommentBean;
import cn.niuco.library.bean.MyBaseBean;
import cn.niuco.library.bean.ProductdataBean;
import cn.niuco.ui.adapter.CommentListAdapter;


public class CommentActivity extends SherlockActivity {
    int productId;
    Button bt_comment;
    EditText et_comment;
    PullToRefreshListView lv_comment;
    StringEntity stringEntity;
    ListView mylv;
    ArrayList<CommentBean> arrayList;
    ArrayList<CommentBean> newarrayList;
    CommentListAdapter commentListAdapter;
    private int currentPageNum = 1;
    private int currentRowNum = 5;
    String url = "http://zy.lenovomm.com/data/product/info/";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        productId = getIntent().getIntExtra("productId", 0);
        Log.e("productId", "" + productId);
        initview();
        setlistview();
        // request(url + productId);

    }

    private void setlistview() {
        lv_comment.setMode(PullToRefreshBase.Mode.BOTH);
       /* commentListAdapter = new CommentListAdapter(CommentActivity.this);
        commentListAdapter.setData(arrayList);
        mylv.setAdapter();*/
        lv_comment.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                AsyncHttpClient client=new AsyncHttpClient();
                client.setTimeout(10000);
                client.addHeader("token", AsynHttpURL.TOKEN);
                String listurl=AsynHttpURL.COMMENT_LIST+"/"+1+"/"+productId+"/"+currentPageNum+"/"+currentRowNum;
                Log.e("usercomment","start"+listurl);
                client.post(listurl, new RequestParams(), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.e("usercomment:","code:"+statusCode);
                        Log.e("usercomment",new String(responseBody));
                        MyBaseBean product = JSON.parseObject(new String(responseBody), MyBaseBean.class);
                        arrayList = JSON.parseObject(product.data, new TypeReference<ArrayList<CommentBean>>() {
                        });
                        commentListAdapter = new CommentListAdapter(CommentActivity.this);
                        commentListAdapter.setData(arrayList,productId);
                        mylv.setAdapter(commentListAdapter);
                        commentListAdapter.notifyDataSetChanged();
                        lv_comment.onRefreshComplete();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(statusCode==200) {
                            Log.e("usercomment", "failure" + statusCode + new String(responseBody));
                            lv_comment.onRefreshComplete();
                        }
                    }
                });
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if(!arrayList.isEmpty())
                {
                    currentPageNum = (arrayList.size()/currentRowNum)+1;
                }else{
                    currentPageNum = 1;
                }
                AsyncHttpClient client=new AsyncHttpClient();
                client.setTimeout(5000);
                client.addHeader("token",AsynHttpURL.TOKEN);
                String newurl=AsynHttpURL.COMMENT_LIST+"/"+1+"/"+productId+"/"+currentPageNum+"/"+currentRowNum;
                Log.e("usercomment","start"+newurl);
                client.post(CommentActivity.this,newurl,new RequestParams(),new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.e("uprefresh",""+new String(responseBody));
                        MyBaseBean product = JSON.parseObject(new String(responseBody), MyBaseBean.class);
                        newarrayList = JSON.parseObject(product.data, new TypeReference<ArrayList<CommentBean>>() {
                        });
                        arrayList.addAll(newarrayList);
                        CommentListAdapter newproductAdapter = new CommentListAdapter(CommentActivity.this);
                        newproductAdapter.setData(newarrayList,productId);

                        newproductAdapter.notifyDataSetChanged();
                        lv_comment.onRefreshComplete();


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.e("uprefreshfailure",""+statusCode);
                    }
                });
            }
        });
        mylv=lv_comment.getRefreshableView();
        lv_comment.setRefreshing(false);


    }

    private void request(String pidurl) {
        AsyncHttpClient client=new AsyncHttpClient();
        client.setTimeout(5000);
        client.addHeader("token",AsynHttpURL.TOKEN);
        client.post(pidurl, new RequestParams(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                MyBaseBean product = JSON.parseObject(new String(responseBody), MyBaseBean.class);
                ProductdataBean data = JSON.parseObject(product.data, ProductdataBean.class);
                ArrayList<CommentBean> arrayList = JSON.parseObject(data.comment, new TypeReference<ArrayList<CommentBean>>() {
                });
                commentListAdapter = new CommentListAdapter(CommentActivity.this);
                commentListAdapter.setData(arrayList,productId);
                //lv_comment.setAdapter(commentListAdapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void initview() {
        lv_comment = (PullToRefreshListView) findViewById(R.id.lv_comment);
        et_comment = (EditText) findViewById(R.id.et_comment);
        bt_comment = (Button) findViewById(R.id.bt_comment);
        bt_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable comment = et_comment.getText();
                if (comment.toString() != null) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("targetID", productId);
                    jsonObject.put("targetType", "1");
                    jsonObject.put("content", "asdfasdfas");
                    try {
                        stringEntity = new StringEntity(jsonObject.toString(), "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    AsyncHttpClient client = new AsyncHttpClient();
                    client.setTimeout(5000);
                    Log.e("commentinfo:jo", "" + jsonObject.toString());
                    Log.e("commentinfo:url", "" + AsynHttpURL.UPDATE_COMMENT);
                    client.addHeader("token", AsynHttpURL.TOKEN);
                    client.post(CommentActivity.this, AsynHttpURL.UPDATE_COMMENT, stringEntity, "application/json", new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Log.e("commentinfo", "" + statusCode + new String(responseBody).toString());
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });
                }
            }
        });
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


}
