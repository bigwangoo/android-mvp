package com.tianxiabuyi.mvp.manager.Lifecycle;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.tianxiabuyi.mvp.base.IFragment;
import com.tianxiabuyi.mvp.base.delegate.FragmentDelegateImpl;
import com.tianxiabuyi.mvp.base.delegate.IFragmentDelegate;

import timber.log.Timber;

/**
 * {@link FragmentManager.FragmentLifecycleCallbacks} 默认实现
 * <p>
 * Created in 2017/9/22 15:10.
 *
 * @author Wang YaoDong.
 */
public class FragmentLifecycle extends FragmentManager.FragmentLifecycleCallbacks {

    @Override
    public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
        Timber.w(f.toString() + " - onFragmentAttached");
        if (f instanceof IFragment && f.getArguments() != null) {
            IFragmentDelegate fragmentDelegate = fetchFragmentDelegate(f);
            if (fragmentDelegate == null || !fragmentDelegate.isAdded()) {
                fragmentDelegate = new FragmentDelegateImpl(fm, f);
                f.getArguments().putParcelable(IFragmentDelegate.FRAGMENT_DELEGATE, fragmentDelegate);
            }
            fragmentDelegate.onAttach(context);
        }
    }

    @Override
    public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        Timber.w(f.toString() + " - onFragmentCreated");
        IFragmentDelegate fragmentDelegate = fetchFragmentDelegate(f);
        if (fragmentDelegate != null) {
            fragmentDelegate.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
        Timber.w(f.toString() + " - onFragmentViewCreated");
        IFragmentDelegate fragmentDelegate = fetchFragmentDelegate(f);
        if (fragmentDelegate != null) {
            fragmentDelegate.onCreateView(v, savedInstanceState);
        }
    }

    @Override
    public void onFragmentActivityCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
        Timber.w(f.toString() + " - onFragmentActivityCreated");
        IFragmentDelegate fragmentDelegate = fetchFragmentDelegate(f);
        if (fragmentDelegate != null) {
            fragmentDelegate.onActivityCreate(savedInstanceState);
        }
    }

    @Override
    public void onFragmentStarted(FragmentManager fm, Fragment f) {
        Timber.w(f.toString() + " - onFragmentStarted");
        IFragmentDelegate fragmentDelegate = fetchFragmentDelegate(f);
        if (fragmentDelegate != null) {
            fragmentDelegate.onStart();
        }
    }

    @Override
    public void onFragmentResumed(FragmentManager fm, Fragment f) {
        Timber.w(f.toString() + " - onFragmentResumed");
        IFragmentDelegate fragmentDelegate = fetchFragmentDelegate(f);
        if (fragmentDelegate != null) {
            fragmentDelegate.onResume();
        }
    }

    @Override
    public void onFragmentPaused(FragmentManager fm, Fragment f) {
        Timber.w(f.toString() + " - onFragmentPaused");
        IFragmentDelegate fragmentDelegate = fetchFragmentDelegate(f);
        if (fragmentDelegate != null) {
            fragmentDelegate.onPause();
        }
    }

    @Override
    public void onFragmentStopped(FragmentManager fm, Fragment f) {
        Timber.w(f.toString() + " - onFragmentStopped");
        IFragmentDelegate fragmentDelegate = fetchFragmentDelegate(f);
        if (fragmentDelegate != null) {
            fragmentDelegate.onStop();
        }
    }

    @Override
    public void onFragmentSaveInstanceState(FragmentManager fm, Fragment f, Bundle outState) {
        Timber.w(f.toString() + " - onFragmentSaveInstanceState");
        IFragmentDelegate fragmentDelegate = fetchFragmentDelegate(f);
        if (fragmentDelegate != null) {
            fragmentDelegate.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
        Timber.w(f.toString() + " - onFragmentViewDestroyed");
        IFragmentDelegate fragmentDelegate = fetchFragmentDelegate(f);
        if (fragmentDelegate != null) {
            fragmentDelegate.onDestroyView();
        }
    }

    @Override
    public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
        Timber.w(f.toString() + " - onFragmentDestroyed");
        IFragmentDelegate fragmentDelegate = fetchFragmentDelegate(f);
        if (fragmentDelegate != null) {
            fragmentDelegate.onDestroy();
        }
    }

    @Override
    public void onFragmentDetached(FragmentManager fm, Fragment f) {
        Timber.w(f.toString() + " - onFragmentDetached");
        IFragmentDelegate fragmentDelegate = fetchFragmentDelegate(f);
        if (fragmentDelegate != null) {
            fragmentDelegate.onDetach();
            f.getArguments().clear();
        }
    }

    private IFragmentDelegate fetchFragmentDelegate(Fragment fragment) {
        if (fragment instanceof IFragment) {
            return fragment.getArguments().getParcelable(IFragmentDelegate.FRAGMENT_DELEGATE);
        }
        return null;
    }

}
