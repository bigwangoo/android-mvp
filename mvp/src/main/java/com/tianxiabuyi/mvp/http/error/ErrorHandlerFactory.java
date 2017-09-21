package com.tianxiabuyi.mvp.http.error;

import android.content.Context;


/**
 * Created in 2017/9/21 16:19.
 *
 * @author Wang YaoDong.
 */
public class ErrorHandlerFactory {

    public final String TAG = this.getClass().getSimpleName();

    private Context mContext;
    private ResponseErrorListener mResponseErrorListener;

    public ErrorHandlerFactory(Context mContext, ResponseErrorListener mResponseErrorListener) {
        this.mResponseErrorListener = mResponseErrorListener;
        this.mContext = mContext;
    }

    /**
     * 处理错误
     */
    public void handleError(Throwable throwable) {
        mResponseErrorListener.handleResponseError(mContext, throwable);
    }
}
