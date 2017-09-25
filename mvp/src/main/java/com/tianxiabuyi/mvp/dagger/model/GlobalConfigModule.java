package com.tianxiabuyi.mvp.dagger.model;

import android.app.Application;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.tianxiabuyi.mvp.TxConstant;
import com.tianxiabuyi.mvp.http.BaseUrl;
import com.tianxiabuyi.mvp.http.GlobalHttpHandler;
import com.tianxiabuyi.mvp.http.ImageLoader.BaseImageLoaderStrategy;
import com.tianxiabuyi.mvp.http.ImageLoader.glide.GlideLoaderStrategy;
import com.tianxiabuyi.mvp.http.RequestInterceptor;
import com.tianxiabuyi.mvp.http.error.ResponseErrorListener;
import com.tianxiabuyi.mvp.utils.DataUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;

/**
 * Created in 2017/9/21 13:50.
 *
 * @author Wang YaoDong.
 */
@Module
public class GlobalConfigModule {

    // 基础api url
    private HttpUrl mApiUrl;
    // 动态获取api
    private BaseUrl mBaseUrl;
    // 图片加载策略
    private BaseImageLoaderStrategy mLoaderStrategy;
    // Http请求和响应结果的处理类
    private GlobalHttpHandler mHandler;
    // 拦截器
    private List<Interceptor> mInterceptors;
    // RxJava 错误的管理器的回调
    private ResponseErrorListener mErrorListener;
    // 缓存文件夹
    private File mCacheFile;
    // retrofit
    private ClientModule.RetrofitConfiguration mRetrofitConfiguration;
    // okHttp
    private ClientModule.OkHttpConfiguration mOkHttpConfiguration;
    // rxCache
    private ClientModule.RxCacheConfiguration mRxCacheConfiguration;
    // Gson
    private AppModule.GsonConfiguration mGsonConfiguration;
    // 日志
    private RequestInterceptor.Level mPrintHttpLogLevel;

