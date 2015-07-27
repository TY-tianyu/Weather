package com.ty.coolweather.util;

/**
 * Created by Tony on 2015/7/21.
 */
public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);

}
