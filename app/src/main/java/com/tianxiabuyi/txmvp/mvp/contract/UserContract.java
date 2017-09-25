package com.tianxiabuyi.txmvp.mvp.contract;

import android.app.Activity;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tianxiabuyi.mvp.mvp.IModel;
import com.tianxiabuyi.mvp.mvp.IView;
import com.tianxiabuyi.txmvp.mvp.model.bean.UserBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * User 业务
 * <p>
 * Created in 2017/9/21 15:33.
 *
 * @author Wang YaoDong.
 */
public interface UserContract {

    /**
     * 对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
     */
    interface View extends IView {

        void startLoadMore();

        void endLoadMore();

        Activity getActivity();

        // 申请权限
        RxPermissions getRxPermissions();
    }

    /**
     * Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,如是否使用缓存
     */
    interface Model extends IModel {

        Observable<List<UserBean>> getUsers(int lastIdQueried, boolean update);
    }
}
