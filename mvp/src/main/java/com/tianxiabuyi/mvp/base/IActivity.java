package com.tianxiabuyi.mvp.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.tianxiabuyi.mvp.dagger.component.AppComponent;

import org.simple.eventbus.EventBus;

/**
 * 规范 {@link Activity}
 * <p>
 * Created in 2017/9/21 16:43.
 *
 * @author Wang YaoDong.
 */
public interface IActivity {

    /**
     * 提供 AppComponent(提供所有的单例对象)给实现类,进行 Component 依赖
     *
     * @param appComponent
     */
    void setupActivityComponent(AppComponent appComponent);

    /**
     * 是否使用 {@link EventBus}
     *
     * @return
     */
    boolean useEventBus();

    /**
     * 初始化 View,如果initView返回0,框架则不会调用{@link Activity#setContentView(int)}
     *
     * @param savedInstanceState
     * @return
     */
    int initView(Bundle savedInstanceState);

    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    void initData(Bundle savedInstanceState);

    /**
     * 这个Activity是否会使用Fragment,框架会根据这个属性判断是否注册
     * {@link FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回false,那意味着这个Activity不需要绑定Fragment,那你再在这个Activity中绑定继承于
     * {@link BaseFragment} 的Fragment将不起任何作用
     *
     * @return
     */
    boolean useFragment();
}
