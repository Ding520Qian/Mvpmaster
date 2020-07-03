package xdf.git.mvp_master.listener;

public interface WeatherListener {

    void onSuccess(String json);

    void onFailed(String msg);
}
