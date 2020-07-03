package xdf.git.mvp_master.net;

public interface LoadTasksCallBack  {
    void onSuccess(String request);
    void onFailed(int code, String msg);
}
