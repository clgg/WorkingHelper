package cn.niuco.library.DataCenter.http;

public interface IAsyncHttpResultCallback {
    public void onStart();

    public void onSuccess(Object response);

    public void onFailure(Object response);
}
