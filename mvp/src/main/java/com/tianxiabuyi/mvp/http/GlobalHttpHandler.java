package com.tianxiabuyi.mvp.http;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created in 2017/9/20 17:29.
 *
 * @author Wang YaoDong.
 */
public interface GlobalHttpHandler {

    Request onHttpRequestBefore(Interceptor.Chain chain, Request request);

    Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response);

    /**
     * 空实现
     */
    GlobalHttpHandler EMPTY = new GlobalHttpHandler() {

        @Override
        public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
            //不管是否处理,都必须将request返回出去
            return request;
        }

        @Override
        public Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response) {
            // 不管是否处理,都必须将response返回出去
            return response;
        }
    };
}
