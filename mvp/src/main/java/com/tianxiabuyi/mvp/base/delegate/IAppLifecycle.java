package com.tianxiabuyi.mvp.base.delegate;

import android.app.Application;
import android.content.Context;

/**
 * 代理{@link Application}的生命周期
 * <p>
 * Created in 2017/9/21 13:30.
 *
 * @author Wang YaoDong.
 */
public interface IAppLifecycle {

    void attachBaseContext(Context base);

    void onCreate(Application application);

    void onTerminate(Application application);
}
