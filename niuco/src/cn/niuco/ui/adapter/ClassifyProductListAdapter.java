package cn.niuco.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.niuco.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import cn.niuco.library.Utils.Utils;
import cn.niuco.library.bean.ClassifyBean;
import cn.niuco.ui.bean.ProductBrief;

public class ClassifyProductListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ClassifyBean> mList;
    private LayoutInflater mInflater;

    public ClassifyProductListAdapter(Context context, ArrayList<ClassifyBean> list) {
        this.mContext = context;
        this.mList = list;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.product_item_layout, null);
            holder = new ViewHolder();

            holder.productImage = (ImageView) convertView.findViewById(R.id.product_item_product_image);
            holder.productTitle = (TextView) convertView.findViewById(R.id.product_item_product_title);
            holder.productDes = (TextView) convertView.findViewById(R.id.product_item_product_des);
            holder.headImage = (ImageView) convertView.findViewById(R.id.product_item_user_head_image);
            holder.nickName = (TextView) convertView.findViewById(R.id.product_item_user_ncik);
            holder.enjoyImage = (ImageView) convertView.findViewById(R.id.product_item_user_enjoy_image);
            holder.enjoyNum = (TextView) convertView.findViewById(R.id.product_item_user_enjoy_num);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.productImage.setImageResource(R.drawable.ic_launcher);
        }
        ImageLoader.getInstance().displayImage(mList.get(position).photo,holder.productImage);
        holder.productTitle.setText(mList.get(position).name);
       /* holder.productDes.setText(mList.get(position).getSummary());
        Utils.displayImage2Circle(holder.headImage, mList.get(position).getPortrait());
        holder.nickName.setText(mList.get(position).getNickname());

//        holder.enjoyImage.setOnClickListener(new enjoyButtonListener(position,mList));  //设置点击动画
        if(mList.get(position).isFavorite())
        {
            holder.enjoyImage.setImageResource(R.drawable.home_favorite2);
        }else{
            holder.enjoyImage.setImageResource(R.drawable.home_favorite);
        }

        holder.enjoyNum.setText(mList.get(position).getFcount()+"");*/

        return convertView;
    }


    public final class ViewHolder {
        ImageView productImage;
        TextView productTitle;
        TextView productDes;
        ImageView headImage;
        TextView nickName;
        ImageView enjoyImage;
        TextView enjoyNum;
    }


}
