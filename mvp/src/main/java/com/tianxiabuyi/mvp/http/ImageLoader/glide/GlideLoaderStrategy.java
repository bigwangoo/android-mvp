package com.tianxiabuyi.mvp.http.ImageLoader.glide;

import android.content.Context;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.tianxiabuyi.mvp.http.ImageLoader.BaseImageLoaderStrategy;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Glide 加载策略
 * <p>
 * Created in 2017/9/21 11:04.
 *
 * @author Wang YaoDong.
 */
public class GlideLoaderStrategy implements BaseImageLoaderStrategy<GlideImageConfig>, GlideLoaderOptions {

    @Override
    public void loadImage(Context ctx, GlideImageConfig config) {
        if (ctx == null) throw new NullPointerException("Context is required");
        if (config == null) throw new NullPointerException("ImageConfigImpl is required");
        if (TextUtils.isEmpty(config.getUrl())) throw new NullPointerException("Url is required");
        if (config.getImageView() == null) throw new NullPointerException("ImageView is required");
    }

    @Override
    public void clear(final Context ctx, GlideImageConfig config) {
        if (ctx == null) throw new NullPointerException("Context is required");
        if (config == null) throw new NullPointerException("ImageConfigImpl is required");

        if (config.isClearDiskCache()) {//清除本地缓存
            Observable.just(0)
                    .observeOn(Schedulers.io())
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(@NonNull Integer integer) throws Exception {
                            Glide.get(ctx).clearDiskCache();
                        }
                    });
        }

        if (config.isClearMemory()) {//清除内存缓存
            Observable.just(0)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(@NonNull Integer integer) throws Exception {
                            Glide.get(ctx).clearMemory();
                        }
                    });
        }
    }


    @Override
    public void applyGlideOptions(Context context, GlideBuilder builder) {
        Timber.w("applyGlideOptions");
    }
}
