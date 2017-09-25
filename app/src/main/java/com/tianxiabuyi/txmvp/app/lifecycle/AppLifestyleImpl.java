package com.tianxiabuyi.txmvp.app.lifecycle;

import android.app.Application;
import android.content.Context;
import android.os.Message;
import android.support.multidex.MultiDex;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tianxiabuyi.mvp.BuildConfig;
import com.tianxiabuyi.mvp.base.delegate.IAppLifecycle;
import com.tianxiabuyi.mvp.manager.AppManager;
import com.tianxiabuyi.mvp.utils.AppUtils;

import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created in 2017/9/22 12:42.
 *
 * @author Wang YaoDong.
 */
public class AppLifestyleImpl implements IAppLifecycle {

    @Override
    public void attachBaseContext(Context base) {
        MultiDex.install(base); // 先于onCreate执行
    }

    @Override
    public void onCreate(Application application) {
        // Timber 初始化, 参考 GitHub
        if (BuildConfig.DEBUG) {
            // Timber(日志框架容器), 外部使用统一的Api,内部可以动态的切换成任何日志框架(打印策略),
            // 并且支持添加多个日志框架(打印策略), 做到外部调用一次 Api, 内部却可以做到同时使用多个策略
            // 比如添加三个策略, 一个打印日志,一个将日志保存本地,一个将日志上传服务器
            // Timber.plant(new Timber.DebugTree());

            // 使用Logger 打印日志
            Logger.addLogAdapter(new AndroidLogAdapter());
            Timber.plant(new Timber.DebugTree() {
                @Override
                protected void log(int priority, String tag, String message, Throwable t) {
                    Logger.log(priority, tag, message, t);
                }
            });
            ButterKnife.setDebug(true);
        }
        // leakCanary内存泄露检查
        AppUtils.obtainAppComponentFromContext(application)
                .extras()
                .put(RefWatcher.class.getName(),
                        BuildConfig.DEBUG ? LeakCanary.install(application) : RefWatcher.DISABLED);
        // 扩展 AppManager 的远程遥控功能
        AppUtils.obtainAppComponentFromContext(application).appManager()
                .setHandleListener(new AppManager.HandleListener() {
                    @Override
                    public void handleMessage(AppManager appManager, Message message) {
                        switch (message.what) {
                            // case 0:
                            // do something ...
                            //   break;
                        }
                    }
                });
        // Usage:
        // Message msg = new Message();
        // msg.what = 0;
        // AppManager.post(msg); like EventBus
    }

    @Override
    public void onTerminate(Application application) {

    }
}
