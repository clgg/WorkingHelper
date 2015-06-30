package com.handmark.pulltorefresh.library.internal;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.R;

public class MyFragmeAnimLoadingLayout extends LoadingLayout {

    private AnimationDrawable animationDrawable;

    public MyFragmeAnimLoadingLayout(Context context, PullToRefreshBase.Mode mode,
                                     PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
        // 初始化
        peopleImage.setImageResource(R.drawable.app_refresh_people_0);
        goodsImage.setImageResource(R.drawable.app_refresh_goods_0);
    }

    // 默认图片
    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.app_refresh_goods_0;
    }

    @Override
    protected void onLoadingDrawableSet(Drawable imageDrawable) {
        // NO-OP
        Log.e("pull", "onLoadingDrawableSet");
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onPullImpl(float scaleOfLayout) {
        // NO-OP
        Log.e("pull", "scaleOfLayout:" + scaleOfLayout);
        if (scaleOfLayout <= 1 && scaleOfLayout > 0) {
            peopleImage.setScaleX(scaleOfLayout);
            peopleImage.setScaleY(scaleOfLayout);
            goodsImage.setScaleX(scaleOfLayout);
            goodsImage.setScaleY(scaleOfLayout);
        } else {
            peopleImage.setScaleX(1);
            peopleImage.setScaleY(1);
            goodsImage.setScaleX(1);
            goodsImage.setScaleY(1);
        }
        peopleRunImage.setVisibility(GONE);
    }

    // 下拉以刷新
    @Override
    protected void pullToRefreshImpl() {
        // NO-OP
        Log.e("pull", "pullToRefreshImpl");

    }

    // 正在刷新时回调
    @Override
    protected void refreshingImpl() {
        // 播放帧动画
//            animationDrawable.start();
        peopleImage.setVisibility(GONE);
        goodsImage.setVisibility(GONE);
        peopleRunImage.setVisibility(VISIBLE);
        animationDrawable = (AnimationDrawable) peopleRunImage.getBackground();
        animationDrawable.start();
        Log.e("pull", "refreshingImpl");
    }

    // 释放以刷新
    @Override
    protected void releaseToRefreshImpl() {
        // NO-OP
        Log.e("pull", "releaseToRefreshImpl");
    }

    // 重新设置
    @Override
    protected void resetImpl() {
        peopleImage.setVisibility(VISIBLE);
        goodsImage.setVisibility(VISIBLE);
        peopleRunImage.setVisibility(GONE);
    }
}
