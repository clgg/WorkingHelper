package cn.niuco.library.bean;

/**
 * Created by 1973 on 2015/4/29.
 */
public class MyLoginBean {
    public MyLoginBean(){}
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

    public LoginBean getData() {
        return data;
    }

    public void setData(LoginBean data) {
        this.data = data;
    }

    public int code;
    public String msg;
    public LoginBean data;
}
