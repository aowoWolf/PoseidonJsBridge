package com.github.poseidon.jsbridge;

import android.os.SystemClock;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ShuXin on 2018/7/13 15:52
 */
public class PoseidonBridge {

    private JavaToJsQueue queue;
    private BridgeWebView webView;
    private HandlerManager handlerManager;
    Map<String, ResponseFunction> responseCallbacks = new HashMap<String, ResponseFunction>();
    private long uniqueId = 0;

    public PoseidonBridge(JavaToJsQueue queue, BridgeWebView webView) {
        this.handlerManager = new HandlerManager(webView);
        this.webView = webView;
        this.queue = queue;
    }

    void receiveDataFromJS(String fakeData) {
        String data = BridgeUtil.getDataFromReturnUrl(fakeData);
        Message m = Message.getMessageFromJson(data);
        if (m == null) {
            return;
        }
        String responseId = m.getResponseId();
        // 是否是response  ResponseFunction
        if (!TextUtils.isEmpty(responseId)) {
            ResponseFunction function = responseCallbacks.get(responseId);
            String responseData = m.getResponseData();
            function.receiveDataFromJs(responseData);
            responseCallbacks.remove(responseId);
        } else {
            String callbackID = m.getCallbackId();
            String serviceAndAction[] = m.getHandlerName().split("_");
            String service = serviceAndAction[0];
            String action = serviceAndAction[1];
            String rawArgs = m.getData();
            handlerManager.exec(service, action, rawArgs, callbackID);
//            queue.flushQueue();
        }
    }

    void callJshandler(String handlerName, String data, ResponseFunction responseCallback) {
        Message m = new Message();
        if (!TextUtils.isEmpty(data)) {
            m.setData(data);
        }
        if (responseCallback != null) {
            String callbackStr = String.format(BridgeUtil.CALLBACK_ID_FORMAT, ++uniqueId + "_" + SystemClock.currentThreadTimeMillis());
            responseCallbacks.put(callbackStr, responseCallback);
            m.setCallbackId(callbackStr);
        }
        if (!TextUtils.isEmpty(handlerName)) {
            m.setHandlerName(handlerName);
        }
//        queue.enequeueMessage(m, false);
//        queue.flushQueue();
        queue.dispatchData2Js(m);
    }

    //java向js发送消息，js发送回执消息的回调接口
    public interface ResponseFunction {
        void receiveDataFromJs(String data);
    }
}
