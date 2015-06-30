package cn.niuco.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.niuco.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;


import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cn.niuco.library.DataCenter.http.AsynHttpURL;
import cn.niuco.library.DataCenter.http.HttpImpl;
import cn.niuco.library.DataCenter.http.IAsyncHttpReultCallback;
import cn.niuco.library.Utils.Utils;
import cn.niuco.library.bean.CommentBean;
import cn.niuco.library.bean.DetailtypeBean;
import cn.niuco.library.bean.MyBaseBean;
import cn.niuco.library.bean.ProductdataBean;
import cn.niuco.ui.adapter.CommentListAdapter;
import cn.niuco.ui.myinterface.JsInterface;
import cn.niuco.ui.widget.CustomGallery;

/**
 * Created by 1973 on 2015/4/27.
 */
public class productdetailActivity extends SherlockActivity {
    ImageView iv_head, iv_like;
    TextView tv_name, tv_like, tv_ccont, tv_title;
    DisplayImageOptions options;
    Button bt_more;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    List<DetailtypeBean> detailinfo;
    CustomGallery gallery;
    ListView lv_comment;
    String introduction;
    CommentListAdapter cladapter;
    int productId;
    String pUrl;
    ProductdataBean data;
    String comment;
    String photo;
    String[] photourls;
    WebView wv;
    int pid;
    String url="http://zy.lenovomm.com/data/product/info/";
    private ImageLoader imageLoader;
    ImageLoader loader;
    View.OnClickListener imglistener;
    ArrayList<String> urls = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_launcher)
                .showImageOnLoading(R.drawable.ic_launcher)
                        //.showImageForEmptyUri(R.drawable.pic_success)
                        //.showImageOnFail(R.drawable.pic_success)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        setContentView(R.layout.activity_productdetail);
        initView();
        Bundle bundle = getIntent().getExtras();
        productId = bundle.getInt("product_id", 1800);//读出数据
        gallery.GetList(url+productId);
        pid= productId;
        requestdetail(productId);
        imglistener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imgurl = (String) v.getTag();
                //Toast.makeText(productdetailActivity.this,""+imgurl,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(productdetailActivity.this, ImagePagerActivity.class);
                // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
                intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, urls.indexOf(v.getTag()));
                startActivity(intent);

            }
        };
        bt_more.setOnClickListener(new MoreListener());
    }
    public class MoreListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent=new Intent(productdetailActivity.this,CommentActivity.class);
            intent.putExtra("productId",productId);
            startActivity(intent);
           //跳转评论页面
        }
    }

    private void initView() {
        gallery = (CustomGallery) findViewById(R.id.gallery);
        wv = (WebView) findViewById(R.id.wv);
        tv_ccont= (TextView) findViewById(R.id.tv_ccount);
        bt_more= (Button) findViewById(R.id.bt_more);
        iv_head = (ImageView) findViewById(R.id.iv_head);
        iv_like = (ImageView) findViewById(R.id.iv_like);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_like = (TextView) findViewById(R.id.tv_like);
        tv_title = (TextView) findViewById(R.id.title);
        lv_comment = (ListView) findViewById(R.id.lv_comment);
        iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncHttpClient client=new AsyncHttpClient();
                client.setTimeout(5000);
                client.addHeader("token",AsynHttpURL.TOKEN);
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("pid",data.id);
                if(data.isFavorite){
                    jsonObject.put("status","0");
                }else{
                    jsonObject.put("status","1");
                }
                StringEntity stringEntity= null;
                try {
                    stringEntity = new StringEntity(jsonObject.toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                client.post(productdetailActivity.this, AsynHttpURL.UPDATE_FAVORITE_PRODUCT,stringEntity,"application/json",new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                      Log.e("likeproduct",new String(responseBody));

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });

            }
        });
    }

    public void requestdetail(int productId) {
        pUrl = "http://zy.lenovomm.com/data/product/info/" + productId;
        Toast.makeText(productdetailActivity.this, "" + pUrl, Toast.LENGTH_SHORT).show();

        HttpImpl.Post(pUrl, new RequestParams(), new IAsyncHttpReultCallback() {
            @Override
            public void onStart() {

            }

            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(Object response) {
                String jsonstring = (String) response;
                try {
                    MyBaseBean product = JSON.parseObject(jsonstring, MyBaseBean.class);
                    data = JSON.parseObject(product.data, ProductdataBean.class);
                    Log.e("mycomment",data.comment);
                    ArrayList<CommentBean> arrayList = JSON.parseObject(data.comment, new TypeReference<ArrayList<CommentBean>>() {
                    });
                    cladapter = new CommentListAdapter(productdetailActivity.this);
                    cladapter.setData(arrayList,pid);
                    lv_comment.setAdapter(cladapter);
                    setlistheight(lv_comment);
                    tv_title.setText(data.name);
                    tv_name.setText(data.nickname);
                    Utils.displayImage2Circle(iv_head, data.portrait);
                    introduction = data.introduction;
                    Log.e("likecount",""+data.fcount);
                    tv_like.setText("" + data.fcount);
                    tv_ccont.setText("评论（"+data.ccount+")");
                    Log.d("mydata", data.toString());
                    Log.d("detail", "" + introduction);
                    //String sss = introduction.replaceAll("</?[^/?(img)][^><]*>", "");
                    // sss=sss.replaceAll("<style[^>]*?>[\\s\\S]*?<\\/style>","");
                    Log.e("replace", introduction);
                    ArrayList urls=Utils.getlist(introduction);
                    //Document doc= Jsoup.parse(introduction);
                    //for(int i=0,i<doc.)
                    //Element element=doc.select("span")
                    //  Log.e("element222",element.text());
                    //  Log.e("element2222",element.toString());
                    // Elements elements=doc.getElementsByTag("img");
                   /* for (Element element : elements) {
                        Log.e("element::",""+element.text());
                        Log.e("img000",element.attr("src"));
                   }*/
                    //detailinfo = Utils.getlist("<p align=\"center\"><br></p><p>　　通过Google的开源硬件平台Project Ara，用户以可像玩乐高一样把相机、电池、处理器等等组件搭成一部手机。这样的组装方式能够满足用户个性化的需求，可以单独对一个模块进行更新换代，以及随时实现功能增减而不需要换手机。</p><p><img alt=\"插图1\" class=\"aligncenter size-full wp-image-6495\"  data-cke-saved-src=\"http://zy.lenovomm.com/images/products/wordpress_upload/2015/04/插图17.jpg\" src=\"http://zy.lenovomm.com/images/products/wordpress_upload/2015/04/插图17.jpg\" ></p><p>　　这款Ara手机包括空气质量组件、二氧化碳组件、光学组件、心脏监测组件、血糖计组件和呼吸探测器组件。每个组件对应一项环境或健康监测功能。它甚至还有一个叫做“灵魂”的组件，对此，网站上显示的是：“对此我们还不能多说什么”。</p><p><img alt=\"插图2\" class=\"aligncenter size-full wp-image-6496\"  data-cke-saved-src=\"http://zy.lenovomm.com/images/products/wordpress_upload/2015/04/插图27.jpg\" src=\"http://zy.lenovomm.com/images/products/wordpress_upload/2015/04/插图27.jpg\" ></p><p>　　无论是从外观还是功能来看，它已经不像是一只手机了，更像由一堆智能硬件积木搭起来的产品。</p><p><img alt=\"插图3\" class=\"aligncenter size-full wp-image-6497\"  data-cke-saved-src=\"http://zy.lenovomm.com/images/products/wordpress_upload/2015/04/插图37.jpg\" src=\"http://zy.lenovomm.com/images/products/wordpress_upload/2015/04/插图37.jpg\" ></p><p>　　Lapka创始人Vadik Marmeladove说：“有关Project Ara的一切都超前了五年，只有设计除外。我认为它的设计也应该成为视觉与文化的里程碑。”他认为，Google应该像为Android平台上的软件设计提出Material Design一样，为Ara硬件设计打造一套专属的设计语言。</p><p>　　Marmeladove认为，Ara平台让人能够在任一维度上扩展手机的形状，使之变成桌面移动实验室。显微镜、甚至椅子都有可能变成Ara手机的硬件模块。听起来可能难以理解，但Lapka已经推出了一款独立显示器硬件——听起来已经离手机相去甚远。</p><p><img alt=\"插图4\" class=\"aligncenter size-full wp-image-6498\"  data-cke-saved-src=\"http://zy.lenovomm.com/images/products/wordpress_upload/2015/04/插图43.jpg\" src=\"http://zy.lenovomm.com/images/products/wordpress_upload/2015/04/插图43.jpg\" ></p><p>　　Google还需要进一步明确Ara硬件能做什么，需要做什么的界限。Lapka公司跨界的设计证明了它远不止是一个能将手机零件组装起来的平台。Ara手机究竟是什么？形状大小有没有限制？这些都有无限的可能性。但可以明确的是，它不能既是一些奇奇怪怪的硬件组合，又同时是我们今天所认识的智能手机。</p>");
                    String js = "file:///android_asset/ProductDetail.html";
                    wv.getSettings().setJavaScriptEnabled(true);
                    wv.getSettings().setDefaultTextEncodingName("UTF-8");
                    wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
                    wv.loadUrl(js);
                    // wv.loadData(js,"text/html","UTF-8");
                    // wv.loadDataWithBaseURL("file:///android_asset/","ExampleApp.html","text/html","UTF-8","");
                    JsInterface jsi=new JsInterface(productdetailActivity.this);
                    jsi.setList(urls);
                    wv.addJavascriptInterface(jsi, "imagelistner");
                    wv.setWebViewClient(new MyWebViewClient());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object response) {
                Toast.makeText(productdetailActivity.this, "请求失败", Toast.LENGTH_LONG).show();
            }
        });


    }

    private void setlistheight(ListView lv_comment) {
        ListAdapter listAdapter = lv_comment.getAdapter();

        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, lv_comment);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = lv_comment.getLayoutParams();
        params.height = totalHeight + (lv_comment.getDividerHeight() * (listAdapter.getCount() - 1));
        Log.e("height",""+totalHeight);
        lv_comment.setLayoutParams(params);
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            view.getSettings().setJavaScriptEnabled(true);

            super.onPageFinished(view, url);
            // html加载完成之后，添加监听图片的点击js函数
           // addImageClickListner();
            view.loadUrl("javascript:connectWebViewJavascriptBridge('"+ introduction +"')");

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            view.getSettings().setJavaScriptEnabled(true);
            /*view.evaluateJavascript(introduction,new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Log.e("valuecallback",value);
                }
            });*/

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            super.onReceivedError(view, errorCode, description, failingUrl);

        }
    }
/*
    private void addImageClickListner() {
      *//*  wv.loadUrl("javascript:()function(){"
                    +"var objs = document.getElementsByTagName(""); ");*//*
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        wv.loadUrl("javascript:(function(){ "
                +"var objs = document.getElementsByTagName(\"img\"); "
                +"for(var i=0;i<objs.length;i++)  "
                +"{"
                +"    objs[i].onclick=function()  "
                +"    {  "
                +"        window.imagelistner.openImage(this.src);  "
                +"    }  "
                +"}"
                +"})()");
    }*/
}

class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
    static final List<String> displayedImages = new ArrayList<>();

    @Override
    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
        super.onLoadingComplete(imageUri, view, loadedImage);
        ImageView imageView = (ImageView) view;
        // 是否第一次显示
        boolean firstDisplay = !displayedImages.contains(imageUri);
        if (firstDisplay) {
            // 图片淡入效果
            FadeInBitmapDisplayer.animate(imageView, 500);
            displayedImages.add(imageUri);
        }
    }
}
