package com.tianxiabuyi.mvp.manager.Lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.trello.rxlifecycle2.android.ActivityEvent;

import javax.inject.Inject;

import io.reactivex.subjects.Subject;

/**
 * Created in 2017/9/20 15:47.
 *
 * @author Wang YaoDong.
 */
public class ActivityLifecycleForRxLifecycle implements Application.ActivityLifecycleCallbacks {

    private FragmentManager.FragmentLifecycleCallbacks mFragmentLifecycle;

    @Inject
    public ActivityLifecycleForRxLifecycle() {
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (activity instanceof IActivityILifecycle) {
            obtainSubject(activity).onNext(ActivityEvent.CREATE);
            if (activity instanceof FragmentActivity) {
                if (mFragmentLifecycle == null) {
                    mFragmentLifecycle = new FragmentLifecycleForRxLifecycle();
                }
                ((FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(mFragmentLifecycle, true);
            }
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (activity instanceof IActivityILifecycle) {
            obtainSubject(activity).onNext(ActivityEvent.START);
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (activity instanceof IActivityILifecycle) {
            obtainSubject(activity).onNext(ActivityEvent.RESUME);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        if (activity instanceof IActivityILifecycle) {
            obtainSubject(activity).onNext(ActivityEvent.PAUSE);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (activity instanceof IActivityILifecycle) {
            obtainSubject(activity).onNext(ActivityEvent.STOP);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activity instanceof IActivityILifecycle) {
            obtainSubject(activity).onNext(ActivityEvent.DESTROY);
        }
    }

    private Subject<ActivityEvent> obtainSubject(Activity activity) {
        return ((IActivityILifecycle) activity).provideLifecycleSubject();
    }
}
