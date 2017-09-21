package com.tianxiabuyi.mvp.dagger.model;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.tianxiabuyi.mvp.http.RequestInterceptor;
import com.tianxiabuyi.mvp.http.GlobalHttpHandler;
import com.tianxiabuyi.mvp.http.error.ResponseErrorListener;
import com.tianxiabuyi.mvp.http.error.RxErrorHandler;
import com.tianxiabuyi.mvp.utils.DataUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * dagger2 第三方实例
 * <p>
 * Created in 2017/9/21 13:50.
 *
 * @author Wang YaoDong.
 */
@Module
public class ClientModule {

    private static final int CONNECT_TIMEOUT = 10;

    /**
     * 提供 {@link Retrofit}
     *
     * @param builder
     * @param client
     * @param httpUrl
     * @return
     */
    @Singleton
    @Provides
    Retrofit provideRetrofit(Application application, @Nullable RetrofitConfiguration configuration,
                             Retrofit.Builder builder, OkHttpClient client, HttpUrl httpUrl, Gson gson) {
        builder.baseUrl(httpUrl) //域名
                .client(client); //设置okHttp
        if (configuration != null)
            configuration.configRetrofit(application, builder);
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //使用rxJava
                .addConverterFactory(GsonConverterFactory.create(gson)); //使用Gson
        return builder.build();
    }

    /**
     * 提供 {@link OkHttpClient}
     *
     * @param builder
     * @return
     */
    @Singleton
    @Provides
    OkHttpClient provideClient(Application application, @Nullable OkHttpConfiguration configuration,
                               OkHttpClient.Builder builder, Interceptor intercept,
                               @Nullable List<Interceptor> interceptors,
                               @Nullable final GlobalHttpHandler handler) {

        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(intercept);
        if (handler != null)
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    return chain.proceed(handler.onHttpRequestBefore(chain, chain.request()));
                }
            });
        // 如果外部提供了interceptor的集合则遍历添加
        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        if (configuration != null)
            configuration.configOkHttp(application, builder);
        return builder.build();
    }


    @Singleton
    @Provides
    Retrofit.Builder provideRetrofitBuilder() {
        return new Retrofit.Builder();
    }


    @Singleton
    @Provides
    OkHttpClient.Builder provideClientBuilder() {
        return new OkHttpClient.Builder();
    }


    @Singleton
    @Provides
    Interceptor provideInterceptor(RequestInterceptor intercept) {
        // 打印请求信息的拦截器
        return intercept;
    }


    /**
     * 提供 {@link RxCache} RxCache缓存路径
     */
    @Singleton
    @Provides
    RxCache provideRxCache(Application application, @Nullable RxCacheConfiguration configuration,
                           @Named("RxCacheDirectory") File cacheDirectory) {
        RxCache.Builder builder = new RxCache.Builder();
        if (configuration != null)
            configuration.configRxCache(application, builder);
        return builder.persistence(cacheDirectory, new GsonSpeaker());
    }

    /**
     * 需要单独给 {@link RxCache} 提供缓存路径
     */
    @Singleton
    @Provides
    @Named("RxCacheDirectory")
    File provideRxCacheDirectory(File cacheDir) {
        File cacheDirectory = new File(cacheDir, "RxCache");
        return DataUtils.makeDirs(cacheDirectory);
    }

    /**
     * 提供处理 RxJava 错误的管理器
     */
    @Singleton
    @Provides
    RxErrorHandler proRxErrorHandler(Application application, ResponseErrorListener listener) {
        return RxErrorHandler.builder()
                .with(application)
                .responseErrorListener(listener)
                .build();
    }


    public interface RetrofitConfiguration {
        void configRetrofit(Context context, Retrofit.Builder builder);
    }

    public interface OkHttpConfiguration {
        void configOkHttp(Context context, OkHttpClient.Builder builder);
    }

    public interface RxCacheConfiguration {
        void configRxCache(Context context, RxCache.Builder builder);
    }
}
