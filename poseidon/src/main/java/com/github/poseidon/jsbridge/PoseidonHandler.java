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
     * @param action   <div class="zh"></div>   要执行的action
     *                 <div class = "en"></div>
     * @param rawArgs  <div class="zh"></div>  JS端传递过来的JSON格式的参数
     *                 <div class = "en"></div>
     * @param callback <div class="zh"></div>  通过callback回调给JavaScript.
     *                 <div class = "en"></div>
     * @return <div class="zh"></div>  返回true，该handler才能正常运行，返回false，则js端将会收到"invalid action"的信息,默认情况下返回false.
     * <div class = "en"></div>
     * @throws JSONException JSONException
     */
    public boolean execute(String action, String rawArgs, CallBack callback) throws JSONException {
        JSONArray args = new JSONArray(rawArgs);
        return execute(action, args, callback);
    }

    /**
     * @param action   <div class="zh"></div>  要执行的action
     *                 <div class = "en"></div>
     * @param args     <div class="zh"></div>     JS端传递过来的JSON格式的参数
     *                 <div class = "en"></div>
     * @param callback <div class="zh"></div>  通过callback回调给JavaScript.
     *                 <div class = "en"></div>
     * @return <div class="zh"></div>  返回true，该handler才能正常运行，返回false，则js端将会收到"invalid action"的信息,默认情况下返回false.
     * <div class = "en"></div>
     * @throws JSONException JSONException
     */
    public boolean execute(String action, JSONArray args, CallBack callback) throws JSONException {
        return false;
    }

    /**
     * <div class="zh">将指定的数据发送给JavaScript.</div>
     * <div class = "en">Sends the specified data to JavaScript.</div>
     *
     * @param data <div class="zh">要发送的数据</div>
     *             <div class = "en">The data to be sent</div>
     */
    protected void dispatchedJSEvent(String data) {
        this.dispatchedJSEvent(null, data);
    }

    /**
     * <div class="zh">将指定的数据发送给JavaScript中注册的handler</div>
     * <div class = "en">Sends the specified data to the handler registered in JavaScript</div>
     *
     * @param handlerName <div class="zh">JavaScript中注册handler的名字</div>
     *                    <div class = "en">The name of the handler is registered in JavaScript</div>
     * @param data        <div class="zh">要发送的数据</div>
     *                    <div class = "en">The data to be sent</div>
     */
    protected void dispatchedJSEvent(String handlerName, String data) {
        this.dispatchedJSEvent(handlerName, data, null);
    }

    /**
     * <div class="zh">将指定的数据发送给JavaScript中注册的handler，同时接收来自JavaScript的回执消息</div>
     * <div class = "en">Sends the specified data to the handler registered in JavaScript and receives a receipt message from JavaScript</div>
     *
     * @param handlerName      <div class="zh">JavaScript中注册handler的名字</div>
     *                         <div class = "en">The name of the handler is registered in JavaScript</div>
     * @param data             <div class="zh">要发送的数据</div>
     *                         <div class = "en">The data to be sent</div>
     * @param responseCallback <div class="zh">用于接收JavaScript的回执消息的回调</div>
     *                         <div class = "en">A callback used to receive a return message from JavaScript</div>
     */
    protected void dispatchedJSEvent(String handlerName, String data, ResponseCallback responseCallback) {
        webview.callJshandler(handlerName, data, responseCallback);
    }


}
