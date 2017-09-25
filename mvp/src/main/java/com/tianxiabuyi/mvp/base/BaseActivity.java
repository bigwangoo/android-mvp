package com.tianxiabuyi.mvp.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tianxiabuyi.mvp.manager.Lifecycle.IActivityILifecycle;
import com.tianxiabuyi.mvp.mvp.IPresenter;
import com.trello.rxlifecycle2.android.ActivityEvent;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * Created in 2017/9/20 15:22.
 *
 * @author Wang YaoDong.
 */
public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity implements
        IActivity, IActivityILifecycle {

    protected final String TAG = this.getClass().getSimpleName();

    // 生命周期管理
    private final BehaviorSubject<ActivityEvent> mLifecycleSubject = BehaviorSubject.create();
    // butterKnife 绑定
    private Unbinder mUnBinder;

    @Inject
    protected P mPresenter; // dagger2依赖注入


    @NonNull
    @Override
    public final Subject<ActivityEvent> provideLifecycleSubject() {
        return mLifecycleSubject;
    }

    /**
     * 初始化
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            int layoutResID = initView(savedInstanceState);
            if (layoutResID != 0) {
                // 如果initView返回0,框架则不会调用setContentView(),当然也不会 Bind ButterKnife
                setContentView(layoutResID);
                // 绑定到butterKnife
                mUnBinder = ButterKnife.bind(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initData(savedInstanceState);
    }

    /**
     * 对象回收,释放资源
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnBinder != null && mUnBinder != Unbinder.EMPTY)
            mUnBinder.unbind();
        this.mUnBinder = null;
        // 释放资源
        if (mPresenter != null)
            mPresenter.onDestroy();
        this.mPresenter = null;
    }

    /**
     * 是否使用eventBus,默认为使用(true)，
     *
     * @return boolean
     */
    @Override
    public boolean useEventBus() {
        return true;
    }

    /**
     * Activity是否使用Fragment,框架会根据这个属性判断是否注册
     * <p>
     * true 注册fragment框架监听生命周期
     * false 不绑定Fragment,也将不起任何作用
     * {@link android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks}
     */
    @Override
    public boolean useFragment() {
        return true;
    }
}
