package com.tianxiabuyi.txmvp.app.config;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.google.gson.GsonBuilder;
import com.squareup.leakcanary.RefWatcher;
import com.tianxiabuyi.mvp.BuildConfig;
import com.tianxiabuyi.mvp.base.delegate.IAppLifecycle;
import com.tianxiabuyi.mvp.dagger.model.AppModule;
import com.tianxiabuyi.mvp.dagger.model.ClientModule;
import com.tianxiabuyi.mvp.dagger.model.GlobalConfigModule;
import com.tianxiabuyi.mvp.http.RequestInterceptor;
import com.tianxiabuyi.mvp.manager.ConfigModule;
import com.tianxiabuyi.mvp.utils.AppUtils;
import com.tianxiabuyi.txmvp.app.lifecycle.ActivityLifecycleCallbacksImpl;
import com.tianxiabuyi.txmvp.app.lifecycle.AppLifestyleImpl;
import com.tianxiabuyi.txmvp.mvp.model.api.Api;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.rx_cache2.internal.RxCache;
import me.jessyan.progressmanager.ProgressManager;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * App 全局配置
 * <p>
 * Created in 2017/9/20 17:27.
 *
 * @author Wang YaoDong.
 */
public class GlobalConfiguration implements ConfigModule {

    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {
        // 打印框架日志, 调试
        if (BuildConfig.DEBUG) {
            builder.printHttpLogLevel(RequestInterceptor.Level.NONE);
        }
        builder.baseUrl(Api.APP_DOMAIN)
                // 自定义图片加载
                // .imageLoaderStrategy(new CustomLoaderStrategy())
                // 3种需求
                // 多个baseUrl  动态切换baseUrl 请求获取baseUrl
                // .baseUrl(new BaseUrl() {
                //      @Override
                //      public HttpUrl url() {
                //         return HttpUrl.parse(sDomain);
                //      }
                // })
                .globalHttpHandler(new GlobalHttpHandlerImpl(context))  // 全局http处理 如 token超时
                .responseErrorListener(new GlobalRespErrorListenerImpl())
                .gsonConfiguration(new AppModule.GsonConfiguration() {
                    @Override
                    public void configGson(Context context, GsonBuilder builder) {
                        // 支持序列化null的参数
                        // 支持将序列化key为object的map, 默认只能序列化key为string的map
                        builder.serializeNulls()
                                .enableComplexMapKeySerialization();
                    }
                })
                .retrofitConfiguration(new ClientModule.RetrofitConfiguration() {
                    @Override
                    public void configRetrofit(Context context, Retrofit.Builder builder) {
                        // 自定义 配置Retrofit参数
                        // 比如使用fastJson替代gson
                        // builder.addConverterFactory(FastJsonConverterFactory.create());
                    }
                })
                .okHttpConfiguration(new ClientModule.OkHttpConfiguration() {
                    @Override
                    public void configOkHttp(Context context, OkHttpClient.Builder builder) {
                        // 自定义 配置OkHttp参数
                        // builder.sslSocketFactory(); //支持 Https,详情请百度
                        builder.writeTimeout(10, TimeUnit.SECONDS);
                        // 使用一行代码监听 Retrofit／OkHttp 上传下载进度监听,以及 Glide 加载进度监听
                        // 参考 https://github.com/JessYanCoding/ProgressManager
                        ProgressManager.getInstance().with(builder);
                        // 让Retrofit 同时支持多个 BaseUrl 以及动态改变 BaseUrl.
                        // 参考 https://github.com/JessYanCoding/RetrofitUrlManager
                        RetrofitUrlManager.getInstance().with(builder);
                    }
                })
                .rxCacheConfiguration(new ClientModule.RxCacheConfiguration() {
                    @Override
                    public void configRxCache(Context context, RxCache.Builder builder) {
                        // 自定义 配置RxCache参数
                        builder.useExpiredDataIfLoaderNotAvailable(true);
                    }
                });
    }

    @Override
    public void injectAppLifecycle(Context context, List<IAppLifecycle> lifecycle) {
        lifecycle.add(new AppLifestyleImpl());
    }

    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycle) {
        lifecycle.add(new ActivityLifecycleCallbacksImpl());
    }

    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycle) {
        lifecycle.add(new FragmentManager.FragmentLifecycleCallbacks() {
            // 在配置变化的时候将这个 Fragment 保存下来,在 Activity 由于配置变化重建是重复利用已经创建的Fragment。
            // 如果在 XML 中使用 <Fragment/> 标签,的方式创建 Fragment 请务必在标签中
            // 加上 android:id 或者 android:tag 属性,否则 setRetainInstance(true) 无效
            // 在 Activity 中绑定少量的 Fragment 建议这样做,如果需要绑定较多的 Fragment 不建议设置此参数,
            // 如 ViewPager 需要展示较多 Fragment
            @Override
            public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
                f.setRetainInstance(true);
            }

            @Override
            public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
                ((RefWatcher) AppUtils.obtainAppComponentFromContext(f.getActivity())
                        .extras()
                        .get(RefWatcher.class.getName()))
                        .watch(f);
            }
        });
    }
}