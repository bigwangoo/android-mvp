package com.tianxiabuyi.mvp.manager.Lifecycle;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.trello.rxlifecycle2.RxLifecycle;

import io.reactivex.subjects.Subject;

/**
 * Rx 生命周期管理
 * 无需再继承{@link RxLifecycle}提供的Activity/Fragment,
 * {@link Activity}/{@link Fragment}实现此接口即可, 扩展性极强
 * <p>
 * Created in 2017/9/20 15:43.
 *
 * @author Wang YaoDong.
 */
public interface ILifecycleSubject<E> {

    @NonNull
    Subject<E> provideLifecycleSubject();
}
