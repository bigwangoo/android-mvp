package com.tianxiabuyi.txmvp.app.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.tianxiabuyi.txmvp.R;

import timber.log.Timber;

/**
 * Created in 2017/9/22 13:01.
 *
 * @author Wang YaoDong.
 */
public class ActivityLifecycleCallbacksImpl implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Timber.w(activity + " - onActivityCreated");
    }

    @Override
    public void onActivityStarted(final Activity activity) {
        Timber.w(activity + " - onActivityStarted");

        if (!activity.getIntent().getBooleanExtra("isInitToolbar", false)) {
            // 加强框架的兼容性, onActivityStarted执行
            activity.getIntent().putExtra("isInitToolbar", true);
            // 这里全局给Activity设置toolbar和title, 发挥想象力
            if (activity.findViewById(R.id.toolbar) != null) {
                if (activity instanceof AppCompatActivity) {
                    android.support.v7.widget.Toolbar toolbar = activity.findViewById(R.id.toolbar);
                    ((AppCompatActivity) activity).setSupportActionBar(toolbar);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        android.widget.Toolbar toolbar = activity.findViewById(R.id.toolbar);
                        activity.setActionBar(toolbar);
                    }
                }
            }
            if (activity.findViewById(R.id.toolbar_title) != null) {
                ((TextView) activity.findViewById(R.id.toolbar_title)).setText(activity.getTitle());
            }
            if (activity.findViewById(R.id.toolbar_back) != null) {
                activity.findViewById(R.id.toolbar_back).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        activity.onBackPressed();
                    }
                });
            }
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        Timber.w(activity + " - onActivityResumed");
    }

    @Override
    public void onActivityPaused(Activity activity) {
        Timber.w(activity + " - onActivityPaused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Timber.w(activity + " - onActivityStopped");
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        Timber.w(activity + " - onActivitySaveInstanceState");
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Timber.w(activity + " - onActivityDestroyed");
    }
}