    private GlobalConfigModule(Builder builder) {
        this.mApiUrl = builder.apiUrl;
        this.mBaseUrl = builder.baseUrl;
        this.mLoaderStrategy = builder.loaderStrategy;
        this.mHandler = builder.handler;
        this.mInterceptors = builder.interceptors;
        this.mErrorListener = builder.responseErrorListener;
        this.mCacheFile = builder.cacheFile;
        this.mRetrofitConfiguration = builder.retrofitConfiguration;
        this.mOkHttpConfiguration = builder.okHttpConfiguration;
        this.mRxCacheConfiguration = builder.rxCacheConfiguration;
        this.mGsonConfiguration = builder.gsonConfiguration;
        this.mPrintHttpLogLevel = builder.printHttpLogLevel;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * 提供 BaseUrl,默认使用 {@link TxConstant #DEFAULT_API}
     */
    @Singleton
    @Provides
    HttpUrl provideBaseUrl() {
        if (mBaseUrl != null) {
            HttpUrl httpUrl = mBaseUrl.url();
            if (httpUrl != null) {
                return httpUrl;
            }
        }
        return mApiUrl == null ? HttpUrl.parse(TxConstant.DEFAULT_API) : mApiUrl;
    }

    /**
     * 提供图片加载框架,默认使用 {@link Glide}
     */
    @Singleton
    @Provides
    BaseImageLoaderStrategy provideImageLoaderStrategy() {
        return mLoaderStrategy == null ? new GlideLoaderStrategy() : mLoaderStrategy;
    }

    @Singleton
    @Provides
    @Nullable
    GlobalHttpHandler provideGlobalHttpHandler() {
        return mHandler;
    }

    @Singleton
    @Provides
    @Nullable
    List<Interceptor> provideInterceptors() {
        return mInterceptors;
    }

    @Singleton
    @Provides
    File provideCacheFile(Application application) {
        return mCacheFile == null ? DataUtils.getCacheFile(application) : mCacheFile;
    }

    @Singleton
    @Provides
    ResponseErrorListener provideResponseErrorListener() {
        return mErrorListener == null ? ResponseErrorListener.EMPTY : mErrorListener;
    }

    @Singleton
    @Provides
    @Nullable
    ClientModule.RetrofitConfiguration provideRetrofitConfiguration() {
        return mRetrofitConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    ClientModule.OkHttpConfiguration provideOkHttpConfiguration() {
        return mOkHttpConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    ClientModule.RxCacheConfiguration provideRxCacheConfiguration() {
        return mRxCacheConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    AppModule.GsonConfiguration provideGsonConfiguration() {
        return mGsonConfiguration;
    }

    @Singleton
    @Provides
    @Nullable
    RequestInterceptor.Level providePrintHttpLogLevel() {
        return mPrintHttpLogLevel;
    }


    public static final class Builder {
        private HttpUrl apiUrl;
        private BaseUrl baseUrl;
        private BaseImageLoaderStrategy loaderStrategy;
        private GlobalHttpHandler handler;
        private List<Interceptor> interceptors;
        private ResponseErrorListener responseErrorListener;
        private File cacheFile;
        private ClientModule.RetrofitConfiguration retrofitConfiguration;
        private ClientModule.OkHttpConfiguration okHttpConfiguration;
        private ClientModule.RxCacheConfiguration rxCacheConfiguration;
        private AppModule.GsonConfiguration gsonConfiguration;
        private RequestInterceptor.Level printHttpLogLevel;

        private Builder() {
        }

        public Builder baseUrl(String baseUrl) {
            if (TextUtils.isEmpty(baseUrl)) {
                throw new NullPointerException("BaseUrl can not be empty");
            }
            this.apiUrl = HttpUrl.parse(baseUrl);
            return this;
        }

        public Builder baseUrl(BaseUrl baseUrl) {
            if (baseUrl == null) {
                throw new NullPointerException("BaseUrl can not be null");
            }
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder imageLoaderStrategy(BaseImageLoaderStrategy loaderStrategy) {//用来请求网络图片
            this.loaderStrategy = loaderStrategy;
            return this;
        }

        public Builder globalHttpHandler(GlobalHttpHandler handler) {//用来处理http响应结果
            this.handler = handler;
            return this;
        }

        public Builder addInterceptor(Interceptor interceptor) {//动态添加任意个interceptor
            if (interceptors == null)
                interceptors = new ArrayList<>();
            this.interceptors.add(interceptor);
            return this;
        }


        public Builder responseErrorListener(ResponseErrorListener listener) {//处理所有RxJava的onError逻辑
            this.responseErrorListener = listener;
            return this;
        }


        public Builder cacheFile(File cacheFile) {
            this.cacheFile = cacheFile;
            return this;
        }

        public Builder retrofitConfiguration(ClientModule.RetrofitConfiguration retrofitConfiguration) {
            this.retrofitConfiguration = retrofitConfiguration;
            return this;
        }

        public Builder okHttpConfiguration(ClientModule.OkHttpConfiguration okHttpConfiguration) {
            this.okHttpConfiguration = okHttpConfiguration;
            return this;
        }

        public Builder rxCacheConfiguration(ClientModule.RxCacheConfiguration rxCacheConfiguration) {
            this.rxCacheConfiguration = rxCacheConfiguration;
            return this;
        }

        public Builder gsonConfiguration(AppModule.GsonConfiguration gsonConfiguration) {
            this.gsonConfiguration = gsonConfiguration;
            return this;
        }

        public Builder printHttpLogLevel(RequestInterceptor.Level printHttpLogLevel) { //是否让框架打印 Http 的请求和响应信息
            if (printHttpLogLevel == null)
                throw new NullPointerException("printHttpLogLevel == null. Use RequestInterceptor.Level.NONE instead.");
            this.printHttpLogLevel = printHttpLogLevel;
            return this;
        }

        public GlobalConfigModule build() {
            return new GlobalConfigModule(this);
        }
    }
}
