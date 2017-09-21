package com.tianxiabuyi.mvp.http.error;

import android.content.Context;

/**
 * Created in 2017/9/21 16:19.
 *
 * @author Wang YaoDong.
 */
public interface ResponseErrorListener {

    void handleResponseError(Context context, Throwable t);

    ResponseErrorListener EMPTY = new ResponseErrorListener() {
        @Override
        public void handleResponseError(Context context, Throwable t) {

        }
    };
}