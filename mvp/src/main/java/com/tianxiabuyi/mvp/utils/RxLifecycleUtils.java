package com.tianxiabuyi.mvp.utils;

import com.tianxiabuyi.mvp.manager.Lifecycle.IActivityILifecycle;
import com.tianxiabuyi.mvp.manager.Lifecycle.IFragmentILifecycle;
import com.tianxiabuyi.mvp.manager.Lifecycle.ILifecycleSubject;
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
        if (view instanceof IActivityILifecycle) {
            return bindUntilEvent((IActivityILifecycle) view, event);
        } else {
            throw new IllegalArgumentException("view isn't IActivityILifecycle");
        }
    }

    /**
     * 绑定 Fragment 的指定生命周期
     */
    public static <T> LifecycleTransformer<T> bindUntilEvent(@NonNull final IView view,
                                                             final FragmentEvent event) {
        PreconditionUtils.checkNotNull(view, "view == null");
        if (view instanceof IFragmentILifecycle) {
            return bindUntilEvent((IFragmentILifecycle) view, event);
        } else {
            throw new IllegalArgumentException("view isn't FragmentLifecycleAble");
        }
    }

    public static <T, R> LifecycleTransformer<T> bindUntilEvent(
            @NonNull final ILifecycleSubject<R> lifecycleAble, final R event) {
        PreconditionUtils.checkNotNull(lifecycleAble, "lifecycleSubject == null");
        return RxLifecycle.bindUntilEvent(lifecycleAble.provideLifecycleSubject(), event);
    }

    /**
     * 绑定 Activity/Fragment 的生命周期
     */
    public static <T> LifecycleTransformer<T> bindToLifecycle(@NonNull IView view) {
        PreconditionUtils.checkNotNull(view, "view == null");
        if (view instanceof ILifecycleSubject) {
            return bindToLifecycle((ILifecycleSubject) view);
        } else {
            throw new IllegalArgumentException("view isn't lifecycleSubject");
        }
    }

    public static <T> LifecycleTransformer<T> bindToLifecycle(@NonNull ILifecycleSubject lifecycleAble) {
        PreconditionUtils.checkNotNull(lifecycleAble, "lifecycleSubject == null");
        if (lifecycleAble instanceof IActivityILifecycle) {
            return RxLifecycleAndroid
                    .bindActivity(((IActivityILifecycle) lifecycleAble).provideLifecycleSubject());
        } else if (lifecycleAble instanceof IFragmentILifecycle) {
            return RxLifecycleAndroid
                    .bindFragment(((IFragmentILifecycle) lifecycleAble).provideLifecycleSubject());
        } else {
            throw new IllegalArgumentException("ILifecycleSubject not match");
        }
    }

}

