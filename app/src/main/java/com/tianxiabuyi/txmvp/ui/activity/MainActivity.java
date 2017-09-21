package com.tianxiabuyi.txmvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.paginate.Paginate;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tianxiabuyi.mvp.base.BaseActivity;
import com.tianxiabuyi.mvp.dagger.component.AppComponent;
import com.tianxiabuyi.mvp.utils.AppUtils;
import com.tianxiabuyi.txmvp.R;
import com.tianxiabuyi.txmvp.contract.UserContract;
import com.tianxiabuyi.txmvp.dagger.component.DaggerUserComponent;
import com.tianxiabuyi.txmvp.dagger.module.UserModule;
import com.tianxiabuyi.txmvp.presenter.UserPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import timber.log.Timber;

/**
 * demo
 */
public class MainActivity extends BaseActivity<UserPresenter> implements
        UserContract.View, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv)
    RecyclerView mRv;
    @BindView(R.id.srl)
    SwipeRefreshLayout mSrl;

    @Inject
    RxPermissions mRxPermissions;
    @Inject
    RecyclerView.Adapter mAdapter;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;

    // 分页
    private Paginate mPaginate;
    // 加载更多
    private boolean isLoadingMore;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        // 编译生成
        DaggerUserComponent.builder()
                .appComponent(appComponent)
                .userModule(new UserModule(this))
                .build().inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        initRV();

        initPaginate();
        // 打开时自动加载列表
        mPresenter.requestUsers(true);
    }

    private void initRV() {
        mSrl.setOnRefreshListener(this);
        AppUtils.configRecycleView(mRv, mLayoutManager);
        mRv.setAdapter(mAdapter);
    }

    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    mPresenter.requestUsers(false);
                }

                @Override
                public boolean isLoading() {
                    return isLoadingMore;
                }

                @Override
                public boolean hasLoadedAllItems() {
                    return false;
                }
            };

            mPaginate = Paginate.with(mRv, callbacks)
                    .setLoadingTriggerThreshold(0).build();
            mPaginate.setHasMoreDataToLoad(false);
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.requestUsers(true);
    }

    @Override
    public void showLoading() {
        Timber.tag(TAG).w("showLoading");
        mSrl.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        Timber.tag(TAG).w("hideLoading");
        mSrl.setRefreshing(false);
    }

    @Override
    public void showMessage(String msg) {
        AppUtils.snackbarText(msg);
    }

    @Override
    public void launchActivity(Intent intent) {

    }

    @Override
    public void killSelf() {
        finish();
    }

    @Override
    public void startLoadMore() {
        isLoadingMore = true;
    }

    @Override
    public void endLoadMore() {
        isLoadingMore = false;
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onDestroy() {
        // DefaultAdapter.releaseAllHolder(mRecyclerView);
        //super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();
        this.mRxPermissions = null;
        this.mPaginate = null;
    }
}
