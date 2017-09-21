package com.tianxiabuyi.mvp.http;

import okhttp3.HttpUrl;

/**
 * Created in 2017/9/21 13:52.
 *
 * @author Wang YaoDong.
 */
public interface BaseUrl {

    /**
     * 在调用 Retrofit API 接口之前,使用 OkHttp 或其他方式,请求到正确的 BaseUrl 并通过此方法返回
     */
    HttpUrl url();
}
