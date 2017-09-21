package com.tianxiabuyi.mvp.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.simple.eventbus.EventBus;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * service 基类
 * <p>
 * Created in 2017/9/20 15:09.
 *
 * @author Wang YaoDong.
 */
public abstract class BaseService extends Service {

    protected final String TAG = this.getClass().getSimpleName();

    protected CompositeDisposable mCompositeDisposable;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        // 解除订阅
        unDispose();
        this.mCompositeDisposable = null;
    }

    /**
     * 初始化
     */
    abstract public void init();

    /**
     * 将所有subscription放入,集中处理
     *
     * @param disposable Disposable
     */
    protected void addDispose(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    /**
     * 保证activity结束时取消所有正在执行的订阅
     */
    protected void unDispose() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }
}
