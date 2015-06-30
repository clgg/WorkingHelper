package cn.niuco.ui.myinterface;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.niuco.ui.activity.ImagePagerActivity;

/**
 * Created by 1973 on 2015/5/13.
 */
public class JsInterface {
    private Context mcontext;
    private ArrayList mlist;
    public  JsInterface(Context context) {
        this.mcontext = context;
    }
    public void setList(ArrayList list){
        this.mlist=list;
    }

    public void openImage(String img) {
            //onclick
        img=img.trim();
        Log.e("jsimg",img);
        int i=mlist.indexOf(img);
        if(i==-1){
            i=0;
        }
        //String imgurl= (String) v.getTag();
        //Toast.makeText(productdetailActivity.this,""+imgurl,Toast.LENGTH_LONG).show();
        Intent intent = new Intent(mcontext, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, mlist);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, i);
        mcontext.startActivity(intent);

    }

}
