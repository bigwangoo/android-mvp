package com.tianxiabuyi.mvp.manager.Lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.tianxiabuyi.mvp.base.IActivity;
import com.tianxiabuyi.mvp.base.delegate.ActivityDelegateImpl;
import com.tianxiabuyi.mvp.base.delegate.IActivityDelegate;
import com.tianxiabuyi.mvp.manager.AppManager;
import com.tianxiabuyi.mvp.manager.ConfigModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created in 2017/9/22 15:09.
 *
 * @author Wang YaoDong.
 */
public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private Application mApplication;
    private AppManager mAppManager;         // activity管理
    private Map<String, Object> mExtras;    // 额外数据

    private FragmentManager.FragmentLifecycleCallbacks mFragmentLifecycle;
    private List<FragmentManager.FragmentLifecycleCallbacks> mFragmentLifecycleList;

    @Inject
    public ActivityLifecycle(Application application, AppManager appManager, Map<String, Object> extras) {
        this.mApplication = application;
        this.mAppManager = appManager;
        this.mExtras = extras;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        // 如果intent包含了此字段,并且为true说明不加入到list进行统一管理
        boolean isNotAdd = false;
        if (activity.getIntent() != null) {
            isNotAdd = activity.getIntent().getBooleanExtra(AppManager.IS_NOT_ADD_ACTIVITY_LIST, false);
        }
        if (!isNotAdd) {
            mAppManager.addActivity(activity);
        }

        // 配置ActivityDelegate
        IActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate == null) {
            activityDelegate = new ActivityDelegateImpl(activity);
            activity.getIntent().putExtra(IActivityDelegate.ACTIVITY_DELEGATE, activityDelegate);
        }
        activityDelegate.onCreate(savedInstanceState);

        // 监听fragment生命周期
        registerFragmentCallbacks(activity);
    }


    @Override
    public void onActivityStarted(Activity activity) {
        IActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onStart();
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        mAppManager.setCurrentActivity(activity);

        IActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onResume();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
        IActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onPause();
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (mAppManager.getCurrentActivity() == activity) {
            mAppManager.setCurrentActivity(null);
        }

        IActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onStop();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        IActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mAppManager.removeActivity(activity);

        IActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            activityDelegate.onDestroy();
            activity.getIntent().removeExtra(IActivityDelegate.ACTIVITY_DELEGATE);
        }
    }

    /**
     * 获取 activity代理
     */
    private IActivityDelegate fetchActivityDelegate(Activity activity) {
        IActivityDelegate activityDelegate = null;
        if (activity instanceof IActivity && activity.getIntent() != null) {
            activityDelegate = activity.getIntent().getParcelableExtra(IActivityDelegate.ACTIVITY_DELEGATE);
        }
        return activityDelegate;
    }

    /**
     * 设置Activity下Fragment生命周期监听
     * <p>
     * 可以通过 {@link IActivity#useFragment()} 是否启用
     */
    private void registerFragmentCallbacks(Activity activity) {
        boolean useFragment = false;
        if (activity instanceof IActivity) {
            ((IActivity) activity).useFragment();
        } else {
            useFragment = true;
        }
        if (activity instanceof FragmentActivity && useFragment) {
            if (mFragmentLifecycle == null) {
                mFragmentLifecycle = new FragmentLifecycle();
            }
            ((FragmentActivity) activity).getSupportFragmentManager()
                    .registerFragmentLifecycleCallbacks(mFragmentLifecycle, true);
            if (mFragmentLifecycleList == null && mExtras.containsKey(ConfigModule.class.getName())) {
                mFragmentLifecycleList = new ArrayList<>();
                List<ConfigModule> modules = (List<ConfigModule>) mExtras.get(ConfigModule.class.getName());
                for (ConfigModule module : modules) {
                    module.injectFragmentLifecycle(mApplication, mFragmentLifecycleList);
                }
                mExtras.put(ConfigModule.class.getName(), null);
            }

            for (FragmentManager.FragmentLifecycleCallbacks fragmentLifecycle : mFragmentLifecycleList) {
                ((FragmentActivity) activity).getSupportFragmentManager()
                        .registerFragmentLifecycleCallbacks(fragmentLifecycle, true);
            }
        }
    }
}
