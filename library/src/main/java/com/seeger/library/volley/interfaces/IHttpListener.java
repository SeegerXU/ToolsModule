package com.seeger.library.volley.interfaces;

import java.net.HttpURLConnection;

/**
 * 网络请求回调
 *
 * @author xuzj
 */
public interface IHttpListener {
    void onSuccess(HttpURLConnection httpEntity);

    void onFail(int errorCode, String message);
}
