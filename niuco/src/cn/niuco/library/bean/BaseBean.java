package cn.niuco.library.bean;

import java.util.List;

import cn.niuco.ui.bean.ProductBrief;

/**
 * Created by Administrator on 2015/4/30.
 */
public class BaseBean {

    private int code;
    private String msg;
    private List<ProductBrief> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ProductBrief> getData() {
        return data;
    }

    public void setData(List<ProductBrief> data) {
        this.data = data;
    }
}
