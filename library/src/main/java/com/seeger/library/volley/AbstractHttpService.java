package com.seeger.library.volley;

import android.support.annotation.NonNull;

import com.seeger.library.volley.interfaces.IHttpListener;
import com.seeger.library.volley.interfaces.IHttpService;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * @author xuzj
 */
public abstract class AbstractHttpService implements IHttpService {

    protected String mRequestType;
    protected String mUrl;
    protected IHttpListener mHttpListener;
    protected HttpURLConnection httpClient;
    protected byte[] requestData;
    /**
     * 是否取消请求
     */
    protected boolean mHasCancel = false;

    public AbstractHttpService() {

    }

    @Override
    public void cancel() {
        mHasCancel = true;
    }

    @Override
    public boolean isCancel() {
        return mHasCancel;
    }


    @Override
    public void setUrl(@NonNull String url) {
        if (url.equals(mUrl)) {
            return;
        }
        mUrl = url;
    }

    @Override
    public void setRequestType(String requestType) {
        mRequestType = requestType;
    }

    @Override
    public void setHttpListener(IHttpListener listener) {
        mHttpListener = listener;
    }

    @Override
    public void setRequestData(byte[] data) {
        requestData = data;
    }

    @Override
    public void setRequestHeader(Map<String, String> map) {
        constructHeader(map);
    }

    private void constructHeader(Map<String, String> map) {

    }
}
