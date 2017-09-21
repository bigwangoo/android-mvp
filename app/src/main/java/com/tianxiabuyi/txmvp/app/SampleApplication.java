package com.tianxiabuyi.txmvp.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.tianxiabuyi.mvp.base.IApp;
import com.tianxiabuyi.mvp.base.delegate.AppDelegate;
import com.tianxiabuyi.mvp.dagger.component.AppComponent;

/**
 * Created in 2017/9/21 13:27.
 *
 * @author Wang YaoDong.
 */
public class SampleApplication extends Application implements IApp {

    private AppDelegate mAppDelegate;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
        this.mAppDelegate = new AppDelegate(base);
        this.mAppDelegate.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.mAppDelegate.onCreate(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAppDelegate != null)
            this.mAppDelegate.onTerminate(this);
    }

    @Override
    public AppComponent getAppComponent() {
        return mAppDelegate.getAppComponent();
    }
}
