package com.tianxiabuyi.mvp.manager;

import android.content.Context;

import com.tianxiabuyi.mvp.mvp.IModel;

/**
 * 用来管理 网络请求层,数据缓存层,数据库请求层
 * 给 {@link IModel} 提供必要的 Api 做数据处理
 * <p>
 * Created in 2017/9/20 14:35.
 *
 * @author Wang YaoDong.
 */
public interface IRepositoryManager {

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     *
     * @param service
     * @param <T>
     * @return
     */
    <T> T obtainRetrofitService(Class<T> service);

    /**
     * 根据传入的 Class 获取对应的 RxCache service
     *
     * @param cache
     * @param <T>
     * @return
     */
    <T> T obtainCacheService(Class<T> cache);

    /**
     * 清理所有缓存
     */
    void clearAllCache();

    Context getContext();
}
