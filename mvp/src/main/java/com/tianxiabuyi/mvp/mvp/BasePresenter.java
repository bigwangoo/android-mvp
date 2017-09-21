package com.tianxiabuyi.mvp.mvp;

import android.app.Activity;

import com.trello.rxlifecycle2.RxLifecycle;

import org.simple.eventbus.EventBus;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 基类 Presenter
 * Created in 2017/9/20 14:05.
 *
 * @author Wang YaoDong.
 */
public class BasePresenter<M extends IModel, V extends IView> implements IPresenter {

    protected final String TAG = this.getClass().getSimpleName();

    protected CompositeDisposable mCompositeDisposable;

    protected M mModel;
    protected V mRootView;

    public BasePresenter() {
        onStart();
    }

    /**
     * 如果当前页面不需要操作数据,只需要 View 层,则使用此构造函数
     */
    public BasePresenter(V rootView) {
        this.mRootView = rootView;
        onStart();
    }

    /**
     * 如果当前页面同时需要 Model 层和 View 层,则使用此构造函数(默认)
     */
    public BasePresenter(M model, V rootView) {
        this.mModel = model;
        this.mRootView = rootView;
        onStart();
    }

    @Override
    public void onStart() {
        // 如果要使用 EventBus 请将此方法返回 true
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        if (useEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        unDispose(); // 解除订阅
        if (mModel != null) {
            mModel.onDestroy();
        }
        this.mModel = null;
        this.mRootView = null;
        this.mCompositeDisposable = null;
    }

    /**
     * 是否使用 {@link EventBus},默认为使用(true)，
     */
    public boolean useEventBus() {
        return true;
    }


    /**
     * 停止正在执行的 RxJava 任务,避免内存泄漏
     * 将所有 Disposable 集中统一管理
     * 在 {@link Activity#onDestroy()} 中使用 {@link #unDispose()}
     * <p>
     * 目前框架已使用 {@link RxLifecycle} 避免内存泄漏,此方法作为备用方案
     *
     * @param disposable Disposable
     */
    public void addDispose(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    /**
     * 停止集合中正在执行的 RxJava 任务,
     * 保证 Activity 结束时取消所有正在执行的订阅
     */
    public void unDispose() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }
}
