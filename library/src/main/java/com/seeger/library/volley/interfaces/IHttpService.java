package com.seeger.library.volley.interfaces;

import java.util.Map;

/**
 * 网络请求服务
 *
 * @author xuzj
 */
public interface IHttpService {
    /**
     * 设置 url
     *
     * @param url
     */
    void setUrl(String url);

    /**
     * @param listener
     */
    void setHttpListener(IHttpListener listener);

    /**
     * @param data
     */
    void setRequestData(byte[] data);

    void excute();

    void cancel();

    boolean isCancel();

    void setRequestHeader(Map<String, String> header);

    void setRequestType(String type);
}
