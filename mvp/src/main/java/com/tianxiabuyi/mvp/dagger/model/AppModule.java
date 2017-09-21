package com.tianxiabuyi.mvp.dagger.model;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tianxiabuyi.mvp.manager.IRepositoryManager;
import com.tianxiabuyi.mvp.manager.RepositoryManager;

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

    @Singleton
    @Provides
    public Gson provideGson(Application application, @Nullable GsonConfiguration configuration) {
        GsonBuilder builder = new GsonBuilder();
        if (configuration != null)
            configuration.configGson(application, builder);
        return builder.create();
    }

    @Singleton
    @Provides
    public IRepositoryManager provideRepositoryManager(RepositoryManager repositoryManager) {
        return repositoryManager;
    }

    @Singleton
    @Provides
    public Map<String, Object> provideExtras() {
        return new ArrayMap<>();
    }


    public interface GsonConfiguration {
        void configGson(Context context, GsonBuilder builder);
    }
}
