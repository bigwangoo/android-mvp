package com.tianxiabuyi.mvp.utils;

import com.tianxiabuyi.mvp.manager.Lifecycle.ActivityLifecycle;
import com.tianxiabuyi.mvp.manager.Lifecycle.FragmentLifecycle;
import com.tianxiabuyi.mvp.manager.Lifecycle.LifecycleSubject;
import com.tianxiabuyi.mvp.mvp.IView;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import io.reactivex.annotations.NonNull;

/**
 * RxLifecycle的特性
 * <p>
 * Created in 2017/9/20 16:51.
 *
 * @author Wang YaoDong.
 */
public class RxLifecycleUtils {

    private RxLifecycleUtils() {
        throw new IllegalStateException("you can't instantiate me!");
    }

    /**
     * 绑定 Activity 的指定生命周期
     */
    public static <T> LifecycleTransformer<T> bindUntilEvent(@NonNull final IView view,
                                                             final ActivityEvent event) {
        // 判空
        PreconditionUtils.checkNotNull(view, "view == null");
        if (view instanceof ActivityLifecycle) {
            return bindUntilEvent((ActivityLifecycle) view, event);
        } else {
            throw new IllegalArgumentException("view isn't ActivityLifecycle");
        }
    }

    /**
     * 绑定 Fragment 的指定生命周期
     */
    public static <T> LifecycleTransformer<T> bindUntilEvent(@NonNull final IView view,
                                                             final FragmentEvent event) {
        PreconditionUtils.checkNotNull(view, "view == null");
        if (view instanceof FragmentLifecycle) {
            return bindUntilEvent((FragmentLifecycle) view, event);
        } else {
            throw new IllegalArgumentException("view isn't FragmentLifecycleAble");
        }
    }

    public static <T, R> LifecycleTransformer<T> bindUntilEvent(
            @NonNull final LifecycleSubject<R> lifecycleAble, final R event) {
        PreconditionUtils.checkNotNull(lifecycleAble, "lifecycleSubject == null");
        return RxLifecycle.bindUntilEvent(lifecycleAble.provideLifecycleSubject(), event);
    }

    /**
     * 绑定 Activity/Fragment 的生命周期
     */
    public static <T> LifecycleTransformer<T> bindToLifecycle(@NonNull IView view) {
        PreconditionUtils.checkNotNull(view, "view == null");
        if (view instanceof LifecycleSubject) {
            return bindToLifecycle((LifecycleSubject) view);
        } else {
            throw new IllegalArgumentException("view isn't lifecycleSubject");
        }
    }

    public static <T> LifecycleTransformer<T> bindToLifecycle(@NonNull LifecycleSubject lifecycleAble) {
        PreconditionUtils.checkNotNull(lifecycleAble, "lifecycleSubject == null");
        if (lifecycleAble instanceof ActivityLifecycle) {
            return RxLifecycleAndroid
                    .bindActivity(((ActivityLifecycle) lifecycleAble).provideLifecycleSubject());
        } else if (lifecycleAble instanceof FragmentLifecycle) {
            return RxLifecycleAndroid
                    .bindFragment(((FragmentLifecycle) lifecycleAble).provideLifecycleSubject());
        } else {
            throw new IllegalArgumentException("LifecycleSubject not match");
        }
    }

}

