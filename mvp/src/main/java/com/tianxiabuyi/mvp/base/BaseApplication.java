package com.tianxiabuyi.mvp.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.tianxiabuyi.mvp.base.delegate.AppDelegate;
import com.tianxiabuyi.mvp.dagger.component.AppComponent;

/**
 * application 参考
 * <p>
 * Created in 2017/9/21 9:13.
 *
 * @author Wang YaoDong.
 */
public class BaseApplication extends Application implements IApp {

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
