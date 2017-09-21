package com.tianxiabuyi.mvp.mvp;

import android.app.Activity;

/**
 * Created in 2017/9/20 14:06.
 *
 * @author Wang YaoDong.
 */
public interface IPresenter {

    /**
     * 执行初始化操作
     */
    void onStart();

    /**
     * 框架中{@link Activity#onDestroy()}会默认调用次方法
     */
    void onDestroy();
}
