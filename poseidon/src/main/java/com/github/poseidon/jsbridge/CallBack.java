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

    public void success(JSONObject message, boolean isKeep) {
        sendActionResult(new ActionResult(ActionResult.Status.OK, message).setKeepCallback(isKeep));
    }

    public void success(String message, boolean isKeep) {
        sendActionResult(new ActionResult(ActionResult.Status.OK, message).setKeepCallback(isKeep));
    }

    public void success(JSONArray message, boolean isKeep) {
        sendActionResult(new ActionResult(ActionResult.Status.OK, message).setKeepCallback(isKeep));
    }

    public void success(byte[] message, boolean isKeep) {
        sendActionResult(new ActionResult(ActionResult.Status.OK, message).setKeepCallback(isKeep));
    }

    public void success(int message, boolean isKeep) {
        sendActionResult(new ActionResult(ActionResult.Status.OK, message).setKeepCallback(isKeep));
    }

    public void success(boolean isKeep) {
        sendActionResult(new ActionResult(ActionResult.Status.OK).setKeepCallback(isKeep));
    }

    public void error(JSONObject message, boolean isKeep) {
        sendActionResult(new ActionResult(ActionResult.Status.ERROR, message).setKeepCallback(isKeep));
    }

    public void error(String message, boolean isKeep) {
        sendActionResult(new ActionResult(ActionResult.Status.ERROR, message).setKeepCallback(isKeep));
    }

    public void error(int message, boolean isKeep) {
        sendActionResult(new ActionResult(ActionResult.Status.ERROR, message).setKeepCallback(isKeep));
    }
}
