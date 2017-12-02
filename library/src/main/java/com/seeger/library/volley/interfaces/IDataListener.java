package com.seeger.library.volley.interfaces;

/**
 * 数据处理
 *
 * @author xuzj
 */
public interface IDataListener<T> {

    /**
     * 请求成功回调方法
     *
     * @param t
     */
    void onSuccess(T t);

    /**
     * 返回失败请求方法
     *
     * @param errorCode
     * @param message
     */
    void onFailure(int errorCode, String message);
}
