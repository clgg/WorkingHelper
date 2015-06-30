package cn.niuco.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.niuco.R;

import java.util.ArrayList;

import cn.niuco.library.bean.PKinfoBean;

/**
 * Created by 1973 on 2015/6/1.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    ArrayList<PKinfoBean> mlist;
    Context mcontext;
    public int weight;
    private ViewHolder vh;

    // Provide a reference to the type of views that you are using
    // (custom viewholder)
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public RelativeLayout rl_parent;
        public LinearLayout ll_top;
        public ViewHolder(View v) {
            super(v);
            ll_top= (LinearLayout) v.findViewById(R.id.ll_top);
            rl_parent= (RelativeLayout) v.findViewById(R.id.rl_parent);
            tv_name= (TextView) v.findViewById(R.id.tv_name);
        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerAdapter(Context context, ArrayList<PKinfoBean> list,int w) {
        mcontext = context;
        mlist=list;
        weight=w;
    }
    public void setlist(ArrayList<PKinfoBean> list){
        mlist=list;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(mcontext).inflate(R.layout.pk_item,parent,false);
        // set the view's size, margins, paddings and layout parameters
        if(110==viewType){
            v.setFocusable(false);
            v.setVisibility(View.GONE);
            v.setLayoutParams(new ViewGroup.LayoutParams((weight-v.getWidth())/6, ViewGroup.LayoutParams.MATCH_PARENT));
            vh=new ViewHolder(v);
        }else{
            vh = new ViewHolder(v);
        }
        return vh;

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

            holder.tv_name.setText(""+mlist.get(position).id);
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0||position==mlist.size()-1){
            return 110;
        }
        return super.getItemViewType(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mlist.size();
    }
}
