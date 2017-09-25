package com.tianxiabuyi.mvp.base.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;

/**
 * {@link Activity} 代理类
 * <p>
 * Created in 2017/9/21 16:47.
 *
 * @author Wang YaoDong.
 */
public interface IActivityDelegate extends Parcelable {

    String ACTIVITY_DELEGATE = "activity_delegate";

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onSaveInstanceState(Bundle outState);

    void onDestroy();
}
