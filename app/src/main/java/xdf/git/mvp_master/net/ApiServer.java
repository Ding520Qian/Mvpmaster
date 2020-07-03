package xdf.git.mvp_master.net;

import android.content.Intent;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;


public class ApiServer implements NetTask {


    private String url;
    private HashMap<String, String> params;
    private Intent intent;

    private static ApiServer mInstance = null;

    public static ApiServer initClient() {
        if (mInstance == null) {
            synchronized (ApiServer.class) {
                if (mInstance == null) {
                    mInstance = new ApiServer();
                }
            }
        }
        return mInstance;
    }

    public static ApiServer getInstance() {
        return initClient();
    }

    public ApiServer url(String url) {
        this.url = url;
        return this;
    }

    /**
     * 请求参数
     *
     * @param params
     * @return
     */
    public ApiServer setParams(HashMap<String, String> params) {
        this.params = params;
        return this;
    }

    @Override
    public void excuter(final LoadTasksCallBack callBack) {

        RequestParams requestParams = new RequestParams();
        if (params != null) {
            Iterator iter = params.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                if (entry.getValue() != null) {
                    if (entry.getValue().toString() != null) {
                        requestParams.addFormDataPart(entry.getKey().toString(), entry.getValue().toString());
                        Log.e("http", entry.getKey().toString() + "  -->  " + entry.getValue());
                    }
                }
            }
        }

        HttpRequest.get(url, requestParams, new BaseHttpRequestCallback<String>() {
            @Override
            protected void onSuccess(String s) {
                super.onSuccess(s);
                Log.e("success", "====" + url + "" + s);
                callBack.onSuccess(s);
            }

            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                Log.e("failure", url + "====" + errorCode + "===" + msg);
                callBack.onFailed(errorCode, msg);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                Log.e("finish", "====");
                if (params != null) {
                    params.clear();
                }
            }
        });
    }
}
