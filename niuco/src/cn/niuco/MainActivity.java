package cn.niuco;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.niuco.R;

import java.util.ArrayList;
import java.util.List;

import cn.niuco.ui.adapter.FragmentTabAdapter;
import cn.niuco.ui.fragment.Fragment1;
import cn.niuco.ui.fragment.Fragment2;
import cn.niuco.ui.fragment.Fragment3;

/**
 * Created by 1973 on 2015/4/27.
 */
public class MainActivity extends SherlockFragmentActivity{
    TextView tv, tv2;
    Button bt1, bt2, bt3, bt4, bt5;
    public List<Fragment> list=new ArrayList<Fragment>();
    int selectedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        list.add(Fragment1.newInstance());
        list.add(Fragment2.newInstance());
        list.add(Fragment3.newInstance());
        init();
    }
    private void init() {
        RadioGroup rg= (RadioGroup) findViewById(R.id.tabs_rg);
        FragmentTabAdapter fragmentTabAdapter=new FragmentTabAdapter(this,list,R.id.content,rg);
        fragmentTabAdapter.setOnRgsExtraCheckedChangedListener(new FragmentTabAdapter.OnRgsExtraCheckedChangedListener() {
            @Override
            public void OnRgsExtraCheckedChanged(RadioGroup radioGroup, int checkedId, int index) {
                MainActivity.this.selectedIndex = index;
            }
        });
    }
}
