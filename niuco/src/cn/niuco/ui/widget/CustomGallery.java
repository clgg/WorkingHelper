package cn.niuco.ui.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.RequestParams;
import com.niuco.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import cn.niuco.library.DataCenter.http.HttpImpl;
import cn.niuco.library.DataCenter.http.IAsyncHttpReultCallback;
import cn.niuco.library.Utils.Utils;
import cn.niuco.library.bean.MyBaseBean;
import cn.niuco.library.bean.ProductdataBean;


/**
 * ViewPager实现的轮播图广告自定义视图，如京东首页的广告轮播图效果；
 * 既支持自动轮播页面也支持手势滑动切换页面
 *
 *
 */

public class CustomGallery extends FrameLayout {

    // 使用image-loader插件读取网络图片
    private ImageLoader imageLoader = ImageLoader.getInstance();

    //轮播图片数量
    private final static int IMAGE_COUNT = 5;
    //自动轮播的时间间隔
    private final static int TIME_INTERVAL = 5;
    //自动轮播启用开关
    private final static boolean isAutoPlay = false;

    //自定义轮播图的资源
    private String[] imageUrls;
    //放轮播图片的ImageView 的list
    private List<ImageView> imageViewsList;
    //放圆点的View的list
    private List<View> dotViewsList;

    private ViewPager viewPager;
    //当前轮播页
    private int currentItem  = 0;
    //定时任务
    private ScheduledExecutorService scheduledExecutorService;

    private Context context;

    //Handler
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            viewPager.setCurrentItem(currentItem);
        }

    };

    public CustomGallery(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }
    public CustomGallery(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }
    public CustomGallery(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;

        initImageLoader(context);

        initData();

        if(isAutoPlay){
            startPlay();
        }


    }
    /**
     * 开始轮播图切换
     */
    private void startPlay(){
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, TIME_INTERVAL, TimeUnit.SECONDS);
    }
    /**
     * 停止轮播图切换
     */
    private void stopPlay(){
        scheduledExecutorService.shutdown();
    }
    /**
     * 初始化相关Data
     */
    private void initData(){
        imageViewsList = new ArrayList<ImageView>();
        dotViewsList = new ArrayList<View>();

        // 一步任务获取图片
      //  GetList("http://zy.lenovomm.com/data/product/info/1800");
    }
    /**
     * 初始化Views等UI
     */
    private void initUI(Context context){
        if(imageUrls == null || imageUrls.length == 0)
            return;

        LayoutInflater.from(context).inflate(R.layout.customgallery, this, true);

        LinearLayout dotLayout = (LinearLayout)findViewById(R.id.dotLayout);
        dotLayout.removeAllViews();

        // 热点个数与图片特殊相等
        for (int i = 0; i < imageUrls.length; i++) {
            ImageView view =  new ImageView(context);
            view.setTag(imageUrls[i]);
            if(i==0)//给一个默认图
                view.setBackgroundResource(R.drawable.abs__ab_bottom_solid_dark_holo);
            view.setScaleType(ScaleType.FIT_XY);
            imageViewsList.add(view);

            ImageView dotView =  new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
            params.leftMargin = 4;
            params.rightMargin = 4;
            dotLayout.addView(dotView, params);
            dotViewsList.add(dotView);
        }

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setFocusable(true);
        MyPagerAdapter myadapter=new MyPagerAdapter();
        viewPager.setAdapter(myadapter);
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
        setdot(0);
    }

    /**
     * 填充ViewPager的页面适配器
     *
     */
    private class MyPagerAdapter  extends PagerAdapter{

        @Override
        public void destroyItem(View container, int position, Object object) {
            // TODO Auto-generated method stub
            //((ViewPag.er)container).removeView((View)object);
            ((ViewPager)container).removeView(imageViewsList.get(position));
        }

        @Override
        public Object instantiateItem(View container, final int position) {
            ImageView imageView = imageViewsList.get(position);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("aasfasfgg","onclick"+position);
                }
            });

            imageLoader.displayImage(imageView.getTag() + "", imageView);

            ((ViewPager)container).addView(imageViewsList.get(position));
            return imageViewsList.get(position);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return imageViewsList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }
        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void finishUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

    }
    /**
     * ViewPager的监听器
     * 当ViewPager中页面的状态发生改变时调用
     *
     */
    private class MyPageChangeListener implements OnPageChangeListener{

        boolean isAutoPlay = false;

        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
            switch (arg0) {
                case 1:// 手势滑动，空闲中
                    isAutoPlay = false;
                    break;
                case 2:// 界面切换中
                    isAutoPlay = true;
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                   /* // 当前为最后一张，此时从右向左滑，则切换到第一张
                    if (viewPager.getCurrentItem() == viewPager.getAdapter().getCount() - 1 && !isAutoPlay) {
                        viewPager.setCurrentItem(0);
                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
                    else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
                        viewPager.setCurrentItem(viewPager.getAdapter().getCount() - 1);
                    }*/
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onPageSelected(int pos) {
            // TODO Auto-generated method stub

            setdot(pos);
        }

    }

    private void setdot(int pos) {
        currentItem = pos;
        for(int i=0;i < dotViewsList.size();i++){
            if(i == pos){
                ((View)dotViewsList.get(pos)).setBackgroundResource(R.drawable.abs__ic_clear);
            }else {
                ((View)dotViewsList.get(i)).setBackgroundResource(R.drawable.abs__ic_go);
            }
        }
    }

    /**
     *执行轮播图切换任务
     *
     */
    private class SlideShowTask implements Runnable{

        @Override
        public void run() {
            // TODO Auto-generated method stub
            synchronized (viewPager) {
                currentItem = (currentItem+1)%imageViewsList.size();
                handler.obtainMessage().sendToTarget();
            }
        }

    }

    /**
     * 销毁ImageView资源，回收内存
     *
     */
    private void destoryBitmaps() {

        for (int i = 0; i < IMAGE_COUNT; i++) {
            ImageView imageView = imageViewsList.get(i);
            Drawable drawable = imageView.getDrawable();
            if (drawable != null) {
                //解除drawable对view的引用
                drawable.setCallback(null);
            }
        }
    }


    /**
     * 异步任务,获取数据
     *
     */
    public void GetList(String url){
        HttpImpl.Post(url, new RequestParams(), new IAsyncHttpReultCallback() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Object response) {
                String jsonstring = (String) response;
                try {
                    MyBaseBean product= JSON.parseObject(jsonstring, MyBaseBean.class);
                    ProductdataBean data=JSON.parseObject(product.data,ProductdataBean.class);
                    String introduction=data.introduction;
                    List detailinfo= Utils.getlist(introduction);
                    //panned sp= Html.fromHtml(introduction);
                    String photo=data.photo;
                    photo=photo.substring(1,photo.length()-2);
                    String [] aa=photo.split(",");
                    imageUrls=new String[aa.length];
                    for(int i=0;i<aa.length;i++){
                        aa[i]=aa[i].replace("\""," ");
                        String url=aa[i].trim();
                        imageUrls[i]=url;
                    }
                    initUI(context);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object response) {
            }
        });

    }

    /**
     * ImageLoader 图片组件初始化
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory().discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs() // Remove
                // for
                // release
                // app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}