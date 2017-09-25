package com.tianxiabuyi.txmvp.mvp.model.api.cache;

import com.tianxiabuyi.txmvp.mvp.model.bean.UserBean;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.LifeCache;
import io.rx_cache2.Reply;

/**
 * Created in 2017/9/21 20:26.
 *
 * @author Wang YaoDong.
 */
public interface CommonCache {

    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<Reply<List<UserBean>>> getUsers(Observable<List<UserBean>> users,
                                               DynamicKey idLastUserQueried,
                                               EvictProvider evictProvider);
}

