package cn.niuco.ui.splash;

import android.animation.ArgbEvaluator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextSwitcher;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.niuco.R;
import com.viewpagerindicator.CirclePageIndicator;

import cn.niuco.MainActivity;
import cn.niuco.library.Utils.Utils;

public class SplashActivity extends SherlockFragmentActivity {

    final float PARALLAX_COEFFICIENT = 1.2f;
    final float DISTANCE_COEFFICIENT = 0.5f;

    TextSwitcher mTextSwitcher;
    ViewPager mPager;
    CirclePageIndicator mPagerIndicator;
    FragmentAdapter mAdapter;
    SparseArray<int[]> mLayoutViewIdsMap = new SparseArray<int[]>();

    private void addGuide(BaseGuideFragment fragment) {
        mAdapter.addItem(fragment);
        mLayoutViewIdsMap.put(fragment.getRootViewId(), fragment.getChildViewIds());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_guide);

        String usingState = Utils.getUsingState(this);
        Toast.makeText(SplashActivity.this,""+usingState, Toast.LENGTH_SHORT).show();
        if(usingState.equals("true"))
        {
            //第一次打开使用

        }else{
            Intent intent = new Intent();
            intent.setClass(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            Utils.setUsingState(this);
            this.finish();
        }

        mTextSwitcher = (TextSwitcher) findViewById(R.id.tip);
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerIndicator = (CirclePageIndicator) findViewById(R.id.indicator);

        mAdapter = new FragmentAdapter(getSupportFragmentManager());
        addGuide(new FirstGuideFragment());
        addGuide(new SecondGuideFragment());
        addGuide(new ThirdGuideFragment());
        addGuide(new FourthGuideFragment());
        addGuide(new FifthGuideFragment());
        addGuide(new SixthGuideFragment());

        mPager.setAdapter(mAdapter);
        mPagerIndicator.setViewPager(mPager);

        mPager.setPageTransformer(true, new ParallaxTransformer(PARALLAX_COEFFICIENT, DISTANCE_COEFFICIENT));
        mPagerIndicator.setOnPageChangeListener(new GuidePageChangeListener());
    }

    class ParallaxTransformer implements ViewPager.PageTransformer {

        float parallaxCoefficient;
        float distanceCoefficient;

        public ParallaxTransformer(float parallaxCoefficient, float distanceCoefficient) {
            this.parallaxCoefficient = parallaxCoefficient;
            this.distanceCoefficient = distanceCoefficient;
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void transformPage(View page, float position) {
            float scrollXOffset = page.getWidth() * parallaxCoefficient;

            ViewGroup pageViewWrapper = (ViewGroup) page;
            @SuppressWarnings("SuspiciousMethodCalls")
            int[] layer = mLayoutViewIdsMap.get(pageViewWrapper.getId());
            for (int id : layer) {
                View view = page.findViewById(id);
                if (view != null) {
                    view.setTranslationX(scrollXOffset * position);
                }//2131361904
                scrollXOffset *= distanceCoefficient;
            }
        }
    }

    class GuidePageChangeListener implements ViewPager.OnPageChangeListener {

        ArgbEvaluator mColorEvaluator;

        int mPageWidth, mTotalScrollWidth;

        int mGuideStartBackgroundColor, mGuideEndBackgroundColor;

        String[] mGuideTips;

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public GuidePageChangeListener() {
            mColorEvaluator = new ArgbEvaluator();

            mPageWidth = getWindowManager().getDefaultDisplay().getWidth();
            mTotalScrollWidth = mPageWidth * mAdapter.getCount();

            mGuideStartBackgroundColor = getResources().getColor(R.color.guide_start_background);
            mGuideEndBackgroundColor = getResources().getColor(R.color.guide_end_background);

            mGuideTips = getResources().getStringArray(R.array.array_guide_tips);
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            float ratio = (mPageWidth * position + positionOffsetPixels) / (float) mTotalScrollWidth;
            Integer color = (Integer) mColorEvaluator.evaluate(ratio, mGuideStartBackgroundColor, mGuideEndBackgroundColor);
            mPager.setBackgroundColor(color);
        }

        @Override
        public void onPageSelected(int position) {
            if (mGuideTips != null && mGuideTips.length > position) {
                mTextSwitcher.setText(mGuideTips[position]);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
