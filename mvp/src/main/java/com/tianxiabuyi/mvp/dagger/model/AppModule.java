package com.tianxiabuyi.mvp.dagger.model;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * 通过dagger2 注入框架实例
 * <p>
 * Created in 2017/9/21 13:50.
 *
 * @author Wang YaoDong.
 */
@Module
public class AppModule {

    private Application mApplication;

    public AppModule(Application application) {
        this.mApplication = application;
    }

    @Singleton
    @Provides
    public Application provideApplication() {
        return mApplication;
    }

    /**
     * gson
     */
    @Singleton
    @Provides
    public Gson provideGson(Application application, @Nullable GsonConfiguration configuration) {
        GsonBuilder builder = new GsonBuilder();
        if (configuration != null)
            configuration.configGson(application, builder);
        return builder.create();
    }

    /**
     * 用来存取一些整个App公用的数据,切勿大量存放大容量数据
     */
    @Singleton
    @Provides
    public Map<String, Object> provideExtras() {
        return new ArrayMap<>();
    }


    public interface GsonConfiguration {
        void configGson(Context context, GsonBuilder builder);
    }
}
