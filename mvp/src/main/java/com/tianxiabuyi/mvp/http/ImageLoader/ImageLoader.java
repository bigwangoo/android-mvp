package com.tianxiabuyi.mvp.http.ImageLoader;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created in 2017/9/21 10:03.
 *
 * @author Wang YaoDong.
 */
@Singleton
public final class ImageLoader {

    private BaseImageLoaderStrategy mStrategy;

    @Inject
    public ImageLoader(BaseImageLoaderStrategy strategy) {
        setLoadImgStrategy(strategy);
    }

    /**
     * 加载图片
     */
    public <T extends ImageConfig> void loadImage(Context context, T config) {
        this.mStrategy.loadImage(context, config);
    }

    /**
     * 停止加载
     */
    public <T extends ImageConfig> void clear(Context context, T config) {
        this.mStrategy.clear(context, config);
    }

    /**
     * 加载策略
     */
    public BaseImageLoaderStrategy getLoadImgStrategy() {
        return mStrategy;
    }

    /**
     * 可在运行时切换
     */
    public void setLoadImgStrategy(BaseImageLoaderStrategy strategy) {
        this.mStrategy = strategy;
    }
}
