package com.tianxiabuyi.txmvp.mvp.model.bean;

import com.tianxiabuyi.txmvp.mvp.model.api.Api;

import java.io.Serializable;

/**
 * 服务器返回格式, 可能存在返回格式不一致
 * <p>
 * Created in 2017/9/21 15:52.
 *
 * @author Wang YaoDong.
 */
public class BaseJson<T> implements Serializable {

    private T data;
    private String code;
    private String msg;

    public T getData() {
        return data;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 请求是否成功
     */
    public boolean isSuccess() {
        return code.equals(Api.RequestSuccess);
    }
}
