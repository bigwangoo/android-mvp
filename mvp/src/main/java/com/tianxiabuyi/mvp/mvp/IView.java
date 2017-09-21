package com.tianxiabuyi.mvp.mvp;

import android.app.Activity;
import android.content.Intent;

/**
 * <p>
 * 每个View实现此接口
 * <p>
 * Created in 2017/9/20 14:06.
 *
 * @author Wang YaoDong.
 */
public interface IView {

    /**
     * 显示加载
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 显示信息
     */
    void showMessage(String msg);

    /**
     * 跳转 {@link Activity}
     */
    void launchActivity(Intent intent);

    /**
     * 结束自己
     */
    void killSelf();
}
