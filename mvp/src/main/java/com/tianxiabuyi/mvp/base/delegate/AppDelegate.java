package com.tianxiabuyi.mvp.base.delegate;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.res.Configuration;

import com.tianxiabuyi.mvp.base.IApp;
import com.tianxiabuyi.mvp.dagger.component.AppComponent;
import com.tianxiabuyi.mvp.dagger.component.DaggerAppComponent;
import com.tianxiabuyi.mvp.dagger.model.AppModule;
import com.tianxiabuyi.mvp.dagger.model.ClientModule;
import com.tianxiabuyi.mvp.dagger.model.GlobalConfigModule;
import com.tianxiabuyi.mvp.http.ImageLoader.glide.GlideImageConfig;
import com.tianxiabuyi.mvp.manager.ConfigModule;
import com.tianxiabuyi.mvp.manager.Lifecycle.ActivityLifecycle;
import com.tianxiabuyi.mvp.manager.Lifecycle.ActivityLifecycleForRxLifecycle;
import com.tianxiabuyi.mvp.manager.ManifestParse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 可以代理application, 执行对应生命周期
 * 如使用第三方 application，只需执行对应生命周期
 * <p>
 * Created in 2017/9/21 10:01.
 *
 * @author Wang YaoDong.
 */
public class AppDelegate implements IApp, IAppLifecycle {

    private AppComponent mAppComponent;
    private ComponentCallbacks2 mComponentCallback;

    private List<ConfigModule> mModules = new ArrayList<>();
    private List<IAppLifecycle> mAppLifecycle = new ArrayList<>();
    private List<Application.ActivityLifecycleCallbacks> mActivityLifecycleCallback = new ArrayList<>();

    @Inject
    ActivityLifecycle mActivityLifecycle;
    @Inject
    ActivityLifecycleForRxLifecycle mActivityLifecycleForRxLifecycle;

    /**
     * 初始化项目配置
     */
    public AppDelegate(Context context) {
        mModules = ManifestParse.parse(context);
        for (ConfigModule module : mModules) {
            module.injectAppLifecycle(context, mAppLifecycle);
            module.injectActivityLifecycle(context, mActivityLifecycleCallback);
        }
    }

    @Override
    public void attachBaseContext(Context base) {
        for (IAppLifecycle lifecycle : mAppLifecycle) {
            lifecycle.attachBaseContext(base);
        }
    }

    @Override
    public void onCreate(Application application) {
        // 依赖注入
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(application)) // 提供application
                .clientModule(new ClientModule())       // 用于提供okHttp和retrofit的单例
                .globalConfigModule(getGlobalConfigModule(application, mModules)) // 全局配置
                .build();
        mAppComponent.inject(this);
        mAppComponent.extras().put(ConfigModule.class.getName(), mModules);
        this.mModules = null;

        // activity 生命周期
        application.registerActivityLifecycleCallbacks(mActivityLifecycle);
        application.registerActivityLifecycleCallbacks(mActivityLifecycleForRxLifecycle);
        for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycleCallback) {
            application.registerActivityLifecycleCallbacks(lifecycle);
        }

        // 内存管理
        mComponentCallback = new AppComponentCallbacks(application, mAppComponent);
        application.registerComponentCallbacks(mComponentCallback);

        // app  onCreate
        for (IAppLifecycle lifecycle : mAppLifecycle) {
            lifecycle.onCreate(application);
        }
    }

    /**
     * Dagger注入配置信息
     */
    private GlobalConfigModule getGlobalConfigModule(Context context, List<ConfigModule> modules) {
        GlobalConfigModule.Builder builder = GlobalConfigModule.builder();
        for (ConfigModule module : modules) {
            module.applyOptions(context, builder);
        }
        return builder.build();
    }

    @Override
    public void onTerminate(Application application) {
        if (mActivityLifecycle != null) {
            application.unregisterActivityLifecycleCallbacks(mActivityLifecycle);
        }
        if (mActivityLifecycleForRxLifecycle != null) {
            application.unregisterActivityLifecycleCallbacks(mActivityLifecycleForRxLifecycle);
        }
        if (mComponentCallback != null) {
            application.unregisterComponentCallbacks(mComponentCallback);
        }
        if (mActivityLifecycleCallback != null && mActivityLifecycleCallback.size() > 0) {
            for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycleCallback) {
                application.unregisterActivityLifecycleCallbacks(lifecycle);
            }
        }
        if (mAppLifecycle != null && mAppLifecycle.size() > 0) {
            for (IAppLifecycle lifecycle : mAppLifecycle) {
                lifecycle.onTerminate(application);
            }
        }
        this.mAppComponent = null;
        this.mActivityLifecycle = null;
        this.mActivityLifecycleForRxLifecycle = null;
        this.mActivityLifecycleCallback = null;
        this.mComponentCallback = null;
        this.mAppLifecycle = null;
    }

    @Override
    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    /**
     * 系统内存管理回调
     */
    private static class AppComponentCallbacks implements ComponentCallbacks2 {
        private Application mApplication;
        private AppComponent mAppComponent;

        public AppComponentCallbacks(Application application, AppComponent appComponent) {
            this.mApplication = application;
            this.mAppComponent = appComponent;
        }

        @Override
        public void onTrimMemory(int level) {

        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {

        }

        @Override
        public void onLowMemory() {
            //内存不足时清理图片请求框架的内存缓存
            mAppComponent.imageLoader().clear(mApplication,
                    GlideImageConfig.builder().isClearMemory(true).build());
        }
    }

}
