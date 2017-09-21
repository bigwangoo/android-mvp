package com.tianxiabuyi.mvp.http.error;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


/**
 * Created in 2017/9/21 16:19.
 *
 * @author Wang YaoDong.
 */
public abstract class ErrorHandleSubscriber<T> implements Observer<T> {

    private ErrorHandlerFactory mHandlerFactory;

    public ErrorHandleSubscriber(RxErrorHandler rxErrorHandler) {
        this.mHandlerFactory = rxErrorHandler.getHandlerFactory();
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(@NonNull Throwable e) {
        e.printStackTrace();
        mHandlerFactory.handleError(e);
    }
}

