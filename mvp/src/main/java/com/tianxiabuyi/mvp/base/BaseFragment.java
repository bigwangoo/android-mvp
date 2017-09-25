package com.tianxiabuyi.mvp.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tianxiabuyi.mvp.manager.Lifecycle.IFragmentILifecycle;
import com.tianxiabuyi.mvp.mvp.IPresenter;
import com.trello.rxlifecycle2.android.FragmentEvent;

import javax.inject.Inject;

import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

/**
 * fragment 基类
 * Created in 2017/9/20 15:13.
 *
 * @author Wang YaoDong.
 */
public abstract class BaseFragment<P extends IPresenter> extends Fragment
        implements IFragment, IFragmentILifecycle {

    protected final String TAG = this.getClass().getSimpleName();

    private final BehaviorSubject<FragmentEvent> mLifecycleSubject = BehaviorSubject.create();

    @Inject
    protected P mPresenter;

    @NonNull
    @Override
    public final Subject<FragmentEvent> provideLifecycleSubject() {
        return mLifecycleSubject;
    }

    public BaseFragment() {
        //必须确保在Fragment实例化时setArguments()
        setArguments(new Bundle());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) mPresenter.onDestroy();//释放资源
        this.mPresenter = null;
    }

    /**
     * 是否使用eventBus,默认为使用(true)，
     */
    @Override
    public boolean useEventBus() {
        return true;
    }

}
