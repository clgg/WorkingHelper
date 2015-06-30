package cn.niuco.library.DataCenter.http;

/**
 * Created by 1973 on 2015/4/29.
 */
public interface IAsyncHttpReultCallback {
    public void onStart();

    public void onSuccess(Object response);

    public void onFailure(Object response);
}
