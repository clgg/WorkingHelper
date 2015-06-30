package cn.niuco.library;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.niuco.library.db.bean.UserInfo;
import cn.niuco.library.db.helper.DataBaseHelper;

/**
 * Created by Administrator on 2015/5/6.
 */
public class AccountManager {

    private static Context mContext;
    private  static AccountManager accountManager;

    private AccountManager(Context context) {
        if (context == null) {
        }
    }

    public static AccountManager getInstance(Context context) {
        mContext = context;
        if (accountManager == null) {
            accountManager = new AccountManager(context);
        }
        return accountManager;
    }



    public UserInfo getCurrentUserInfo()
    {
        DataBaseHelper helper = DataBaseHelper.getHelper(mContext);
        List<UserInfo> userList = new ArrayList<UserInfo>();
        try {
            userList = helper.getUserDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(userList.isEmpty())
        {
            return null;
        }else{
            return userList.get(0);
        }
    }

    public boolean isLogined(Context context) {
        String logined = getLoginState();
        if(logined.equals("1"))
        {
            return true;
        }else{
            return false;
        }
    }

    public void setLogined(boolean isLogined) {
        if(isLogined)
        {
            setLoginState(1);
        }else{
            setLoginState(0);
        }

    }

    public void setLoginState(int isLogined)
    {
        SharedPreferences mySharedPreferences= mContext.getSharedPreferences("base_userinfo",
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("islogined", isLogined+"");
        editor.commit();
    }

    public String getLoginState()
    {
        SharedPreferences sharedPreferences= mContext.getSharedPreferences("base_userinfo",
                Activity.MODE_PRIVATE);
        String value =sharedPreferences.getString("islogined", "0"); //0为未登陆 ，1为yijing登陆
        return value;
    }
}
