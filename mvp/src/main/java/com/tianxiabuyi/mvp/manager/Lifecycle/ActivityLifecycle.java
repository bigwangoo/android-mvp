package com.tianxiabuyi.mvp.manager.Lifecycle;

import android.app.Activity;

import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.ActivityEvent;

/**
 * {@link Activity} 实现此接口,即可正常使用 {@link RxLifecycle}
 * <p>
 * Created in 2017/9/20 15:47.
 *
 * @author Wang YaoDong.
 */
public interface ActivityLifecycle extends LifecycleSubject<ActivityEvent> {
}
