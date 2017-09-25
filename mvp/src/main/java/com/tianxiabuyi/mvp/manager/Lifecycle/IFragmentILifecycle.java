package com.tianxiabuyi.mvp.manager.Lifecycle;

import android.support.v4.app.Fragment;

import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.FragmentEvent;

/**
 * {@link Fragment} 实现此接口,即可正常使用 {@link RxLifecycle}
 * <p>
 * Created in 2017/9/20 15:48.
 *
 * @author Wang YaoDong.
 */
public interface IFragmentILifecycle extends ILifecycleSubject<FragmentEvent> {
}
