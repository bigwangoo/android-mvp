package com.tianxiabuyi.mvp.manager;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentManager;

import com.tianxiabuyi.mvp.base.delegate.IAppLifecycle;
import com.tianxiabuyi.mvp.dagger.model.GlobalConfigModule;

import java.util.List;

/**
 * 框架配置一些参数, 生命周期注入
 * <p>
 * Created in 2017/9/22 10:11.
 *
 * @author Wang YaoDong.
 */
public interface ConfigModule {

    /**
     * 框架参数配置
     * <p>
     * see {@link GlobalConfigModule.Builder}
     */
    void applyOptions(Context context, GlobalConfigModule.Builder builder);

    /**
     * App 生命周期中操作注入
     * <p>
     * see {@link IAppLifecycle}
     */
    void injectAppLifecycle(Context context, List<IAppLifecycle> lifecycle);

    /**
     * Activity 生命周期操作注入
     * <p>
     * see {@link Application.ActivityLifecycleCallbacks}
     */
    void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycle);

    /**
     * Fragment 生命周期中操作注入
     * <p>
     * see {@link FragmentManager.FragmentLifecycleCallbacks}
     */
    void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycle);
}
