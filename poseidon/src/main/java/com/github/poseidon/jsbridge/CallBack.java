package com.github.poseidon.jsbridge;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;


public class CallBack {

    public static final String TAG = CallBack.class.getSimpleName();

    private BridgeWebView webView;
    private String callbackID;
    private boolean finished = false;

    public CallBack(BridgeWebView webView, String callbackID) {
        this.webView = webView;
        this.callbackID = callbackID;
    }

    //发送action的结果到队列中
    void sendActionResult(ActionResult entity) {
        //keepcallback等于false时，下次再调用此方法会被自动拦截
        synchronized (this) {
            if (finished) {
                Log.w(TAG, "Attempted to send a second callback for ID: " + callbackID + "\nResult was: " + entity.getMessage());
                return;
            } else {
                finished = !entity.getKeepCallBack();
            }
        }
        webView.sendActionResult(entity, callbackID);
    }

    /**
     * <div class="zh">返回成功的回调，默认返回Status.OK的状态</div>
     * <div class = "en">Returns a successful callback, and by default returns Status.OK</div>
     *
     * @param message <div class="zh">成功的信息</div>
     *                <div class = "en">Success Message</div>
     * @param isKeep  <div class="zh">当isKeep等于true的时候，回调接口会一直被打开，你可以一直调用这个接口。建议一般使用的时候设置成false</div>
     *                <div class = "en">When isKeep equals true, the callback interface is always open and you can call it all the time.Recommended general use</div>
     */
    public void success(JSONObject message, boolean isKeep) {
        sendActionResult(new ActionResult(ActionResult.Status.OK, message).setKeepCallback(isKeep));
    }


    /**
     * <div class="zh">返回成功的回调，默认返回Status.OK的状态</div>
     * <div class = "en">Returns a successful callback, and by default returns Status.OK</div>
     *
     * @param message <div class="zh">成功的信息</div>
     *                <div class = "en">Success Message</div>
     * @param isKeep  <div class="zh">当isKeep等于true的时候，回调接口会一直被打开，你可以一直调用这个接口。建议一般使用的时候设置成false</div>
     *                <div class = "en">When isKeep equals true, the callback interface is always open and you can call it all the time.Recommended general use</div>
     */
    public void success(String message, boolean isKeep) {
        sendActionResult(new ActionResult(ActionResult.Status.OK, message).setKeepCallback(isKeep));
    }

    /**
     * <div class="zh">返回成功的回调，默认返回Status.OK的状态</div>
     * <div class = "en">Returns a successful callback, and by default returns Status.OK</div>
     *
     * @param message <div class="zh">成功的信息</div>
     *                <div class = "en">Success Message</div>
     * @param isKeep  <div class="zh">当isKeep等于true的时候，回调接口会一直被打开，你可以一直调用这个接口。建议一般使用的时候设置成false</div>
     *                <div class = "en">When isKeep equals true, the callback interface is always open and you can call it all the time.Recommended general use</div>
     */
    public void success(JSONArray message, boolean isKeep) {
        sendActionResult(new ActionResult(ActionResult.Status.OK, message).setKeepCallback(isKeep));
    }

    /**
     * <div class="zh">返回成功的回调，默认返回Status.OK的状态</div>
     * <div class = "en">Returns a successful callback, and by default returns Status.OK</div>
     *
     * @param message <div class="zh">成功的信息</div>
     *                <div class = "en">Success Message</div>
     * @param isKeep  <div class="zh">当isKeep等于true的时候，回调接口会一直被打开，你可以一直调用这个接口。建议一般使用的时候设置成false</div>
     *                <div class = "en">When isKeep equals true, the callback interface is always open and you can call it all the time.Recommended general use</div>
     */
    public void success(byte[] message, boolean isKeep) {
        sendActionResult(new ActionResult(ActionResult.Status.OK, message).setKeepCallback(isKeep));
    }

    /**
     * <div class="zh">返回成功的回调，默认返回Status.OK的状态</div>
     * <div class = "en">Returns a successful callback, and by default returns Status.OK</div>
     *
     * @param message <div class="zh">成功的信息</div>
     *                <div class = "en">Success Message</div>
     * @param isKeep  <div class="zh">当isKeep等于true的时候，回调接口会一直被打开，你可以一直调用这个接口。建议一般使用的时候设置成false</div>
     *                <div class = "en">When isKeep equals true, the callback interface is always open and you can call it all the time.Recommended general use</div>
     */
    public void success(int message, boolean isKeep) {
        sendActionResult(new ActionResult(ActionResult.Status.OK, message).setKeepCallback(isKeep));
    }

    /**
     * <div class="zh">返回成功的回调，默认返回Status.OK的状态</div>
     * <div class = "en">Returns a successful callback, and by default returns Status.OK</div>
     *
     * @param isKeep <div class="zh">当isKeep等于true的时候，回调接口会一直被打开，你可以一直调用这个接口。建议一般使用的时候设置成false</div>
     *               <div class = "en">When isKeep equals true, the callback interface is always open and you can call it all the time.Recommended general use</div>
     */
    public void success(boolean isKeep) {
        sendActionResult(new ActionResult(ActionResult.Status.OK).setKeepCallback(isKeep));
    }

    /**
     * <div class="zh">返回成功的回调，默认返回Status.ERROR的状态</div>
     * <div class = "en">Returns a successful callback, and by default returns Status.ERROR</div>
     *
     * @param message <div class="zh">错误的信息</div>
     *                <div class = "en">Error Message</div>
     * @param isKeep  <div class="zh">当isKeep等于true的时候，回调接口会一直被打开，你可以一直调用这个接口。建议一般使用的时候设置成false</div>
     *                <div class = "en">When isKeep equals true, the callback interface is always open and you can call it all the time.Recommended general use</div>
     */
    public void error(JSONObject message, boolean isKeep) {
        sendActionResult(new ActionResult(ActionResult.Status.ERROR, message).setKeepCallback(isKeep));
    }

    /**
     * <div class="zh">返回成功的回调，默认返回Status.ERROR的状态</div>
     * <div class = "en">Returns a successful callback, and by default returns Status.ERROR</div>
     *
     * @param message <div class="zh">错误的信息</div>
     *                <div class = "en">Error Message</div>
     * @param isKeep  <div class="zh">当isKeep等于true的时候，回调接口会一直被打开，你可以一直调用这个接口。建议一般使用的时候设置成false</div>
     *                <div class = "en">When isKeep equals true, the callback interface is always open and you can call it all the time.Recommended general use</div>
     */
    public void error(String message, boolean isKeep) {
        sendActionResult(new ActionResult(ActionResult.Status.ERROR, message).setKeepCallback(isKeep));
    }

    /**
     * <div class="zh">返回成功的回调，默认返回Status.ERROR的状态</div>
     * <div class = "en">Returns a successful callback, and by default returns Status.ERROR</div>
     *
     * @param message <div class="zh">错误的信息</div>
     *                <div class = "en">Error Message</div>
     * @param isKeep  <div class="zh">当isKeep等于true的时候，回调接口会一直被打开，你可以一直调用这个接口。建议一般使用的时候设置成false</div>
     *                <div class = "en">When isKeep equals true, the callback interface is always open and you can call it all the time.Recommended general use</div>
     */
    public void error(int message, boolean isKeep) {
        sendActionResult(new ActionResult(ActionResult.Status.ERROR, message).setKeepCallback(isKeep));
    }
}
