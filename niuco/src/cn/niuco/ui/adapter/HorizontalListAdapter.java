package cn.niuco.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.niuco.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.apache.http.entity.StringEntity;

import java.util.ArrayList;
import java.util.List;

import cn.niuco.library.bean.PKinfoBean;

/**
 * Created by yangbin on 2015/2/12.
 */
public class HorizontalListAdapter extends BaseAdapter{
    private ImageLoadingListener animateFirstListener=new AnimateFirstDisplayListener();
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<PKinfoBean> list;
    StringEntity entity,myentity;
    DisplayImageOptions options;
    int id;
    private ImageLoader imageLoader;
    String status;
    public HorizontalListAdapter(Context context){
        this.context=context;
        layoutInflater=layoutInflater.from(context);
    }
    public List getData(){
        return list;
    }
    public void setData(ArrayList<PKinfoBean> data){
        this.list=data;
        //this.id=id;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view=null;
        imageLoader= ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        final ViewHolder viewHolder;
        options= new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.ic_launcher)
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .showImageOnFail(R.drawable.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.commentitem,null);
            viewHolder=new ViewHolder();
            viewHolder.iv_head= (ImageView) convertView.findViewById(R.id.iv_head);
            viewHolder.tv_content= (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.tv_time= (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tv_delate= (TextView) convertView.findViewById(R.id.tv_delate);
            viewHolder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_like= (TextView) convertView.findViewById(R.id.tv_like);
            viewHolder.bt_return= (TextView) convertView.findViewById(R.id.bt_return);
            viewHolder.iv_like= (ImageView) convertView.findViewById(R.id.iv_like);
            convertView.setTag(viewHolder);
    }else {
        viewHolder= (ViewHolder) convertView.getTag();
    }
    viewHolder.tv_name.setText(""+list.get(position).id);
  /*  viewHolder.tv_time.setText(list.get(position).blueCount);
    viewHolder.tv_like.setText(""+list.get(position).redTitle);
    viewHolder.tv_content.setText(list.get(position).redCount);
//       DownImage downimage=new DownImage(list.get(position).imgurl);
     //   Log.e("Adapter", "" + list.get(position).imgurl);
        imageLoader.displayImage(list.get(position).image, viewHolder.iv_head, options, animateFirstListener);*/
//        downimage.loadImage(new DownImage.ImageCallBack() {
//            @Override
//            public void getDrawable(Drawable drawable) {
//                viewHolder.img.setImageDrawable(drawable);
//            }
//        });

        return convertView;
    }
    public class ViewHolder{
        ImageView iv_head,iv_like;
        TextView tv_content,tv_time,tv_name,tv_like,bt_return,tv_delate;

    }
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
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
}
