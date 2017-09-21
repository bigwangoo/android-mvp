package com.tianxiabuyi.mvp.http.ImageLoader;

import android.widget.ImageView;

/**
 * 图片加载通用配置参数
 * <p>
 * Created in 2017/9/21 10:04.
 *
 * @author Wang YaoDong.
 */
public class ImageConfig {

    protected String url;
    protected ImageView imageView;
    protected int placeholder;          // 占位符
    protected int errorPic;             // 错误占位符

    public String getUrl() {
        return url;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public int getPlaceholder() {
        return placeholder;
    }

    public int getErrorPic() {
        return errorPic;
    }
}
