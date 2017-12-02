package com.seeger.library.volley;

import com.seeger.library.volley.interfaces.IHttpListener;

import java.net.HttpURLConnection;

/**
 * 返回字符集处理
 *
 * @author xuzj
 */
public class StringListener implements IHttpListener {

    private String TAG = this.getClass().getSimpleName();

    @Override
    public void onSuccess(HttpURLConnection httpEntity) {

    }

    @Override
    public void onFail(int errorCode, String message) {

    }
}
