package com.tianxiabuyi.mvp.base.delegate;

import android.os.Bundle;
import android.os.Parcelable;

/**
 * Created in 2017/9/21 16:47.
 *
 * @author Wang YaoDong.
 */
public interface IActivityDelegate extends Parcelable {

    String LAYOUT_LINEARLAYOUT = "LinearLayout";
    String LAYOUT_FRAMELAYOUT = "FrameLayout";
    String LAYOUT_RELATIVELAYOUT = "RelativeLayout";
    String ACTIVITY_DELEGATE = "activity_delegate";

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onSaveInstanceState(Bundle outState);

    void onDestroy();
}
