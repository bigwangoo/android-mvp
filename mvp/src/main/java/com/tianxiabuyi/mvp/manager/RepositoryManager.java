package com.tianxiabuyi.mvp.manager;

import android.app.Application;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;
import io.rx_cache2.internal.RxCache;
import retrofit2.Retrofit;

/**
 * 用来管理网络请求层,以及数据缓存层,以后可能添加数据库请求层
 * <p>
 * Created in 2017/9/20 14:50.
 *
 * @author Wang YaoDong.
 */
@Singleton
public class RepositoryManager {

    private Application mApplication;

    private Lazy<Retrofit> mRetrofit;
    private Lazy<RxCache> mRxCache;

    private final Map<String, Object> mRetrofitServiceCache = new HashMap<>();
    private final Map<String, Object> mCacheServiceCache = new HashMap<>();

    @Inject
    public RepositoryManager(Application mApplication, Lazy<Retrofit> mRetrofit, Lazy<RxCache> mRxCache) {
        this.mApplication = mApplication;
        this.mRetrofit = mRetrofit;
        this.mRxCache = mRxCache;
    }

    public Context getContext() {
        return mApplication;
    }

    /**
     * 根据传入的 Class 获取对应的 Retrofit service
     */
    public <T> T obtainRetrofitService(Class<T> service) {
        T retrofitService;
        synchronized (mRetrofitServiceCache) {
            retrofitService = (T) mRetrofitServiceCache.get(service.getName());
            if (retrofitService == null) {
                retrofitService = mRetrofit.get().create(service);
                mRetrofitServiceCache.put(service.getName(), retrofitService);
            }
        }
        return retrofitService;
    }

    /**
     * 根据传入的 Class 获取对应的 RxCache service
     */
    public <T> T obtainCacheService(Class<T> cache) {
        T cacheService;
        synchronized (mCacheServiceCache) {
            cacheService = (T) mCacheServiceCache.get(cache.getName());
            if (cacheService == null) {
                cacheService = mRxCache.get().using(cache);
                mCacheServiceCache.put(cache.getName(), cacheService);
            }
        }
        return cacheService;
    }

    /**
     * 清理所有缓存
     */
    public void clearAllCache() {
        mRxCache.get().evictAll();
    }
}
