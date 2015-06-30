package cn.niuco.library.bean;

import java.util.ArrayList;

/**
 * Created by 1973 on 2015/4/29.
 */
public class ProductdataBean {
    public int id;
    public String name;
    public String introduction;
    public String summary;
    public float price;
    public String source;
    public int fcount;
    public int ccount;
    public int acount;
    public float puid;
    public String ptime;
    public String nickname;
    public String portrait;
    public String isTryout;
    public Boolean isFavorite;
    public String photo;
    public String comment;
    public String tagScope;

    @Override
    public String toString() {
        return "ProductBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", introduction='" + introduction + '\'' +
                ", summary='" + summary + '\'' +
                ", price=" + price +
                ", source='" + source + '\'' +
                ", fcount=" + fcount +
                ", ccount=" + ccount +
                ", acount=" + acount +
                ", puid=" + puid +
                ", ptime='" + ptime + '\'' +
                ", nickname='" + nickname + '\'' +
                ", portrait='" + portrait + '\'' +
                ", isTryout='" + isTryout + '\'' +
                ", isFavorite=" + isFavorite +
                ", photo='" + photo + '\'' +
                ", comment='" + comment + '\'' +
                ", tagScope='" + tagScope + '\'' +
                ", article='" + article + '\'' +
                '}';
    }

    public String article;
}
