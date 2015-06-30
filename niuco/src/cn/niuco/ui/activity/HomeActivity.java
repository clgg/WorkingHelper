package cn.niuco.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lenovo.share.constant.Platform;
import com.lenovo.share.control.ShareController;
import com.lenovo.share.platform.QQPlatform;
import com.lenovo.share.platform.QQzonePlatform;
import com.lenovo.share.platform.SinaPlatform;
import com.lenovo.share.platform.WeiXinCirclePlatform;
import com.lenovo.share.platform.WeiXinPlatform;
import com.niuco.R;

import java.util.ArrayList;
import java.util.List;

import cn.niuco.library.AccountManager;
import cn.niuco.library.DataCenter.http.AsynHttpImpl;
import cn.niuco.library.DataCenter.http.IAsyncHttpResultCallback;
import cn.niuco.library.bean.BaseBean;
import cn.niuco.library.db.bean.UserInfo;
import cn.niuco.ui.adapter.ProductListAdapter;
import cn.niuco.ui.bean.ProductBrief;


public class HomeActivity extends SherlockActivity {

    private PullToRefreshListView mPullRefreshListView;
    private ProductListAdapter productAdapter;
    private ArrayList<ProductBrief> list;
    private ListView actualListView;
    private int currentPageNum = 1;
    private int currentRowNum = 5;
    Button bt1,bt2,bt3;

    private ImageView imgLogin;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        UserInfo userInfo = AccountManager.getInstance(HomeActivity.this).getCurrentUserInfo();
        if(userInfo!=null)
        {
            Toast.makeText(HomeActivity.this,""+userInfo.getNickname(),Toast.LENGTH_LONG).show();
        }

        bt2= (Button) findViewById(R.id.bt2);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomeActivity.this,ClassifyActivity.class);
                startActivity(intent);
            }
        });

        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        // Set a listener to be invoked when the list should be refreshed.
        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if(!list.isEmpty());
                {
                    list.clear();
                }
                AsynHttpImpl.getInstance(HomeActivity.this).getProductList(1,5,new IAsyncHttpResultCallback() {
                    @Override
                    public void onStart() {
                        Log.e("niuco","MyonStart1112");
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
                            productAdapter = new ProductListAdapter(HomeActivity.this, list);
                            actualListView.setAdapter(productAdapter);
                            productAdapter.notifyDataSetChanged()   ;
                            mPullRefreshListView.onRefreshComplete();
                        }else{
                            Toast.makeText(HomeActivity.this,"code:"+baseBean.getCode()+baseBean.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Object response) {
                        Log.e("niuco","MyonFailure"+response);
                    }
                });
                Toast.makeText(HomeActivity.this, "刷新成功！", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                if(!list.isEmpty())
                {
                    currentPageNum = (list.size()/currentRowNum)+1;
                }else{
                    currentPageNum = 1;
                }

                AsynHttpImpl.getInstance(HomeActivity.this).getProductList(currentPageNum,currentRowNum,new IAsyncHttpResultCallback() {
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
                            productAdapter = new ProductListAdapter(HomeActivity.this, list);

                            productAdapter.notifyDataSetChanged();
                            mPullRefreshListView.onRefreshComplete();
                        }else{
                            Toast.makeText(HomeActivity.this,"code:"+baseBean.getCode()+baseBean.getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Object response) {
                        Log.e("niuco","MyonFailure"+response);
                    }
                });

                Toast.makeText(HomeActivity.this, "加载成功！", Toast.LENGTH_SHORT).show();
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
                intent.setClass(HomeActivity.this,productdetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("product_id",list.get(position-1).getId());
                intent.putExtras(bundle);
                startActivity(intent);
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
        switch(item.getItemId()){
            case R.id.user_login_head_menu:
                Intent intent = new Intent();
                intent.setClass(HomeActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.share_head_menu:
                ShareController.initPlatform(Platform.QQ, Platform.QQZone,
                        Platform.Sina, Platform.WeiXinFriend,
                        Platform.WeiXinFriendCircle);
                // 设置对应的申请到的APP_ID
                QQPlatform.setQQAppId("1103996426");
                SinaPlatform.setWeiBoAppKey("169355841");
                WeiXinPlatform.setWeiXinAppID("wx76c6f17baba3a218");
                // 分享文字到新浪微博
                SinaPlatform.shareTextToSina("Hello everybody.");
                // QQ好友分享
                QQPlatform.shareQQDefaultType(
                                        "默认分享的标题",
                                        "默认分享的内容。。。。。。",
                                        "http://www.baidu.com",
                                        false,
                                        "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif",
                                        false);

                // 分享应用信息到QQ空间
                ArrayList<String> imgList = new ArrayList<String>();
                imgList.add("http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
                imgList.add("http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
                imgList.add("http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
                QQzonePlatform.shareQQZoneApp("分享应用title", "分享应用内容", imgList);

                WeiXinPlatform.shareWebsiteToWeiXinFriend(HomeActivity.this,
                        "http://www.baidu.com", "分享网页title", "分享网页的内容描述......",
                        R.drawable.weixinfriend_pic);

                WeiXinCirclePlatform.shareMusicToWeiXinFriendCircle(
                        HomeActivity.this, "http://www.hao123.com", "音乐的title",
                        "音乐的描述。。。。", R.drawable.weixinfriend_pic);
                // 创建分享面板
                ShareController.createBottomDialog(this);

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }



}
