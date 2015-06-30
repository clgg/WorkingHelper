package cn.niuco.ui.bean;

import java.util.List;

public class ProductBrief {

//    {
//        "id": 111,
//            "name": "HIRIS可穿戴套装",
//            "summary": "HIRIS支持追踪以及手势控制，是这个星球上最先进，最时尚的可穿戴计算机之一。",
//            "price": 0,
//            "fcount": 1,
//            "puid": 186895,
//            "nickname": "Star°时光",
//            "portrait": "http://q.qlogo.cn/qqapp/101204127/CE56A7DC0B2A6B9BE19EDAA16E6B89E1/100",
//            "isFavorite": false,
//            "photo": [
//        "http://zy.lenovomm.com/images/products/wordpress_upload/2015/03/a61dd4d3-c55c-459d-9a96-78bd0af3185d_banner121.jpg",
//                "http://zy.lenovomm.com/images/products/wordpress_upload/2015/03/21b7b1e5-deb3-450f-af4f-e52dae5bf5fd_banner211.png",
//                "http://zy.lenovomm.com/images/products/wordpress_upload/2015/03/0654cc66-eab1-406d-a38e-446b76a25c10_banner321.jpg"
//        ]
//    }
    private int id;
    private String name;
    private String summary;
    private int price;
    private int fcount;
    private int puid;
    private String nickname;
    private String portrait;
    private boolean isFavorite;
    private List<String> photo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getFcount() {
        return fcount;
    }

    public void setFcount(int fcount) {
        this.fcount = fcount;
    }

    public int getPuid() {
        return puid;
    }

    public void setPuid(int puid) {
        this.puid = puid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public List<String> getPhoto() {
        return photo;
    }

    public void setPhoto(List<String> photo) {
        this.photo = photo;
    }
}
