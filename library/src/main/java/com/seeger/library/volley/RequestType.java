package com.seeger.library.volley;

import android.support.annotation.StringDef;

import static com.seeger.library.volley.RequestType.GET;
import static com.seeger.library.volley.RequestType.POST;

/**
 * 请求类型
 *
 * @author xuzj
 */
@StringDef({GET, POST})
public @interface RequestType {
    String GET = "get";
    String POST = "post";
}
