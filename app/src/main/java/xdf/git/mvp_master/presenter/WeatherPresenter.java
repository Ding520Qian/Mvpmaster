package xdf.git.mvp_master.presenter;

import xdf.git.mvp_master.base.BasePresenter;
import xdf.git.mvp_master.net.ApiServer;
import xdf.git.mvp_master.net.LoadTasksCallBack;
import xdf.git.mvp_master.net.UrlFactory;
import xdf.git.mvp_master.listener.WeatherListener;

public class WeatherPresenter extends BasePresenter<WeatherListener> {


    public WeatherPresenter(WeatherListener mView) {
        super(mView);
    }

    /**
     * 获取天气
     */
    public void getWeather() {
        ApiServer.getInstance().url(UrlFactory.WEATHER_BASE_URL).excuter(new LoadTasksCallBack() {
            @Override
            public void onSuccess(String request) {
                if (mListener != null) {
                    mListener.onSuccess(request);
                }
            }

            @Override
            public void onFailed(int code, String msg) {
                if (mListener != null) {
                    mListener.onFailed(msg);
                }
            }
        });
    }
}
