package com.tianxiabuyi.mvp.base.delegate;

import android.app.Application;
import android.content.Context;

import com.tianxiabuyi.mvp.base.IApp;
import com.tianxiabuyi.mvp.dagger.component.AppComponent;

/**
 * 可以代理application, 只需执行对应生命周期
 * <p>
 * 如第三方 application
 * <p>
 * Created in 2017/9/21 10:01.
 *
 * @author Wang YaoDong.
 */
public class AppDelegate implements IApp, IAppLifecycle {

    public AppDelegate(Context context) {
    }

    @Override
    public AppComponent getAppComponent() {
        return null;
    }

    @Override
    public void attachBaseContext(Context base) {

    }

    @Override
    public void onCreate(Application application) {

    }

    @Override
    public void onTerminate(Application application) {

    }
}
