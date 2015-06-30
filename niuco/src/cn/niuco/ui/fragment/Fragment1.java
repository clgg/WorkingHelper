package cn.niuco.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.niuco.R;

import org.apache.http.Header;

import java.util.ArrayList;

import cn.niuco.library.DataCenter.http.AsynHttpURL;
import cn.niuco.library.bean.MyBaseBean;
import cn.niuco.library.bean.PKinfoBean;
import cn.niuco.ui.adapter.RecyclerAdapter;


/**
 * Created by 1973 on 2015/3/24.
 */
public class Fragment1 extends SherlockFragment {
    TextView tv;
    int width;
    int length;
    ArrayList<PKinfoBean> list;
    RecyclerView rv;
    LinearLayoutManager mLayoutManager;
  /*  HorizontalListView hlv;
    HorizontalListAdapter horizontalListAdapter;*/

    private static String[] TITLE={"first","second"};
    private RecyclerAdapter mAdapter;
    private SwipeRefreshLayout sr;

    public Fragment1(){}
    public static Fragment1 newInstance(){
        return new Fragment1();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        /*menu.add("Save")
                .setIcon(R.drawable.ic_launcher)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);*/
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
       // horizontalListAdapter=new HorizontalListAdapter(getActivity());
        list=new ArrayList<>();
        WindowManager wm= (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        width=wm.getDefaultDisplay().getWidth();
        //request();

    }

    private void request() {
        AsyncHttpClient client=new AsyncHttpClient();
        client.setTimeout(5000);
        client.addHeader("token", AsynHttpURL.TOKEN);
        client.post(getActivity(),AsynHttpURL.PK_LIST+"/"+"1"+"/"+"5",new RequestParams(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e("pklist", "" + new String(responseBody));
                MyBaseBean mbb= JSON.parseObject(new String(responseBody),MyBaseBean.class);
                list=JSON.parseObject(mbb.data,new TypeReference<ArrayList<PKinfoBean>>(){});
                list.add(0,new PKinfoBean(11111));
                list.add(list.size(),new PKinfoBean(99999));
               /* horizontalListAdapter.setData(list);
                hlv.setAdapter(horizontalListAdapter);
                hlv.setOnItemMoveListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        Log.e("myontouchlis","  "+event.getAction());
                        return false;
                    }
                });*/
                mAdapter.setlist(list);
                mAdapter.notifyDataSetChanged();

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e("pklist","failure"+statusCode);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        request();
        View view=inflater.inflate(R.layout.fragment1,container,false);
        sr= (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            }
        });
        rv= (RecyclerView) view.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(mLayoutManager);
        mAdapter= new RecyclerAdapter(getActivity(),list,width);
        rv.setAdapter(mAdapter);
        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(int i) {
                Log.e("onScrollState",":::"+i);
                Log.e("getVisibility",":::"+  mLayoutManager.findFirstVisibleItemPosition());
                if(i==0){
                    //mLayoutManager.scrollToPosition(mLayoutManager.findFirstVisibleItemPosition());
                    int t= mLayoutManager.findFirstVisibleItemPosition();
                    Log.e("postition",""+t);
                    View v=mLayoutManager.findViewByPosition(t);
                    int viewwidth=v.getWidth();
                    int[] location = new int[2];
                    v.getLocationOnScreen(location);
                    int x = location[0];
                    Log.e("onscreen",":"+x);
                    if (t==0){
                        length=-Math.abs(x);
                    }else if (Math.abs(x)<(viewwidth/2)){
                        //右移
                         length=-((width-viewwidth)/2+Math.abs(x));
                    }else{
                        //左移
                         length=-(Math.abs(x)+(width-viewwidth)/2-viewwidth);
                    }
                    Log.e("mylength",""+width+":"+viewwidth+":"+x);
                    Log.e("position0",":"+mLayoutManager.findViewByPosition(t).getWidth());
                    Log.e("length",":"+length);
                    //int scroll=mLayoutManager.scrollHorizontallyBy(length,null, new RecyclerView.State());
                   // rv.scrollBy(length, 0);
                    rv.smoothScrollBy(length,0);

                    /*long x=v.getLeft();
                    Log.e("lefttt",":"+x);*/
                    //mLayoutManager.smoothScrollToPosition(new RecyclerView(getActivity()),new RecyclerView.State(),mLayoutManager.findFirstVisibleItemPosition());
                }
            }

            @Override
            public void onScrolled(int i, int i2) {
                Log.e("onScrolled",":::"+i+"::"+i2);
            }
        });
        //如果我们要对ViewPager设置监听，用indicator设置就行了
      //  hlv= (HorizontalListView) view.findViewById(R.id.hlv_pk);
        for(int i=0;i<8;i++) {
            PKinfoBean pb = new PKinfoBean(i);
            list.add(pb);
        }
        /*  list.add(0,new PKinfoBean(0));
            list.add(list.size()-1,new PKinfoBean(0));*//*
        }*/


       /* horizontalListAdapter.setData(list);
        hlv.setAdapter(horizontalListAdapter);
        hlv.setOnScrollListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("myonscroll", "" + event.getAction()+hlv);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.e("stopp", "" + hlv.getSelectedItemPosition());
                    Log.e("stopp2", "" + hlv.getFirstVisiblePosition());
                    //Log.e("stopp", "" + hlv.getSelectedItemPosition());
                }
                return false;
            }
        });
        hlv.scrollTo(44);*/



    return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        //  getSherlockActivity().getSupportActionBar().setIcon(R.drawable.abs__ic_ab_back_holo_light);
        // getSherlockActivity().getSupportActionBar().setTitle("运动");
        getSherlockActivity().getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ActionBar.LayoutParams lp=new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        View custom=LayoutInflater.from(getActivity()).inflate(R.layout .actionbar,null);
        getSherlockActivity().getSupportActionBar().setCustomView(custom,lp);
        getSherlockActivity().getSupportActionBar().setDisplayShowCustomEnabled(true);
        tv= (TextView) getSherlockActivity().getSupportActionBar().getCustomView().findViewById(R.id.title_tv);
        tv.setText("PKlist");

    }
   /* class TabPageIndicatorAdapter extends FragmentPagerAdapter {
        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            //新建一个Fragment来展示ViewPager item的内容，并传递参数
            Fragment fragment = new ItemFragment();
            Bundle args = new Bundle();
            args.putString("arg", TITLE[position]);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLE[position % TITLE.length];
        }

        @Override
        public int getCount() {
            return TITLE.length;
        }
    }*/



}
