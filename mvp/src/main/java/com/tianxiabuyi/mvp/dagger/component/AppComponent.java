package com.tianxiabuyi.mvp.dagger.component;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.tianxiabuyi.mvp.base.delegate.AppDelegate;
import com.tianxiabuyi.mvp.http.ImageLoader.ImageLoader;
import com.tianxiabuyi.mvp.http.error.RxErrorHandler;
import com.tianxiabuyi.mvp.manager.AppManager;
import com.tianxiabuyi.mvp.manager.IRepositoryManager;
import com.tianxiabuyi.mvp.utils.AppUtils;

import java.io.File;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * 可通过 {@link AppUtils#obtainAppComponentFromContext(Context)} 拿到此接口的实现类
 * 拥有此接口的实现类即可调用对应的方法拿到 Dagger 提供的对应实例
 * <p>
 * Created in 2017/9/20 16:19.
 *
 * @author Wang YaoDong.
 */
public interface AppComponent {

    Application application();

    /**
     * 用于管理所有 activity
     */
    AppManager appManager();

    /**
     * 用于管理网络请求层,以及数据缓存层
     */
    IRepositoryManager repositoryManager();

    /**
     * RxJava 错误处理管理类
     */
    RxErrorHandler rxErrorHandler();

    /**
     * 图片管理器,用于加载图片的管理类,默认使用 Glide ,使用策略模式,可在运行时替换框架
     */
    ImageLoader imageLoader();

    /**
     * okHttpClient
     */
    OkHttpClient okHttpClient();

    /**
     * gson
     */
    Gson gson();

    /**
     * 缓存文件根目录(RxCache 和 Glide 的缓存都已经作为子文件夹放在这个根目录下)
     * 应该将所有缓存都放到这个根目录下,便于管理和清理,可在 GlobalConfigModule 里配置
     */
    File cacheFile();

    /**
     * 用来存取一些整个App公用的数据,切勿大量存放大容量数据
     */
    Map<String, Object> extras();

    void inject(AppDelegate delegate);
}
