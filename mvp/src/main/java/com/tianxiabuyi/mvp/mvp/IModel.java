package com.tianxiabuyi.mvp.mvp;

/**
 * Created in 2017/9/20 14:06.
 *
 * @author Wang YaoDong.
 */
public interface IModel {

    /**
     * 框架中{@link BasePresenter#onDestroy()} 会默认调用此方法
     */
    void onDestroy();
}
