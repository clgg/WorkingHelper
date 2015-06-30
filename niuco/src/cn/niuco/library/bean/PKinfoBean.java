package cn.niuco.library.bean;

import java.util.ArrayList;

/**
 * Created by 1973 on 2015/4/29.
 */
public class PKinfoBean {
    public PKinfoBean(){}
    public PKinfoBean(long id) {
        this.id = id;
    }

    public long id;
    public int myVote;
    public Boolean isAward;
    public String title;
    public int status;
    public long endTime;
    public String redTitle;
    public String blueTitle;
    public int redCount;
    public int blueCount;
    public String image;
    public ArrayList<UserBean> userList;
}
