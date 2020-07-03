package xdf.git.mvp_master.base;

import com.google.gson.Gson;

public abstract class BasePresenter<V> {
    protected V mListener;
    protected Gson gson = new Gson();

    public BasePresenter(V mView) {
        this.mListener = mView;
    }

    public void onDestroy() {
        if (mListener != null) {
            this.mListener = null;
        }
        if (gson != null) {
            gson = null;
        }
    }
}
