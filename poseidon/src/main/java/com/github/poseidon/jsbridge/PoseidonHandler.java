package com.github.poseidon.jsbridge;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by ShuXin on 2018/7/12 18:01
 */
public abstract class PoseidonHandler {
    public BridgeWebView webview;
    public PoseidonInterface poseidon;

    public final void privateInitialize(BridgeWebView webview, PoseidonInterface poseidon) {
        this.webview = webview;
        this.poseidon = poseidon;
        initialize(webview, poseidon);
    }

    protected void initialize(BridgeWebView webview, PoseidonInterface poseidon) {

    }

    public boolean execute(String action, String rawArgs, CallBack callback) throws JSONException {
        JSONArray args = new JSONArray(rawArgs);
        return execute(action, args, callback);
    }

    public boolean execute(String action, JSONArray args, CallBack callback) throws JSONException {
        return false;
    }

    protected void dispatchedJSEvent(String data) {
        this.dispatchedJSEvent(null, data);
    }

    protected void dispatchedJSEvent(String handlerName, String data) {
        this.dispatchedJSEvent(handlerName, data, null);
    }

    protected void dispatchedJSEvent(String handlerName, String data, PoseidonBridge.ResponseFunction responseCallback) {
        webview.callJshandler(handlerName, data, responseCallback);
    }


}
