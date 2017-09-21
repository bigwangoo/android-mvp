package com.tianxiabuyi.txmvp.model;

import com.tianxiabuyi.mvp.dagger.scope.ActivityScope;
import com.tianxiabuyi.mvp.manager.IRepositoryManager;
import com.tianxiabuyi.mvp.mvp.BaseModel;
import com.tianxiabuyi.txmvp.contract.UserContract;
import com.tianxiabuyi.txmvp.model.api.cache.CommonCache;
import com.tianxiabuyi.txmvp.model.api.service.UserService;
import com.tianxiabuyi.txmvp.model.bean.UserBean;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import io.rx_cache2.Reply;

/**
 * Created in 2017/9/21 20:24.
 *
 * @author Wang YaoDong.
 */
@ActivityScope
public class UserModel extends BaseModel implements UserContract.Model {

    public static final int USERS_PER_PAGE = 10;

    @Inject
    public UserModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }

    @Override
    public Observable<List<UserBean>> getUsers(final int lastIdQueried, final boolean update) {
        // 使用rxCache缓存,上拉刷新则不读取缓存, 加载更多读取缓存
        return Observable.just(mRepositoryManager.obtainRetrofitService(UserService.class)
                .getUsers(lastIdQueried, USERS_PER_PAGE))
                .flatMap(new Function<Observable<List<UserBean>>, ObservableSource<List<UserBean>>>() {
                    @Override
                    public ObservableSource<List<UserBean>> apply(@NonNull Observable<List<UserBean>> listObservable) throws Exception {
                        return mRepositoryManager.obtainCacheService(CommonCache.class)
                                .getUsers(listObservable, new DynamicKey(lastIdQueried), new EvictDynamicKey(update))
                                .map(new Function<Reply<List<UserBean>>, List<UserBean>>() {
                                    @Override
                                    public List<UserBean> apply(@NonNull Reply<List<UserBean>> listReply) throws Exception {
                                        return listReply.getData();
                                    }
                                });
                    }
                });
    }

}

