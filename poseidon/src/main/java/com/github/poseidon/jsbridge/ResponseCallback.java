package com.github.poseidon.jsbridge;

/**
 * java向js发送消息，js发送回执消息的回调接口
 * Created by ShuXin on 2018/7/19 22:19
 */
public interface ResponseCallback {
    void receiveDataFromJs(String data);
}
