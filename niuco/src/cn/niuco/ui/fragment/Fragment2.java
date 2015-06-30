package cn.niuco.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.niuco.R;

import org.apache.http.entity.StringEntity;

import java.util.ArrayList;
import java.util.List;

import cn.niuco.library.AccountManager;
import cn.niuco.library.DataCenter.http.AsynHttpImpl;
import cn.niuco.library.DataCenter.http.IAsyncHttpResultCallback;
import cn.niuco.library.bean.BaseBean;
import cn.niuco.library.db.bean.UserInfo;
import cn.niuco.ui.activity.productdetailActivity;
import cn.niuco.ui.adapter.ProductListAdapter;
import cn.niuco.ui.bean.ProductBrief;

/**
 * Created by 1973 on 2015/3/24.
 */
public class Fragment2 extends SherlockFragment {
    StringEntity stringEntity;
    private PullToRefreshListView mPullRefreshListView;
    private ProductListAdapter productAdapter;
    private ArrayList<ProductBrief> list;
    private ListView actualListView;
    private int currentPageNum = 1;
    private int currentRowNum = 5;
    Button bt1,bt2,bt3;
    public Fragment2(){}
    public static Fragment2 newInstance(){
        return new Fragment2();
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
        UserInfo userInfo = AccountManager.getInstance(getActivity()).getCurrentUserInfo();
        if(userInfo!=null)
        {
            Toast.makeText(getActivity(), "" + userInfo.getNickname(), Toast.LENGTH_LONG).show();
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2,container,false);

        mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        // Set a listener to be invoked when the list should be refreshed.
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if(!list.isEmpty());
                {
                    list.clear();
                }
                AsynHttpImpl.getInstance(getActivity()).getProductList(1,5,new IAsyncHttpResultCallback() {
                    @Override
                    public void onStart() {
                        Log.e("niuco", "MyonStart1112");
                    }

                    @Override
                    public void onSuccess(Object response) {
                        Log.e("niuco","MyonSuccess"+response);
                        BaseBean baseBean = JSON.parseObject(response.toString(), BaseBean.class);

                        if(baseBean.getCode()==200)
                        {
                            List<ProductBrief> productBriefList = baseBean.getData();
                            for(int i=0;i<productBriefList.size();i++)
                            {
                                Log.e("niuco","MyonSuccess"+productBriefList.get(i).getName()+"\n");
                                list.add(productBriefList.get(i));
                            }
                            productAdapter = new ProductListAdapter(getActivity(), list);
                            actualListView.setAdapter(productAdapter);
                            productAdapter.notifyDataSetChanged()   ;
                            mPullRefreshListView.onRefreshComplete();
                        }else{
                            Toast.makeText(getActivity(),"code:"+baseBean.getCode()+baseBean.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Object response) {
                        Log.e("niuco","MyonFailure"+response);
                    }
                });
                Toast.makeText(getActivity(), "刷新成功！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                if(!list.isEmpty())
                {
                    currentPageNum = (list.size()/currentRowNum)+1;
                }else{
                    currentPageNum = 1;
                }

                AsynHttpImpl.getInstance(getActivity()).getProductList(currentPageNum,currentRowNum,new IAsyncHttpResultCallback() {
                    @Override
                    public void onStart() {
                        Log.e("niuco","MyonStart111");
                    }

                    @Override
                    public void onSuccess(Object response) {
                        Log.e("niuco","MyonSuccess"+response);
                        BaseBean baseBean = JSON.parseObject(response.toString(), BaseBean.class);
                        if(baseBean.getCode()==200) {
                            List<ProductBrief> productBriefList = baseBean.getData();
                            for (int i = 0; i < productBriefList.size(); i++) {
                                Log.e("niuco", "MyonSuccess" + productBriefList.get(i).getName() + "\n");
                                list.add(productBriefList.get(i));
                            }
                            productAdapter = new ProductListAdapter(getActivity(), list);

                            productAdapter.notifyDataSetChanged();
                            mPullRefreshListView.onRefreshComplete();
                        }else{
                            Toast.makeText(getActivity(),"code:"+baseBean.getCode()+baseBean.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Object response) {
                        Log.e("niuco","MyonFailure"+response);
                    }
                });

                Toast.makeText(getActivity(), "加载成功！", Toast.LENGTH_SHORT).show();
            }
        });

        actualListView = mPullRefreshListView.getRefreshableView();
//        registerForContextMenu(actualListView);

        list = new ArrayList<>();

        mPullRefreshListView.setRefreshing(false);

        mPullRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent= new Intent();
                intent.setClass(getActivity(),productdetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("product_id",list.get(position-1).getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    /*  登录
      Button tv2= (Button) view.findViewById(R.id.tv_signin);
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpClient client= new AsyncHttpClient();
                client.setTimeout(5000);
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("accessToken","18810617881");
                jsonObject.put("scope","3273099");
                jsonObject.put("authType",2);
                try {
                    stringEntity=new StringEntity(jsonObject.toString(),"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                client.post(getActivity(), AsynHttpURL.LOGIN,stringEntity,"application/json",new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.e("loginbody",""+new String(responseBody));
                        MyLoginBean mbb= JSON.parseObject(new String(responseBody),MyLoginBean.class);
                        if (mbb.code==200){
                            AsynHttpURL.TOKEN=mbb.data.token;
                            Log.e("token",mbb.data.token);
                            Toast.makeText(getActivity(),"登录成功",Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
            }
        });*/

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
