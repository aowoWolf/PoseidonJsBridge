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

    /**
     * <div class="zh">自定义的Handler继承PoseidonHandler后，有需要初始化的操作可以重写此方法</div>
     * <div class = "en">After the custom Handler inherits PoseidonHandler, there is a need to initialize the Handler to override this method</div>
     *
     * @param webview  BrdigeWebView
     * @param poseidon Poseidon
     */
    protected void initialize(BridgeWebView webview, PoseidonInterface poseidon) {

    }

    /**
     *
     * @param action
     * @param rawArgs
     * @param callback
     * @return 返回true，该handler才能正常运行，返回false，则js端将会收到"invalid action"的信息,默认情况下返回false.
     * @throws JSONException
     */
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

    protected void dispatchedJSEvent(String handlerName, String data, ResponseCallback responseCallback) {
        webview.callJshandler(handlerName, data, responseCallback);
    }


}
