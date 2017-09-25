package com.tianxiabuyi.txmvp.mvp.presenter;

import android.app.Application;
import android.support.v7.widget.RecyclerView;

import com.tianxiabuyi.mvp.http.error.ErrorHandleSubscriber;
import com.tianxiabuyi.mvp.http.error.RetryWithDelay;
import com.tianxiabuyi.mvp.http.error.RxErrorHandler;
import com.tianxiabuyi.mvp.manager.AppManager;
import com.tianxiabuyi.mvp.mvp.BasePresenter;
import com.tianxiabuyi.mvp.utils.PermissionUtil;
import com.tianxiabuyi.mvp.utils.RxLifecycleUtils;
import com.tianxiabuyi.txmvp.mvp.contract.UserContract;
import com.tianxiabuyi.txmvp.mvp.model.bean.UserBean;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created in 2017/9/21 15:30.
 *
 * @author Wang YaoDong.
 */
public class UserPresenter extends BasePresenter<UserContract.Model, UserContract.View> {

    private RxErrorHandler mErrorHandler;
    private AppManager mAppManager;
    private Application mApplication;
    private List<UserBean> mUsers;
    private RecyclerView.Adapter mAdapter;
    private boolean isFirst = true;
    private int lastUserId = 1;
    private int preEndIndex;

    @Inject
    public UserPresenter(UserContract.Model model, UserContract.View rootView,
                         RxErrorHandler handler, AppManager appManager, Application application,
                         List<UserBean> list, RecyclerView.Adapter adapter) {
        super(model, rootView);
        this.mApplication = application;
        this.mErrorHandler = handler;
        this.mAppManager = appManager;
        this.mUsers = list;
        this.mAdapter = adapter;
    }

    /**
     * baseActivity onDestroy调用
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mAdapter = null;
        this.mUsers = null;
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mApplication = null;
    }

    /**
     * 加载数据
     *
     * @param pullToRefresh 下拉刷新
     */
    public void requestUsers(final boolean pullToRefresh) {
        // 请求外部存储权限
        PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                //request permission success, do something.
                mRootView.showMessage("Request permissions success");
            }

            @Override
            public void onRequestPermissionFailure() {
                mRootView.showMessage("Request permissions failure");
            }
        }, mRootView.getRxPermissions(), mErrorHandler);

        // 下拉刷新默认只请求第一页
        if (pullToRefresh) lastUserId = 1;

        // RxCache缓存
        boolean isEvictCache = pullToRefresh;
        // 是否驱逐缓存,为true即不使用缓存,每次下拉刷新即需要最新数据,则不使用缓存
        if (pullToRefresh && isFirst) { //默认在第一次下拉刷新时使用缓存
            isFirst = false;
            isEvictCache = false;
        }

        mModel.getUsers(lastUserId, isEvictCache)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2)) // 重试(次数,间隔)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (pullToRefresh) {
                            //显示下拉刷新的进度条
                            mRootView.showLoading();
                        } else {
                            //显示上拉加载更多的进度条
                            mRootView.startLoadMore();
                        }
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (pullToRefresh) {
                            mRootView.hideLoading();//隐藏下拉刷新的进度条
                        } else {
                            mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                        }
                    }
                })
                // 使用RxLifecycle, 使 Disposable和Activity一起销毁
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))
                .subscribe(new ErrorHandleSubscriber<Object>(mErrorHandler) {
                    @Override
                    public void onNext(Object o) {
                        List<UserBean> users = (List<UserBean>) o;
                        // 记录最后一个id,用于下一次请求
                        lastUserId = users.get(users.size() - 1).getId();
                        // 如果是下拉刷新则清空列表
                        if (pullToRefresh) mUsers.clear();
                        // 更新之前列表总长度,用于确定加载更多的起始位置
                        preEndIndex = mUsers.size();
                        mUsers.addAll(users);
                        if (pullToRefresh)
                            mAdapter.notifyDataSetChanged();
                        else
                            mAdapter.notifyItemRangeInserted(preEndIndex, users.size());
                    }
                });
    }
}
