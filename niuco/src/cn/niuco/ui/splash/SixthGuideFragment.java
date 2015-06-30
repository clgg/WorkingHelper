package cn.niuco.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.niuco.R;

import cn.niuco.MainActivity;
import cn.niuco.library.Utils.Utils;

/**
 * Created with Android Studio.
 * User: ryan@xisue.com
 * Date: 7/14/14
 * Time: 11:10 PM
 * Desc: ThirdGuideFragment
 */
public class SixthGuideFragment extends BaseGuideFragment {

    View mLayoutLogo;
    private LinearLayout goToHomePage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guide_sixth, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLayoutLogo = view.findViewById(R.id.guide_item_1);
        goToHomePage = (LinearLayout)view.findViewById(R.id.goto_world);
        goToHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MainActivity.class);
                startActivity(intent);
                Utils.setUsingState(getActivity()); //设置已经使用过
                getActivity().finish();
            }
        });
        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.splash_guide_logo);
        mLayoutLogo.startAnimation(anim);
    }

    @Override
    public int[] getChildViewIds() {
        return new int[]{};
    }

    @Override
    public int getRootViewId() {
        return R.id.layout_guide_sixth;
    }
}
