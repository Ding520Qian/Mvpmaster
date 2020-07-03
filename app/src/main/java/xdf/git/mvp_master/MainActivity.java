package xdf.git.mvp_master;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xdf.git.mvp_master.base.BaseActivity;
import xdf.git.mvp_master.listener.WeatherListener;
import xdf.git.mvp_master.presenter.WeatherPresenter;

public class MainActivity extends BaseActivity<WeatherPresenter> implements WeatherListener {


    @BindView(R.id.btn_get_weather)
    Button btnGetWeather;
    @BindView(R.id.tv_show_request)
    TextView tvShowRequest;

    @Override
    public WeatherPresenter creatPresenter() {
        return new WeatherPresenter(this);
    }

    @Override
    public void initData() {

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViewsAndEvents() {

    }

    @Override
    protected void initBar() {
        setTitle("首页");
        isLeftImgShow(true);
    }

    @OnClick(R.id.btn_get_weather)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_weather:
                mPresenter.getWeather();
                break;
        }
    }

    @Override
    public void onSuccess(String json) {
        tvShowRequest.setText(json);
    }

    @Override
    public void onFailed(String msg) {
        tvShowRequest.setText(msg);
    }
}
