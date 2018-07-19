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
    private HandlerManager handlerManager;
    Map<String, ResponseCallback> responseCallbacks = new HashMap<String, ResponseCallback>();
    private long uniqueId = 0;

    public PoseidonBridge(JavaToJsQueue queue, BridgeWebView webView) {
        this.handlerManager = new HandlerManager(webView);
        this.queue = queue;
    }

    void receiveDataFromJS(String fakeData) {
        String data = BridgeUtil.getDataFromReturnUrl(fakeData);
        Message m = Message.getMessageFromJson(data);
        if (m == null) {
            return;
        }
        String responseId = m.getResponseId();
        if (!TextUtils.isEmpty(responseId)) {
            ResponseCallback responseCallback = responseCallbacks.get(responseId);
            String responseData = m.getResponseData();
            responseCallback.receiveDataFromJs(responseData);
            responseCallbacks.remove(responseId);
        } else {
            String callbackID = m.getCallbackId();
            String serviceAndAction[] = m.getHandlerName().split("_");
            String service = serviceAndAction[0];
            String action = serviceAndAction[1];
            String rawArgs = m.getData();
            queue.setPaused(true);
            handlerManager.exec(service, action, rawArgs, callbackID);
            queue.setPaused(false);
        }
    }

    void callJsHandler(String handlerName, String data, ResponseCallback responseCallback) {
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
        queue.dispatchData2Js(m);
    }

}
