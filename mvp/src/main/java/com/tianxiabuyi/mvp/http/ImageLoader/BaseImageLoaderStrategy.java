package com.tianxiabuyi.mvp.http.ImageLoader;

import android.content.Context;

/**
 * 图片加载策略
 * <p>
 * Created in 2017/9/21 10:10.
 *
 * @author Wang YaoDong.
 */
public interface BaseImageLoaderStrategy<T extends ImageConfig> {

    /**
     * 加载图片
     *
     * @param ctx
     * @param config
     */
    void loadImage(Context ctx, T config);

    /**
     * 停止加载
     *
     * @param ctx
     * @param config
     */
    void clear(Context ctx, T config);
}
