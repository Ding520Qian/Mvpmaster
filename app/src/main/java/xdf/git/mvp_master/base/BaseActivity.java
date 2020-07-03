package xdf.git.mvp_master.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import xdf.git.mvp_master.R;
import xdf.git.mvp_master.eventbus.EventBusManager;
import xdf.git.mvp_master.eventbus.EventBusModel;


public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {


    protected P mPresenter;
    private FrameLayout viewContent;
    private TextView mTitle;
    private ImageView iv_bar_left;
    private TextView tv_bar_right;
    private RelativeLayout br_rl;
    private RelativeLayout rl_bar_start;
    private RelativeLayout rl_not_network;

    private LoadDialog L;


    private OnImgClickListener onClickListenerTopLeft;   //左边图标的点击事件
    private OnImgClickListener onClickListenerTopRight;



    //定义接口
    public interface OnImgClickListener {
        void onClick();
    }

    @Override
    protected void onStart() {
        if (isBindEventBus()) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.setTranslucentForImageView(this, 0, br_rl);
        View decorView = getWindow().getDecorView();
        if (decorView != null) {   //白色背景要设置暗色系的状态栏图标
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (mPresenter == null) {
            mPresenter = creatPresenter();
        }
        initData(savedInstanceState);

        if (getContentLayoutId() != 0) {
            setContentView(R.layout.activity_base);
            viewContent = findViewById(R.id.view_content);
            iv_bar_left = findViewById(R.id.iv_bar_left);
            tv_bar_right = findViewById(R.id.tv_bar_right);
            rl_bar_start = findViewById(R.id.rl_bar_start);
            mTitle = findViewById(R.id.tv_title);
            br_rl = findViewById(R.id.br_rl);
            rl_not_network = findViewById(R.id.rl_not_network);

            LayoutInflater.from(this).inflate(getContentLayoutId(), viewContent);
            ButterKnife.bind(this);
            if (!isNetworkAvailable(this)) {
                rl_not_network.setVisibility(View.VISIBLE);
                viewContent.setVisibility(View.GONE);
                br_rl.setVisibility(View.VISIBLE);
                iv_bar_left.setVisibility(View.VISIBLE);
                mTitle.setVisibility(View.GONE);
                iv_bar_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                return;
            }

            if (isShowBar()) {
                br_rl.setVisibility(View.VISIBLE);
            } else {
                br_rl.setVisibility(View.GONE);
            }

            if (isShowTit()) {
                rl_bar_start.setVisibility(View.VISIBLE);
            } else {
                rl_bar_start.setVisibility(View.GONE);
            }

        } else {
            throw new IllegalArgumentException("You must return a right contentView layout resource Id");
        }

        L = new LoadDialog(this);

        initViewsAndEvents();
        initBar();
        initData();


        iv_bar_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListenerTopLeft != null) {
                    onClickListenerTopLeft.onClick();
                }

            }
        });

        tv_bar_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListenerTopRight != null) {
                    onClickListenerTopRight.onClick();
                }

            }
        });
    }

    protected void setTopRightButton(String txt, int style, String color, OnImgClickListener rightListener) {
        this.tv_bar_right.setText(txt);
        if (!color.isEmpty()) {
            this.tv_bar_right.setTextColor(Color.parseColor(color));
        }
        this.tv_bar_right.setBackgroundResource(style);
        this.onClickListenerTopRight = rightListener;

    }

    //添加一个方法设置图标资源id和监听器
    protected void setTopLeftButton(int iconId, OnImgClickListener leftListener) {
        this.iv_bar_left.setImageResource(iconId);
        this.onClickListenerTopLeft = leftListener;    //接口回调
    }

    protected void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            mTitle.setText(title);
        }
    }

    protected void isLeftImgShow(boolean isShow) {
        if (isShow) {
            iv_bar_left.setVisibility(View.VISIBLE);
        }
    }

    protected void isRightShow(boolean isShow) {
        if (isShow == false) {
            tv_bar_right.setText("");
        }
    }


    public abstract P creatPresenter();

    //初始化数据
    public abstract void initData();

    public void initData(Bundle savedInstanceState) {

    }

    protected boolean isBindEventBus() {
        return true;
    }

    protected boolean isShowBar() {
        return true;
    }


    protected abstract int getContentLayoutId();

    protected abstract void initViewsAndEvents();

    protected abstract void initBar();

    protected boolean isShowTit() {
        return true;
    }


    protected void setWinStyle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //修改为深色，因为我们把状态栏的背景色修改为主题色白色，默认的文字及图标颜色为白色，导致看不到了。
            if (Build.VERSION.SDK_INT >= 21) {
                View decorView = getWindow().getDecorView();
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isBindEventBus()) {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
        }
        if (mPresenter != null) mPresenter.onDestroy();//释放资源

    }

    //无参跳转
    public void startActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    //待遇有参数跳转
    public void startActivityWithExtras(Class<?> clazz, Bundle extras) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(extras);
        startActivity(intent);
    }


    @Override
    public void showL() {
        L.show();
    }

    @Override
    public void hideL() {
        if (L != null) {
            L.dismiss();
        }
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED
                            || info[i].getState() == NetworkInfo.State.CONNECTING) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void isTopActivity(EventBusModel busModel) {
        if (busModel == null) {
            return;
        }
    }

    public <T extends View> T getId(int id) {
        return (T) findViewById(id);
    }
}