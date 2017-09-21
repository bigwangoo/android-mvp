package com.tianxiabuyi.mvp.http.ImageLoader.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;

/**
 * Created in 2017/9/21 11:03.
 *
 * @author Wang YaoDong.
 */
public interface GlideLoaderOptions {

    /**
     * 配置 @{@link Glide} 的自定义参数,此方法在 @{@link Glide}
     * 初始化时执行(@{@link Glide} 在第一次被调用时初始化),只会执行一次
     *
     * @param builder {@link GlideBuilder} 此类被用来创建 Glide
     */
    void applyGlideOptions(Context context, GlideBuilder builder);
}
