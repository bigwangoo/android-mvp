package com.tianxiabuyi.txmvp.utils;

import com.tianxiabuyi.mvp.mvp.IView;
import com.tianxiabuyi.mvp.utils.RxLifecycleUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created in 2017/9/20 16:44.
 *
 * @author Wang YaoDong.
 */
public class RxUtils {

/*    public static <T> ObservableTransformer<T, T> applySchedulers(final IView view) {
        return new ObservableTransformer<T, T>() {
            @Override
            public Observable<T> apply(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(@NonNull Disposable disposable) throws Exception {
                                view.showLoading(); //显示进度条
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(new Action() {
                            @Override
                            public void run() {
                                view.hideLoading(); //隐藏进度条
                            }
                        })
                        .compose(RxLifecycleUtils.bindToLifecycle(view));
            }
        };
    }*/

}
