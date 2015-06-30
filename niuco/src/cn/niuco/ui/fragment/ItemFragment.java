package cn.niuco.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.niuco.R;

import org.apache.http.Header;

import cn.niuco.library.DataCenter.http.AsynHttpURL;


/**
 * Created by 1973 on 2015/3/24.
 */
public class ItemFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View contextView = inflater.inflate(R.layout.fragment_pklist, container, false);
        final TextView mTextView = (TextView) contextView.findViewById(R.id.tv22);

        //获取Activity传递过来的参数
        Bundle mBundle = getArguments();
        String title = mBundle.getString("arg");
        AsyncHttpClient client=new AsyncHttpClient();
        client.setTimeout(5000);
        client.addHeader("token", AsynHttpURL.TOKEN);
        client.post(getActivity(),AsynHttpURL.PK_LIST+"/"+"1"+"/"+"5",new RequestParams(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.e("pklist", "" + new String(responseBody));
                mTextView.setText(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


        return contextView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}