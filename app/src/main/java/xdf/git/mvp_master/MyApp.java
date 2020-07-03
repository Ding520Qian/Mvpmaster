package xdf.git.mvp_master;

import android.app.Application;
import android.content.Context;

import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;

public class MyApp extends Application {

    private static MyApp instance;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        instance = this;

        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder();
        OkHttpFinal.getInstance().init(builder.build());
    }

    public static Context getContext() {
        return context;
    }

    public static MyApp instance() {
        return instance;
    }
}
