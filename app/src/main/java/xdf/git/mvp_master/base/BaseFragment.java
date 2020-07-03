package xdf.git.mvp_master.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements BaseView {

    private boolean isVisible;                  //是否可见状态
    private boolean isPrepared;                 //标志位，View已经初始化完成。
    private boolean isFirstLoad = true;         //是否第一次加载
    protected LayoutInflater inflater;
    public View mRootView;

    protected P mPresenter;

    private LoadDialog L;

    Unbinder unbinder;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (mPresenter == null) {
            mPresenter = creatPresenter();
        }
        super.onCreate(savedInstanceState);
        if (isBindEventBus()) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getContentViewLayoutID() != 0) {
            return inflater.inflate(getContentViewLayoutID(), null);
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mRootView = view;
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this,view);
        L = new LoadDialog(getActivity());
        initViewsAndEvents();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) mPresenter.onDestroy();//释放资源
        if (isBindEventBus()) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    protected boolean isBindEventBus() {
        return false;
    }

    public abstract P creatPresenter();

    protected abstract int getContentViewLayoutID();

    protected abstract void initViewsAndEvents();

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


}
