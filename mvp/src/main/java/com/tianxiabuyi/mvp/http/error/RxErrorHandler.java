package com.tianxiabuyi.mvp.http.error;

import android.content.Context;

/**
 * rx 错误处理
 * Created in 2017/9/21 16:18.
 *
 * @author Wang YaoDong.
 */
public class RxErrorHandler {

    public final String TAG = this.getClass().getSimpleName();

    private ErrorHandlerFactory mHandlerFactory;

    private RxErrorHandler(Builder builder) {
        this.mHandlerFactory = builder.errorHandlerFactory;
    }

    public static Builder builder() {
        return new RxErrorHandler.Builder();
    }

    public ErrorHandlerFactory getHandlerFactory() {
        return mHandlerFactory;
    }

    public static final class Builder {
        private Context context;
        private ResponseErrorListener mResponseErrorListener;
        private ErrorHandlerFactory errorHandlerFactory;

        private Builder() {
        }

        public Builder with(Context context) {
            this.context = context;
            return this;
        }

        public Builder responseErrorListener(ResponseErrorListener responseErrorListener) {
            this.mResponseErrorListener = responseErrorListener;
            return this;
        }

        public RxErrorHandler build() {
            if (context == null)
                throw new IllegalStateException("Context is required");
            if (mResponseErrorListener == null)
                throw new IllegalStateException("ResponseErrorListener is required");
            this.errorHandlerFactory = new ErrorHandlerFactory(context, mResponseErrorListener);
            return new RxErrorHandler(this);
        }
    }
}
